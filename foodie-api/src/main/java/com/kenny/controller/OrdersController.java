package com.kenny.controller;

import com.kenny.bo.SubmitOrderBO;
import com.kenny.enums.OrderStatusEnum;
import com.kenny.pojo.OrderStatus;
import com.kenny.service.OrderService;
import com.kenny.utils.JsonResult;
import com.kenny.vo.MerchantOrdersVO;
import com.kenny.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Api(value = "Order related", tags = {"API interfaces related to orders"})
@RequestMapping("orders")
@RestController
public class OrdersController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

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
        OrderVO orderVO = orderService.createOrder(submitOrderBO);
        String orderId = orderVO.getOrderId();

        // 2. After creating the order, remove the items that have been settled (submitted) from the shopping cart
        // TODO After integrate ing Redis, enhance the functionality to clear settled items from the shopping cart and synchronize this update to the frontend cookie
//        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);

        // 3. Send the current order to the payment center to save the order data
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);

        merchantOrdersVO.setAmount(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", "imooc");
        headers.add("password", "imooc");

        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO,headers);

        ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(paymentUrl,
                                                                                entity,
                                                                                JsonResult.class);
        JsonResult paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            return JsonResult.errorMsg("Payment center order creation failed, please contact the administrator!");
        }

        return JsonResult.ok(orderId);
    }
    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @PostMapping("getPaidOrderInfo")
    public JsonResult getPaidOrderInfo(String orderId) {

        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return JsonResult.ok(orderStatus);
    }

}
