package com.kenny.controller;

import com.kenny.bo.UserBO;
import com.kenny.pojo.Users;
import com.kenny.service.UserService;
import com.kenny.utils.CookieUtils;
import com.kenny.utils.JsonResult;
import com.kenny.utils.JsonUtils;
import com.kenny.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

@Api(value = "register login", tags = {"api for register and login"})
@RestController
@RequestMapping("passport")
public class PassportController {
    @Autowired
    private UserService userService;

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

        users = setNullProperty(users);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(users), true);

        // TODO Generate user token and store it in the Redis session
        // TODO Synchronize shopping cart data

        return JsonResult.ok();
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

        users = setNullProperty(users);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(users), true);

        return JsonResult.ok(users);
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
    // TODO In a distributed session, user data needs to be cleared

        return JsonResult.ok();
    }
}
