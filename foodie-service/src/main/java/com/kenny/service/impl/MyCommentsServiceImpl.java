package com.kenny.service.impl;

import com.github.pagehelper.PageHelper;
import com.kenny.bo.OrderItemsCommentBO;
import com.kenny.enums.YesOrNo;
import com.kenny.mapper.ItemsCommentsMapperCustom;
import com.kenny.mapper.OrderItemsMapper;
import com.kenny.mapper.OrderStatusMapper;
import com.kenny.mapper.OrdersMapper;
import com.kenny.pojo.OrderItems;
import com.kenny.pojo.OrderStatus;
import com.kenny.pojo.Orders;
import com.kenny.service.MyCommentsService;
import com.kenny.service.impl.center.BaseService;
import com.kenny.utils.PagedGridResult;
import com.kenny.vo.MyCommentVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyCommentsServiceImpl extends BaseService implements MyCommentsService {

    @Autowired
    public OrderItemsMapper orderItemsMapper;

    @Autowired
    public OrdersMapper ordersMapper;

    @Autowired
    public OrderStatusMapper orderStatusMapper;
    @Autowired
    public ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComments(String orderId) {
        OrderItems query = new OrderItems();
        query.setOrderId(orderId);
        return orderItemsMapper.select(query);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyComments(String userId,
                                           Integer page,
                                           Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> list = itemsCommentsMapperCustom.queryMyComments(map);

        return setterPagedGrid(list, page);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList) {

        // 1. Save comments in items_comments table
        for (OrderItemsCommentBO oic : commentList) {
            oic.setCommentId(sid.nextShort());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("commentList", commentList);
        itemsCommentsMapperCustom.saveComments(map);

        // 2. Update the isComment field in the orders table
        Orders order = new Orders();
        order.setId(orderId);
        order.setIsComment(YesOrNo.YES.type);
        ordersMapper.updateByPrimaryKeySelective(order);

        // 3. Update the commentTime field in the order_status table
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }
}
