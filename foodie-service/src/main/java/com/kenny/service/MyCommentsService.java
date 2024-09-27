package com.kenny.service;

import com.kenny.bo.OrderItemsCommentBO;
import com.kenny.pojo.OrderItems;
import com.kenny.utils.PagedGridResult;

import java.util.List;

public interface MyCommentsService {
    public List<OrderItems> queryPendingComments(String orderId);

    PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);

    void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList);
}

