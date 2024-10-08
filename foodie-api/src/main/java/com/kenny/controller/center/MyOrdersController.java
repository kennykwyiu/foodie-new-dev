package com.kenny.controller.center;

import com.kenny.controller.BaseController;
import com.kenny.pojo.Orders;
import com.kenny.service.MyOrdersService;
import com.kenny.utils.JsonResult;
import com.kenny.utils.PagedGridResult;
import com.kenny.vo.OrderStatusCountsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "My Orders in User Center",
        tags = {"User Center My Orders Related APIs"})
@RestController
@RequestMapping("myorders")
public class MyOrdersController extends BaseController {

    @Autowired
    private MyOrdersService myOrdersService;

    @ApiOperation(value = "Get Order Status Counts Overview", notes = "Get Order Status Counts Overview", httpMethod = "POST")
    @PostMapping("/statusCounts")
    public JsonResult statusCounts(
            @ApiParam(name = "userId", value = "User ID", required = true)
            @RequestParam String userId) {

        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg(null);
        }

        // Retrieve order status counts for the given user ID
        OrderStatusCountsVO result = myOrdersService.getOrderStatusCounts(userId);

        // Return the order status counts in the response
        return JsonResult.ok(result);
    }

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

    @ApiOperation(value="Confirm Receipt by User", notes="Confirm Receipt by User", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public JsonResult confirmReceive(
            @ApiParam(name = "orderId", value = "Order ID", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "User ID", required = true)
            @RequestParam String userId) throws Exception {

        JsonResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        boolean res = myOrdersService.updateReceiveOrderStatus(orderId);
        if (!res) {
            return JsonResult.errorMsg("Failed to confirm order receipt!");
        }

        return JsonResult.ok();
    }

    @ApiOperation(value="User Delete Order", notes="User Delete Order", httpMethod = "POST")
    @PostMapping("/delete")
    public JsonResult delete(
            @ApiParam(name = "orderId", value = "Order ID", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "User ID", required = true)
            @RequestParam String userId) throws Exception {

        JsonResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        boolean res = myOrdersService.deleteOrder(userId, orderId);
        if (!res) {
            return JsonResult.errorMsg("Failed to delete the order!");
        }

        return JsonResult.ok();
    }

    public JsonResult checkUserOrder(String userId, String orderId) {
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return JsonResult.errorMsg("Order does not exist!");
        }
        return JsonResult.ok(order);
    }

    @ApiOperation(value = "Query Order Trends", notes = "Query Order Trends", httpMethod = "POST")
    @PostMapping("/trend")
    public JsonResult trend(
            @ApiParam(name = "userId", value = "User ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "The page number for the next page to query", required = false)
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
            pageSize = COMMON_PAGE_SIZE; // Assuming COMMON_PAGE_SIZE is a constant defined elsewhere
        }

        // Retrieve order trends for the specified user, page, and page size
        PagedGridResult grid = myOrdersService.getOrdersTrend(userId, page, pageSize);

        return JsonResult.ok(grid);
    }

}
