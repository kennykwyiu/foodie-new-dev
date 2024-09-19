package com.kenny.controller;

import com.kenny.bo.SubmitOrderBO;
import com.kenny.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(value = "Order related", tags = {"API interfaces related to orders"})
@RequestMapping("orders")
@RestController
public class OrdersController {



    @ApiOperation(value = "User Order", notes = "User Order", httpMethod = "POST")
    @PostMapping("/create")
    public JsonResult create(@RequestBody SubmitOrderBO submitOrderBO) {

//        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
//            return JsonResult.errorMsg("Payment method not supported!");
//        }

        // 1. Create order
        // 2. After creating the order, remove the items that have been settled (submitted) from the shopping cart
        // 3. Send the current order to the payment center to save the order data


        System.out.println(submitOrderBO.toString());

        return JsonResult.ok();
    }

}
