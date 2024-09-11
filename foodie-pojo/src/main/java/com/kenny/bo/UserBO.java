package com.kenny.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "user dto", description = "Data were passed from client side and encapsulated into UserBO")
public class UserBO {
    @ApiModelProperty(value = "user name", name = "username", example = "kenny", required = true)
    private String username;
    @ApiModelProperty(value = "password", name = "password", example = "12345", required = true)
    private String password;
    @ApiModelProperty(value = "confirmed password", name = "confirmPassword", example = "123456", required = false)
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
