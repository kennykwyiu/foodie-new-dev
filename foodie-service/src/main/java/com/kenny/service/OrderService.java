package com.kenny.service;

import com.kenny.bo.SubmitOrderBO;
import com.kenny.pojo.Carousel;

import java.util.List;

public interface OrderService {
    public String createOrder(SubmitOrderBO submitOrderBO);
}
