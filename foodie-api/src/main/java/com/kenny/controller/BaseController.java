package com.kenny.controller;

import org.springframework.stereotype.Controller;

import java.io.File;

@Controller
public class BaseController {

    // Payment center's invocation URL
    String paymentUrl = "http://api.z.mukewang.com/foodie-payment/payment/createMerchantOrder"; // Production
//    String paymentUrl = "http://localhost:8088/foodie-payment/payment/createMerchantOrder"; // Production

    // WeChat payment success -> Payment center -> Daily Gourmet Platform
    //                                  |-> Callback notification URL
//    String payReturnUrl = "http://api.z.mukewang.com/foodie-dev-api/orders/notifyMerchantOrderPaid";
    String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";

    public static final String FOODIE_SHOPCART = "shopcart";
    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    // Location for user avatar uploads
//    public static final String IMAGE_USER_FACE_LOCATION = File.separator + "workspaces" +
//            File.separator + "images" +
//            File.separator + "foodie" +
//            File.separator + "faces";
    //public static final String IMAGE_USER_FACE_LOCATION = "C:\Users\yaowi\Desktop\java\images\foodie\faces";

    public static final String IMAGE_USER_FACE_LOCATION = "C:" + File.separator + "Users" + File.separator + "yaowi" +
            File.separator + "Desktop" + File.separator + "java" +
            File.separator + "images" + File.separator + "foodie" +
            File.separator + "faces";

}
