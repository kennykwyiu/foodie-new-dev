package com.kenny.controller.center;

import com.kenny.bo.OrderItemsCommentBO;
import com.kenny.controller.BaseController;
import com.kenny.enums.YesOrNo;
import com.kenny.pojo.OrderItems;
import com.kenny.pojo.Orders;
import com.kenny.service.MyCommentsService;
import com.kenny.utils.JsonResult;
import com.kenny.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "User Center Comment Module", tags = {"Endpoints related to the User Center Comment Module"})
@RestController
@RequestMapping("mycomments")
public class MyCommentsController extends BaseController {

    @Autowired
    private MyCommentsService myCommentsService;

    @ApiOperation(value = "Query Order List", notes = "Query Order List", httpMethod = "POST")
    @PostMapping("/pending")
    public JsonResult pending(
            @ApiParam(name = "userId", value = "User ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "Order ID", required = true)
            @RequestParam String orderId) {

        // Check if the user is associated with the order
        JsonResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        // Check if the order has already been reviewed
        Orders myOrder = (Orders) checkResult.getData();
        if (myOrder.getIsComment().equals(YesOrNo.YES.type)) {
            return JsonResult.errorMsg("This order has already been reviewed");
        }

        // Retrieve a list of order items pending review
        List<OrderItems> list = myCommentsService.queryPendingComments(orderId);

        return JsonResult.ok(list);
    }

    @ApiOperation(value = "Save Comment List", notes = "Save Comment List", httpMethod = "POST")
    @PostMapping("/saveList")
    public JsonResult saveList(
            @ApiParam(name = "userId", value = "User ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "Order ID", required = true)
            @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> commentList) {

        System.out.println(commentList);

        // Check if the user is associated with the order
        JsonResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        // Check if the comment list is not empty
        if (commentList == null || commentList.isEmpty() || commentList.size() == 0) {
            return JsonResult.errorMsg("Comment content cannot be empty!");
        }

        myCommentsService.saveComments(orderId, userId, commentList);
        return JsonResult.ok();
    }

    @ApiOperation(value = "Query My Reviews", notes = "Query My Reviews", httpMethod = "POST")
    @PostMapping("/query")
    public JsonResult query(
            @ApiParam(name = "userId", value = "User ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "Page number for next page query", required = false)
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

        PagedGridResult grid = myCommentsService.queryMyComments(userId,
                page,
                pageSize);

        return JsonResult.ok(grid);
    }



}
