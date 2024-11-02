package com.kenny.controller;

import com.kenny.bo.ShopcartBO;
import com.kenny.bo.UserBO;
import com.kenny.pojo.Users;
import com.kenny.service.UserService;
import com.kenny.utils.*;
import com.kenny.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Api(value = "register login", tags = {"api for register and login"})
@RestController
@RequestMapping("passport")
public class PassportController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "is username exist or not", notes = "is username exist or not", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public JsonResult usernameIsExist(@RequestParam String username) {
        if (StringUtils.isBlank(username)) {
            return JsonResult.errorMsg("username cannot be null");
        }

        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return JsonResult.errorMsg("username is existed");
        }

        return JsonResult.ok();
    }

    @ApiOperation(value = "user register", notes = "user register", httpMethod = "POST")
    @PostMapping("/register")
    public JsonResult register(@RequestBody UserBO userBO,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)) {
            return JsonResult.errorMsg("username or password cannot be empty!");
        }

        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return JsonResult.errorMsg("username is existed");
        }

        if (password.length() < 6) {
            return JsonResult.errorMsg("password cannot be less than 6 letters!");
        }

        if (!password.equals(confirmPassword)) {
            return JsonResult.errorMsg("password is not same with confirmed password!");
        }

        Users users = userService.createUser(userBO);

//        users = setNullProperty(users);

        // TODO Generate user token and store it in the Redis session
        UserVO userVO = convertUserVo(users);

        CookieUtils.setCookie(request,
                            response,
                            "user",
                            JsonUtils.objectToJson(userVO),
                            true);

        // TODO Synchronize shopping cart data
        synchShopcartData(users.getId(), request, response);

        return JsonResult.ok();
    }

    private UserVO convertUserVo(Users users) {
        // Implementing user session using Redis
        String uniqueToken = UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN + ":" + users.getId(),
                          uniqueToken);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(users, userVO);
        userVO.setUserUniqueToken(uniqueToken);
        return userVO;
    }

    @ApiOperation(value = "user login", notes = "user login", httpMethod = "POST")
    @PostMapping("/login")
    public JsonResult login(@RequestBody UserBO userBO,
                            HttpServletRequest request,
                            HttpServletResponse response) throws NoSuchAlgorithmException {
        String username = userBO.getUsername();
        String password = userBO.getPassword();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return JsonResult.errorMsg("username or password cannot be empty!");
        }

        String md5Str = MD5Utils.getMD5Str(password);

        Users users = userService.queryUserForLogin(username, md5Str);

        if (users == null) {
            return JsonResult.errorMsg("username or password is incorrect!");
        }

        // TODO Generate user token and store it in the Redis session
//        users = setNullProperty(users);
        UserVO userVO = convertUserVo(users);

        CookieUtils.setCookie(request,
                            response,
                            "user",
                            JsonUtils.objectToJson(userVO),
                            true);

        // TODO Synchronize shopping cart data
        synchShopcartData(users.getId(), request, response);

        return JsonResult.ok(users);
    }

    /**
     * After successful registration and login, synchronize shopping cart data between cookies and Redis.
     */
    private void synchShopcartData(String userId, HttpServletRequest request,
                                   HttpServletResponse response) {

        /**
         * 1. If there is no data in Redis and the cookie's shopping cart is empty, do nothing.
         *    If the cookie's shopping cart is not empty, directly put it into Redis.
         * 2. If there is data in Redis and the cookie's shopping cart is empty, directly overwrite the local cookie with the Redis shopping cart.
         *    If the cookie's shopping cart is not empty,
         *        - If a product in the cookie also exists in Redis,
         *          then prioritize the cookie, delete it from Redis,
         *          and directly overwrite the product from the cookie into Redis (similar to Jingdong).
         * 3. After synchronization to Redis, update the data in the local cookie shopping cart to ensure it is up to date.
         */

        // Get the shopping cart from Redis
        String shopcartJsonRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);

        // Get the shopping cart from the cookie
        String shopcartStrCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);

        if (StringUtils.isBlank(shopcartJsonRedis) && StringUtils.isNotBlank(shopcartStrCookie)) {
            // If Redis is empty but the cookie is not, put the data from the cookie into Redis
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, shopcartStrCookie);
        } else {
            // If Redis is not empty and the cookie is not empty, merge the data from the cookie and Redis shopping carts (cookie data overrides Redis for the same product)
            if (StringUtils.isNotBlank(shopcartStrCookie)) {

                /**
                 * 1. For existing items, overwrite the quantity from the cookie to Redis (like Jingdong)
                 * 2. Mark the item to be deleted and add it to a pending delete list
                 * 3. Clean up all items marked for deletion from the cookie
                 * 4. Merge data from Redis and the cookie
                 * 5. Update to Redis and the cookie
                 */

                List<ShopcartBO> shopcartListRedis = JsonUtils.jsonToList(shopcartJsonRedis, ShopcartBO.class);
                List<ShopcartBO> shopcartListCookie = JsonUtils.jsonToList(shopcartStrCookie, ShopcartBO.class);

                // Define a list for pending deletions
                List<ShopcartBO> pendingDeleteList = new ArrayList<>();

                for (ShopcartBO redisShopcart : shopcartListRedis) {
                    String redisSpecId = redisShopcart.getSpecId();

                    for (ShopcartBO cookieShopcart : shopcartListCookie) {
                        String cookieSpecId = cookieShopcart.getSpecId();

                        if (redisSpecId.equals(cookieSpecId)) {
                            // Overwrite the purchase quantity, do not accumulate, similar to Jingdong
                            redisShopcart.setBuyCounts(cookieShopcart.getBuyCounts());
                            // Add the cookieShopcart to the pending delete list for later removal and merging
                            pendingDeleteList.add(cookieShopcart);
                        }
                    }
                }

                // Remove the overwritten product data from the current cookie
                shopcartListCookie.removeAll(pendingDeleteList);

                // Merge the two lists
                shopcartListRedis.addAll(shopcartListCookie);
                // Update in Redis and the cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartListRedis), true);
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartListRedis));
            } else {
                // If Redis is not empty but the cookie is empty, overwrite the cookie with Redis
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, shopcartJsonRedis, true);
            }
        }
    }

    private void synchShopcartDataUsingMap(String userId, HttpServletRequest request,
                                   HttpServletResponse response) {
        // Get the shopping cart from Redis
        String shopcartJsonRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);

        // Get the shopping cart from the cookie
        String shopcartStrCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);

        if (StringUtils.isBlank(shopcartJsonRedis) && StringUtils.isNotBlank(shopcartStrCookie)) {
            // If Redis is empty but the cookie is not, put the data from the cookie into Redis
            redisOperator.set(FOODIE_SHOPCART + ":" + userId, shopcartStrCookie);
        } else {
            // If Redis is not empty and the cookie is not empty, merge the data from the cookie and Redis shopping carts (cookie data overrides Redis for the same product)
            if (StringUtils.isNotBlank(shopcartStrCookie)) {
                List<ShopcartBO> shopcartListRedis = JsonUtils.jsonToList(shopcartJsonRedis, ShopcartBO.class);
                List<ShopcartBO> shopcartListCookie = JsonUtils.jsonToList(shopcartStrCookie, ShopcartBO.class);

                // Define a list for pending deletions
                List<ShopcartBO> pendingDeleteList = new ArrayList<>();
                List<ShopcartBO> newShopcartList = new ArrayList<>();

                // Use HashMap for faster lookup
                Map<String, ShopcartBO> redisMap = new HashMap<>();

                for (ShopcartBO shopcartRedis : shopcartListRedis) {
                    redisMap.put(shopcartRedis.getSpecId(), shopcartRedis);
                }

                for (ShopcartBO cookieShopcart : shopcartListCookie) {
                    String cookieShopcartSpecId = cookieShopcart.getSpecId();

                    if (redisMap.containsKey(cookieShopcartSpecId)) {
                        ShopcartBO redisShopcartBO = redisMap.get(cookieShopcartSpecId);
                        redisShopcartBO.setBuyCounts(cookieShopcart.getBuyCounts());
                        pendingDeleteList.add(cookieShopcart);
                    } else {
                        newShopcartList.add(cookieShopcart);
                    }
                }

                newShopcartList.addAll(shopcartListRedis);

                // Update Redis and the cookie with the merged list
                shopcartListRedis.clear();
                shopcartListRedis.addAll(newShopcartList);

                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartListRedis), true);
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartListRedis));


            } else {
                // If Redis is not empty but the cookie is empty, overwrite the cookie with Redis
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, shopcartJsonRedis, true);
            }
        }
    }

    private Users setNullProperty(Users users) {
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        users.setBirthday(null);

        return users;
    }

    @ApiOperation(value = "User Logout", notes = "User Logout", httpMethod = "POST")
    @PostMapping("/logout")
    public JsonResult logout(@RequestParam String userId,
                             HttpServletRequest request,
                             HttpServletResponse response) {

        CookieUtils.deleteCookie(request, response, "user");


        // TODO User logout, need to clear the shopping cart
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);

        // TODO In a distributed session, user data needs to be cleared
        // User logout, need to clear session in Redis
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);

        return JsonResult.ok();
    }
}
