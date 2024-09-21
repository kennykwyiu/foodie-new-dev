package com.kenny.service;

import com.kenny.bo.SubmitOrderBO;

public interface OrderService {
    public String createOrder(SubmitOrderBO submitOrderBO);
    public void updateOrderStatus(String orderId, Integer orderStatus);
}
