package com.kenny.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @Title: JsonResult.java
 * @Package com.kenny.utils
 * @Description: Custom response data structure
 * 				This class can be used by H5/iOS/Android/WeChat Mini Program
 * 				Front-end can implement related functions based on the received data (json object)
 *
 * 				200: Success
 * 				500: Error, error message is in the msg field
 * 				501: Bean validation error, return in map format regardless of the number of errors
 * 				502: Interceptor intercepted user token error
 * 				555: Exception thrown information
 * 				556: User QQ verification exception
 * 			    557: Validating whether a user is logged in to CAS and verifying the user ticket
 */
public class JsonResult {

    // Define jackson object
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Response business status
    private Integer status;

    // Response message
    private String msg;

    // Data in the response
    private Object data;

    @JsonIgnore
    private String ok;	// Not used

    public static JsonResult build(Integer status, String msg, Object data) {
        return new JsonResult(status, msg, data);
    }

    public static JsonResult build(Integer status, String msg, Object data, String ok) {
        return new JsonResult(status, msg, data, ok);
    }

    public static JsonResult ok(Object data) {
        return new JsonResult(data);
    }

    public static JsonResult ok() {
        return new JsonResult(null);
    }

    public static JsonResult errorMsg(String msg) {
        return new JsonResult(500, msg, null);
    }

    public static JsonResult errorMap(Object data) {
        return new JsonResult(501, "error", data);
    }

    public static JsonResult errorTokenMsg(String msg) {
        return new JsonResult(502, msg, null);
    }

    public static JsonResult errorException(String msg) {
        return new JsonResult(555, msg, null);
    }

    public static JsonResult errorUserQQ(String msg) {
        return new JsonResult(556, msg, null);
    }

    public static JsonResult errorUserTicket(String msg) {
        return new JsonResult(557, msg, null);
    }

    public JsonResult() {

    }

    public JsonResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public JsonResult(Integer status, String msg, Object data, String ok) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.ok = ok;
    }

    public JsonResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

}