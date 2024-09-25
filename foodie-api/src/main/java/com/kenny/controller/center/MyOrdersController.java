package com.kenny.controller.center;

import com.kenny.controller.BaseController;
import com.kenny.pojo.Orders;
import com.kenny.pojo.Users;
import com.kenny.service.MyOrdersService;
import com.kenny.service.center.CenterUserService;
import com.kenny.utils.JsonResult;
import com.kenny.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "My Orders in User Center",
        tags = {"User Center My Orders Related APIs"})
@RestController
@RequestMapping("myorders")
public class MyOrdersController extends BaseController {

    @Autowired
    private MyOrdersService myOrdersService;

    @PostMapping("/query")
    public JsonResult query(
            @ApiParam(name = "userId", value = "User ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderStatus", value = "Order Status", required = false)
            @RequestParam Integer orderStatus,
            @ApiParam(name = "page", value = "Page number for the next page to query", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "Number of items to display per page", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = myOrdersService.queryMyOrders(userId,
                orderStatus,
                page,
                pageSize);
        return JsonResult.ok(grid);
    }

    // Since there is no backend for merchant delivery, this endpoint is used for simulation purposes only
    @ApiOperation(value="Merchant Delivery", notes="Merchant Delivery", httpMethod = "GET")
    @GetMapping("/deliver")
    public JsonResult deliver(
            @ApiParam(name = "orderId", value = "Order ID", required = true)
            @RequestParam String orderId) throws Exception {

        if (StringUtils.isBlank(orderId)) {
            return JsonResult.errorMsg("Order ID cannot be empty");
        }
        myOrdersService.updateDeliverOrderStatus(orderId);
        return JsonResult.ok();
    }

    public JsonResult checkUserOrder(String userId, String orderId) {
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return JsonResult.errorMsg("Order does not exist!");
        }
        return JsonResult.ok(order);
    }

}
