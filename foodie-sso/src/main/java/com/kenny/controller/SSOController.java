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

    final static Logger logger = LoggerFactory.getLogger(SSOController.class);

    @GetMapping("/login")
    public String login(String returnUrl,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) throws NoSuchAlgorithmException {

        model.addAttribute("returnUrl", returnUrl);

        return "login";
    }

    /**
     * CAS Unified Login Interface
     *      Purpose:
     *          1. Create a global session for the user after login                     -> uniqueToken
     *          2. Create a global ticket for the user to indicate CAS login status     -> userTicket
     *          3. Create a temporary ticket for the user for callback transmission     -> tmpTicket
     */
    @PostMapping("/doLogin")
    public String doLogin(String username,
                          String password,
                          String returnUrl,
                          Model model,
                          HttpServletRequest request,
                          HttpServletResponse response) throws NoSuchAlgorithmException {

        model.addAttribute("returnUrl", returnUrl);

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            model.addAttribute("errmsg","username or password cannot be empty!");
            return "login";
        }

        String md5Str = MD5Utils.getMD5Str(password);

        Users users = userService.queryUserForLogin(username, md5Str);

        if (users == null) {
            model.addAttribute("errmsg","username or password is incorrect!");
            return "login";
        }

        // Implementing user session using Redis
        String uniqueToken = UUID.randomUUID().toString().trim();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(users, userVO);
        userVO.setUserUniqueToken(uniqueToken);

        redisOperator.set(REDIS_USER_TOKEN + ":" + users.getId(),
                JsonUtils.objectToJson(userVO));

        return "login";
    }

}
