package com.kenny.service;

import com.kenny.pojo.Category;
import com.kenny.pojo.Orders;
import com.kenny.utils.PagedGridResult;
import com.kenny.vo.CategoryVO;
import com.kenny.vo.NewItemsVO;

import java.util.List;

public interface MyOrdersService {
    public PagedGridResult queryMyOrders(String userId,
                                         Integer orderStatus,
                                         Integer page,
                                         Integer pageSize);

    void updateDeliverOrderStatus(String orderId);

    Orders queryMyOrder(String userId, String orderId);

    boolean updateReceiveOrderStatus(String orderId);

    boolean deleteOrder(String userId, String orderId);
}

