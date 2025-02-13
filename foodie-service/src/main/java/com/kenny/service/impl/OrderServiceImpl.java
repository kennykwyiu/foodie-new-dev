package com.kenny.service.impl;

import com.kenny.bo.ShopcartBO;
import com.kenny.bo.SubmitOrderBO;
import com.kenny.enums.OrderStatusEnum;
import com.kenny.enums.YesOrNo;
import com.kenny.mapper.OrderItemsMapper;
import com.kenny.mapper.OrderStatusMapper;
import com.kenny.mapper.OrdersMapper;
import com.kenny.pojo.*;
import com.kenny.service.AddressService;
import com.kenny.service.ItemService;
import com.kenny.service.OrderService;
import com.kenny.utils.DateUtil;
import com.kenny.vo.MerchantOrdersVO;
import com.kenny.vo.OrderVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private AddressService addressService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private Sid sid;
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(List<ShopcartBO> shopcartList, SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();

        // default 0
        Integer postAmount = 0;
        String orderId = sid.nextShort();
        UserAddress address = addressService.queryUserAddress(userId, addressId);

        // 1. Save new order data
        Orders newOrder = new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);
        newOrder.setReceiverName(address.getReceiver());
        newOrder.setReceiverMobile(address.getMobile());
        newOrder.setReceiverAddress(address.getProvince() + " "
                + address.getCity() + " "
                + address.getDistrict() + " "
                + address.getDetail());
//        newOrder.setTotalAmount();
//        newOrder.setRealPayAmount();
        newOrder.setPostAmount(postAmount);

        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);

        newOrder.setIsComment(YesOrNo.NO.type);
        newOrder.setIsDelete(YesOrNo.NO.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());

        // 2. Loop through itemSpecIds to save order item information table
        String[] itemSpecIdArr = itemSpecIds.split(",");
        Integer totalAmount = 0;
        Integer realPayAmount = 0;

        List<ShopcartBO> toBeRemovedShopcartList = new ArrayList<>();
        for (String itemSpecId : itemSpecIdArr) {
            ShopcartBO cartItem = getBuyCountsFromShopcart(shopcartList, itemSpecId);
            // TODO After integrating Redis, retrieve the purchased quantity from the Redis shopping cart
            int buyCounts = cartItem.getBuyCounts();
            toBeRemovedShopcartList.add(cartItem);

            // 2.1 Query specific information based on spec id, mainly to get the price
            ItemsSpec itemsSpec = itemService.queryItemSpecById(itemSpecId);
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;

            // 2.2 Get product information and product images based on product id
            String itemId = itemsSpec.getItemId();
            Items item = itemService.queryItemById(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);

            // 2.3 Loop to save sub-order data to the database
            String subOrderId = sid.nextShort();
            OrderItems subOrderItem = new OrderItems();
            subOrderItem.setId(subOrderId);
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemId(itemId);
            subOrderItem.setItemName(item.getItemName());
            subOrderItem.setItemImg(imgUrl);
            subOrderItem.setBuyCounts(buyCounts);
            subOrderItem.setItemSpecId(itemSpecId);
            subOrderItem.setItemSpecName(itemsSpec.getName());
            subOrderItem.setPrice(itemsSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItem);

            // 2.4 After the user submits the order, deduct the inventory in the spec table
            itemService.decreaseItemSpecStock(itemSpecId, buyCounts);

        }
        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrder);

        // 3. Save the order status table
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);

        // 4. Build merchant order to send to the payment center
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount + postAmount);
        merchantOrdersVO.setPayMethod(payMethod);

        // 5. Build custom order VO
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);
        orderVO.setToBeRemovedShopcartList(toBeRemovedShopcartList);

        return orderVO;
    }

    private ShopcartBO getBuyCountsFromShopcart(List<ShopcartBO> shopcartList, String specId) {
        for (ShopcartBO shopcartBO : shopcartList) {
            String tmpSpecId = shopcartBO.getSpecId();
            if (tmpSpecId.equals(specId)) {
                return shopcartBO;
            }
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId(orderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());

        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void closeOrder() {
        // Query all unpaid orders, check if the time has exceeded (1 day), and close the transaction if it has
        OrderStatus queryOrder = new OrderStatus();
        queryOrder.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> list = orderStatusMapper.select(queryOrder);

        for (OrderStatus os : list) {
            // Get the order creation time
            Date createdTime = os.getCreatedTime();
            // Compare with the current time
            int days = DateUtil.daysBetween(createdTime, new Date());
            if (days >= 1) {
                // If it has been more than 1 day, close the order
                doCloseOrder(os.getOrderId());
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    void doCloseOrder(String orderId) {
        OrderStatus close = new OrderStatus();
        close.setOrderId(orderId);
        close.setOrderStatus(OrderStatusEnum.CLOSE.type);
        close.setCloseTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(close);
    }
}
