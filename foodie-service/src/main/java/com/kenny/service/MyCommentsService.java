package com.kenny.service;

import com.kenny.pojo.OrderItems;

import java.util.List;

public interface MyCommentsService {
    public List<OrderItems> queryPendingComments(String orderId);
}

