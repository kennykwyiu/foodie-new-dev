package com.kenny.service.impl;

import com.kenny.bo.SubmitOrderBO;
import com.kenny.mapper.OrdersMapper;
import com.kenny.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createOrder(SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();

        // default 0
        Integer postAmount = 0;

        // 1. Save new order data

        // 2. Loop through itemSpecIds to save order item information table

        // TODO After integrating Redis, retrieve the purchased quantity from the Redis shopping cart

        // 2.1 Query specific information based on spec id, mainly to get the price

        // 2.2 Get product information and product images based on product id

        // 2.3 Loop to save sub-order data to the database

        // 2.4 After the user submits the order, deduct the inventory in the spec table

        // 3. Save the order status table

        // 4. Build merchant order to send to the payment center

        // 5. Build custom order VO

    }
}
