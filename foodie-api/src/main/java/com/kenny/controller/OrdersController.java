package com.kenny.controller;

import com.kenny.bo.SubmitOrderBO;
import com.kenny.service.OrderService;
import com.kenny.utils.CookieUtils;
import com.kenny.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Api(value = "Order related", tags = {"API interfaces related to orders"})
@RequestMapping("orders")
@RestController
public class OrdersController extends BaseController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "User Order", notes = "User Order", httpMethod = "POST")
    @PostMapping("/create")
    public JsonResult create(@RequestBody SubmitOrderBO submitOrderBO,
                             HttpServletRequest request,
                             HttpServletResponse response) {

//        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
//            return JsonResult.errorMsg("Payment method not supported!");
//        }

        System.out.println(submitOrderBO.toString());
        // 1. Create order
        String orderId = orderService.createOrder(submitOrderBO);

        // 2. After creating the order, remove the items that have been settled (submitted) from the shopping cart
        // TODO After integrat ing Redis, enhance the functionality to clear settled items from the shopping cart and synchronize this update to the frontend cookie
//        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);

        // 3. Send the current order to the payment center to save the order data


        return JsonResult.ok(orderId);
    }

}
