package com.kenny.controller.center;

import com.kenny.bo.CenterUserBO;
import com.kenny.controller.BaseController;
import com.kenny.pojo.Users;
import com.kenny.resource.FileUpload;
import com.kenny.service.center.CenterUserService;
import com.kenny.utils.CookieUtils;
import com.kenny.utils.DateUtil;
import com.kenny.utils.JsonResult;
import com.kenny.utils.JsonUtils;
import com.kenny.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "User Information API", tags = {"APIs related to user information"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {
    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @ApiOperation(value = "User avatar update", notes = "User avatar update", httpMethod = "POST")
    @PostMapping("uploadFace")
    public JsonResult uploadFace(@ApiParam(name = "userId", value = "User ID", required = true)
                                 @RequestParam String userId,
                                 @ApiParam(name = "file", value = "User avatar", required = true)
                                 MultipartFile file,
                                 HttpServletRequest request, HttpServletResponse response) {

//        String fileSpace = IMAGE_USER_FACE_LOCATION;
        String fileSpace = fileUpload.getImageUserFaceLocation();

        String uploadPathPrefix = File.separator + userId;

        if (file != null) {
            FileOutputStream fileOutputStream = null;

            try {
                String filename = file.getOriginalFilename();
                if (StringUtils.isNoneBlank(filename)) {

                    // face-{userId}.png <- save as this name
                    String[] fileNameArr = filename.split("\\.");
                    String suffix = fileNameArr[fileNameArr.length - 1];

                    // // .sh .php  -> prevent penetration by hacker
                    if (!suffix.equalsIgnoreCase("png") &&
                            !suffix.equalsIgnoreCase("jpg") &&
                            !suffix.equalsIgnoreCase("jpeg")) {
                        return JsonResult.errorMsg("Incorrect image format!");
                    }
                    String newFileName = "face-" + userId + "." + suffix;
                    String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;

                    uploadPathPrefix += File.separator + newFileName;

                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null) {
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }


        } else {
            return JsonResult.errorMsg("File cannot be empty!");
        }

        String imageServerUrl = fileUpload.getImageServerUrl();
        String  finalServerUrl = imageServerUrl + uploadPathPrefix + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        Users userResult = centerUserService.updateUserFace(userId, finalServerUrl);
//        userResult = setNullProperty(userResult);

        // TODO: Future improvement - Add token authentication, integrate with Redis for distributed sessions
        UserVO userVO = convertUserVo(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userVO), true);

        return JsonResult.ok();
    }

    @ApiOperation(value = "Modify user information", notes = "Modify user information", httpMethod = "POST")
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
//        userResult = setNullProperty(userResult);

        // TODO: Future improvement - Add token authentication, integrate with Redis for distributed sessions
        UserVO userVO = convertUserVo(userResult);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userVO), true);

        return JsonResult.ok();
    }

    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            String errorField = error.getField();
            String errorMsg = error.getDefaultMessage();
            map.put(errorField, errorMsg);
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
