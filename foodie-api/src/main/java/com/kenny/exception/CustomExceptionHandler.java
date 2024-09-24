package com.kenny.exception;

import com.kenny.utils.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JsonResult handleMaxUploadFile(MaxUploadSizeExceededException exception) {
        return JsonResult.errorMsg("File upload size cannot exceed 500k, please compress the image or reduce image quality before uploading!");
    }

}
