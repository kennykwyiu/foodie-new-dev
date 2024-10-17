package com.kenny.service;

import com.kenny.bo.ShopcartBO;
import com.kenny.bo.SubmitOrderBO;
import com.kenny.pojo.OrderStatus;
import com.kenny.vo.OrderVO;

import java.util.List;

public interface OrderService {
    public OrderVO createOrder(List<ShopcartBO> shopcartList, SubmitOrderBO submitOrderBO);
    public void updateOrderStatus(String orderId, Integer orderStatus);
    public OrderStatus queryOrderStatusInfo(String orderId);

    public void closeOrder();
}

