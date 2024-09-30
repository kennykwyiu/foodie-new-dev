package com.kenny.service;

import com.kenny.pojo.Orders;
import com.kenny.utils.PagedGridResult;
import com.kenny.vo.OrderStatusCountsVO;

public interface MyOrdersService {
    public PagedGridResult queryMyOrders(String userId,
                                         Integer orderStatus,
                                         Integer page,
                                         Integer pageSize);

    void updateDeliverOrderStatus(String orderId);

    Orders queryMyOrder(String userId, String orderId);

    boolean updateReceiveOrderStatus(String orderId);

    boolean deleteOrder(String userId, String orderId);

    public OrderStatusCountsVO getOrderStatusCounts(String userId);

    public PagedGridResult getOrdersTrend(String userId,
                                         Integer page,
                                         Integer pageSize);
}

