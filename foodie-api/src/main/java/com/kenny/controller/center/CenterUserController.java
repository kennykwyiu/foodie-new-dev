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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "User Information API", tags = {"APIs related to user information"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController {
    @Autowired
    private CenterUserService centerUserService;
    @PostMapping("/update")
    public JsonResult update(@ApiParam(name = "userId", value = "用户id", required = true)
                             @RequestParam String userId,
                             @RequestBody @Valid CenterUserBO centerUserBO,
                             BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        System.out.println(centerUserBO);

        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return JsonResult.errorMap(errorMap);
        }

        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);
        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), true);

        // TODO: Future improvement - Add token authentication, integrate with Redis for distributed sessions

        return JsonResult.ok();
    }

    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            String errorField = error.getField();
            String errorMsg = error.getDefaultMessage();
            map.put(errorField,errorMsg);
        }
        return map;
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
