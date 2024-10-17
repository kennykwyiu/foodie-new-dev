package com.kenny.controller;

import com.kenny.bo.ShopcartBO;
import com.kenny.utils.JsonResult;
import com.kenny.utils.JsonUtils;
import com.kenny.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "Shopping Cart Interface Controller", tags = {"APIs related to the shopping cart interface"})
@RequestMapping("shopcart")
@RestController
public class ShopCartController extends BaseController {

    @Autowired
    private RedisOperator redisOperator;

    final static Logger logger = LoggerFactory.getLogger(ShopCartController.class);

    @ApiOperation(value = "Add Product to Shopping Cart", notes = "Add Product to Shopping Cart", httpMethod = "POST")
    @PostMapping("/add")
    public JsonResult add(@RequestParam String userId,
                          @RequestBody ShopcartBO shopcartBO,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("");
        }

        System.out.println(shopcartBO);

        // TODO When a frontend user logs in and adds items to the shopping cart, the shopping cart will be synchronized to the Redis cache on the backend simultaneously.
        // It is necessary to check if the current cart already contains the item. If it does, then increment the purchase quantity.

        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        List<ShopcartBO> shopcartList = null;
        if (StringUtils.isNoneBlank(shopcartJson)) {
            shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);

            boolean isHaving = false;
            for (ShopcartBO sc : shopcartList) {
                String tmpSpecId = sc.getSpecId();
                if (tmpSpecId.equals(shopcartBO.getSpecId())) {
                    sc.setBuyCounts(sc.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if (!isHaving) {
                shopcartList.add(shopcartBO);
            }
        } else {
            shopcartList = new ArrayList<>();
            shopcartList.add(shopcartBO);
        }

        redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartList));

        return JsonResult.ok();
    }

    @ApiOperation(value = "Remove Item from Shopping Cart", notes = "Remove Item from Shopping Cart", httpMethod = "POST")
    @PostMapping("/del")
    public JsonResult del(@RequestParam String userId,
                          @RequestParam String itemSpecId,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return JsonResult.errorMsg("Parameter cannot be empty");
        }

        // TODO When a user deletes shopping cart data on the page, if the user is logged in at this time,
        //  it is necessary to synchronize the deletion of the items in the backend shopping cart.

        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        if (StringUtils.isNotBlank(shopcartJson)) {
            List<ShopcartBO> shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            for (ShopcartBO sc : shopcartList) {
                String tmpSpecId = sc.getSpecId();
                if (tmpSpecId.equals(itemSpecId)) {
                    shopcartList.remove(sc);
                    break;
                }
            }
            redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartList));
        }

        return JsonResult.ok();
    }

}
