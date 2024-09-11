package com.kenny.controller;

import com.kenny.bo.UserBO;
import com.kenny.service.UserService;
import com.kenny.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public JsonResult register(@RequestBody UserBO userBO) {
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

        userService.createUser(userBO);

        return JsonResult.ok();
    }
}
