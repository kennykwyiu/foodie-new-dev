package com.kenny.controller;

import com.kenny.pojo.Orders;
import com.kenny.pojo.Users;
import com.kenny.service.MyOrdersService;
import com.kenny.utils.JsonResult;
import com.kenny.utils.RedisOperator;
import com.kenny.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class BaseController {

    @Autowired
    private RedisOperator redisOperator;

    // Payment center's invocation URL
//    String paymentUrl = "http://api.z.mukewang.com/foodie-payment/payment/createMerchantOrder"; // Production
    String paymentUrl = "http://localhost:8088/foodie-payment/payment/createMerchantOrder"; // Production

    // WeChat payment success -> Payment center -> Daily Gourmet Platform
    //                                  |-> Callback notification URL
//    String payReturnUrl = "http://api.z.mukewang.com/foodie-dev-api/orders/notifyMerchantOrderPaid";
//    String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";
    String payReturnUrl = "http://192.168.74.128:8088/foodie-api/orders/notifyMerchantOrderPaid";

    public static final String FOODIE_SHOPCART = "shopcart";
    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;
    public static final String REDIS_USER_TOKEN = "redis_user_token";

    // Location for user avatar uploads
//    public static final String IMAGE_USER_FACE_LOCATION = File.separator + "workspaces" +
//            File.separator + "images" +
//            File.separator + "foodie" +
//            File.separator + "faces";
    //public static final String IMAGE_USER_FACE_LOCATION = "C:\Users\yaowi\Desktop\java\images\foodie\faces";

//    public static final String IMAGE_USER_FACE_LOCATION = "C:" +
//                                                        File.separator + "Users" + File.separator + "yaowi" +
//                                                        File.separator + "Desktop" + File.separator + "java" +
//                                                        File.separator + "images" + File.separator + "foodie" +
//                                                        File.separator + "faces";

    @Autowired
    public MyOrdersService myOrdersService;
    public JsonResult checkUserOrder(String userId, String orderId) {
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return JsonResult.errorMsg("Order does not exist!");
        }
        return JsonResult.ok(order);
    }

    public UserVO convertUserVo(Users users) {
        // Implementing user session using Redis
        String uniqueToken = UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN + ":" + users.getId(),
                uniqueToken);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(users, userVO);
        userVO.setUserUniqueToken(uniqueToken);
        return userVO;
    }

}
