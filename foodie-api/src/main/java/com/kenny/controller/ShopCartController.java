package com.kenny.controller;

import com.kenny.bo.ShopcartBO;
import com.kenny.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "Shopping Cart Interface Controller", tags = {"APIs related to the shopping cart interface"})
@RequestMapping("shopcart")
@RestController
public class ShopCartController {

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

        // TODO When a front-end user is logged in and adds a product to the shopping cart,
        //  synchronize the shopping cart to the Redis cache on the backend.

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

        return JsonResult.ok();
    }

}
