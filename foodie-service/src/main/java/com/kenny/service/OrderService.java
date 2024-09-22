package com.kenny.service;

import com.kenny.bo.SubmitOrderBO;
import com.kenny.pojo.OrderStatus;
import com.kenny.vo.OrderVO;

public interface OrderService {
    public OrderVO createOrder(SubmitOrderBO submitOrderBO);
    public void updateOrderStatus(String orderId, Integer orderStatus);
    public OrderStatus queryOrderStatusInfo(String orderId);

    public void closeOrder();
}

