package com.kenny.controller;

import com.kenny.pojo.Users;
import com.kenny.service.UserService;
import com.kenny.utils.JsonUtils;
import com.kenny.utils.MD5Utils;
import com.kenny.utils.RedisOperator;
import com.kenny.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Controller
public class SSOController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_TICKET = "redis_user_ticket";
    public static final String REDIS_TMP_TICKET = "redis_tmp_ticket";
    public static final String COOKIE_USER_TICKET = "cookie_user_ticket";

    final static Logger logger = LoggerFactory.getLogger(SSOController.class);

    @GetMapping("/login")
    public String login(String returnUrl,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) throws NoSuchAlgorithmException {

        model.addAttribute("returnUrl", returnUrl);
        // 1. Obtain the userTicket ticket. If it can be retrieved from the cookie, it indicates that the user has logged in.
        // At this point, issue a one-time temporary ticket and redirect.
        String userTicket = getCookie(request, COOKIE_USER_TICKET);
        boolean isVerified = verifyUserTicket(userTicket);
        if (isVerified) {
            String tmpTicket = createTmpTicket();
            return "redirect:" + returnUrl + "?tmpTicket=" +  tmpTicket;
        }

        // 2. If the user has never logged in before,
        // redirect to the CAS unified login page upon the first visit
        return "login";
    }

    /**
     * Verify the CAS global user ticket
     */
    private boolean verifyUserTicket(String userTicket) {
        // 0. Verify that the CAS ticket must not be empty
        if (StringUtils.isBlank(userTicket)) {
            return false;
        }

        // 1. Verify the validity of the CAS ticket
        String userId = redisOperator.get(REDIS_USER_TICKET + ":" + userTicket);
        if (StringUtils.isBlank(userId)) {
            return false;
        }

        // 2. Verify if the user session corresponding to the ticket exists
        String userRedis = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(userRedis)) {
            return false;
        }

        return true;
    }

    /**
     * CAS Unified Login Interface
     * Purpose:
     * 1. Create a global session for the user after login                     -> uniqueToken
     * 2. Create a global ticket for the user to indicate CAS login status     -> userTicket
     * 3. Create a temporary ticket for the user for callback transmission     -> tmpTicket
     */
    @PostMapping("/doLogin")
    public String doLogin(String username,
                          String password,
                          String returnUrl,
                          Model model,
                          HttpServletRequest request,
                          HttpServletResponse response) throws NoSuchAlgorithmException {

        model.addAttribute("returnUrl", returnUrl);

        // 0. Check that the username and password must not be empty
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            model.addAttribute("errmsg", "username or password cannot be empty!");
            return "login";
        }

        // 1. Implement login
        String md5Str = MD5Utils.getMD5Str(password);

        Users users = userService.queryUserForLogin(username, md5Str);

        if (users == null) {
            model.addAttribute("errmsg", "username or password is incorrect!");
            return "login";
        }

        // 2 Implementing user session using Redis
        String uniqueToken = UUID.randomUUID().toString().trim();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(users, userVO);
        userVO.setUserUniqueToken(uniqueToken);

        redisOperator.set(REDIS_USER_TOKEN + ":" + users.getId(),
                JsonUtils.objectToJson(userVO));

        // 3. Generate a ticket, a global ticket representing that the user has logged in to the CAS system
        String userTicket = UUID.randomUUID().toString().trim();

        // 3.1 User's global ticket needs to be placed in the CAS cookie
        setCookie(COOKIE_USER_TICKET, userTicket, response);

        // 4. Associate userTicket with the user ID, and store it in Redis, indicating that the user has a ticket and can visit various attractions
        redisOperator.set(REDIS_USER_TICKET + ":" + userTicket, users.getId());

        // 5. Generate a temporary ticket to return to the calling website, issued by the CAS system as a one-time temporary ticket
        String tmpTicket = createTmpTicket();

        /**
         * userTicket: Used to indicate a user's login status in the CAS system: logged in
         * tmpTicket: Issued to the user for one-time validation, with a limited validity period
         */

        /**
         * Example:
         *      When we go to the zoo to play, we buy a unified ticket at the entrance, which is the global ticket and user global session of the CAS system.
         *      Inside the zoo, there are some small attractions where you need to collect a one-time ticket with your main ticket. With this ticket, you can visit these small attractions.
         *      These small attractions correspond to the individual sites we are talking about here.
         *      After using this temporary ticket, it needs to be destroyed.
         */

//        return "login";
        return "redirect:" + returnUrl + "?tmpTicket=" +  tmpTicket;
    }

    @PostMapping("/verifyTmpTicket")
    @ResponseBody
    public JsonResult verifyTmpTicket(String tmpTicket,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws NoSuchAlgorithmException {

        String tmpTicketValue = redisOperator.get(REDIS_TMP_TICKET + ":" + tmpTicket);

        if (StringUtils.isBlank(tmpTicketValue)) {
            return JsonResult.errorUserTicket("User ticket exception");
        }

        if (!tmpTicketValue.equals(MD5Utils.getMD5Str(tmpTicket))) {
            return JsonResult.errorUserTicket("User ticket exception");
        } else {
            redisOperator.del(REDIS_TMP_TICKET + ":" + tmpTicket);
        }


    private String createTmpTicket() {
        String tmpTicket = UUID.randomUUID().toString().trim();

        try {
            redisOperator.set(REDIS_TMP_TICKET + ":" + tmpTicket,
                                MD5Utils.getMD5Str(tmpTicket),
                                600);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return tmpTicket;
    }

    private void setCookie(String key,
                           String val,
                           HttpServletResponse response) {
        Cookie cookie = new Cookie(key, val);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || StringUtils.isBlank(key)) {
            return null;
        }

        String cookieValue = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                cookieValue = cookie.getValue();
                break;
            }
        }
        return cookieValue;
    }
}
