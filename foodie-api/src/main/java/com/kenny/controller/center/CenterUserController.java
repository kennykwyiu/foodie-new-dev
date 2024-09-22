package com.kenny.controller.center;

import com.kenny.bo.CenterUserBO;
import com.kenny.pojo.Users;
import com.kenny.service.center.CenterUserService;
import com.kenny.utils.CookieUtils;
import com.kenny.utils.JsonResult;
import com.kenny.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "User Information API", tags = {"APIs related to user information"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController {
    @Autowired
    private CenterUserService centerUserService;
    @PostMapping("/update")
    public JsonResult update(@ApiParam(name = "userId", value = "用户id", required = true)
                             @RequestParam String userId,
                             @RequestBody CenterUserBO centerUserBO,
                             HttpServletRequest request, HttpServletResponse response) {
        System.out.println(centerUserBO);

        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);
        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), true);

        // TODO: Future improvement - Add token authentication, integrate with Redis for distributed sessions

        return JsonResult.ok();
    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }
}
