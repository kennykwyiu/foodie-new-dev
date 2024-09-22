package com.kenny.controller.center;

import com.kenny.pojo.Users;
import com.kenny.service.center.CenterUserService;
import com.kenny.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "center - User Center", tags = {"APIs related to user center display"})
@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "Get user information", notes = "Get user information", httpMethod = "GET")
    @GetMapping("/userInfo")
    public JsonResult userInfo(
            @ApiParam(name = "userId", value = "User ID", required = true)
            @RequestParam String userId) {

        Users user = centerUserService.queryUserInfo(userId);
        return JsonResult.ok(user);
    }


}
