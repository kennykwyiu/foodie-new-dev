package com.kenny.controller;

import org.springframework.stereotype.Controller;

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
}
