package com.kenny.service;

import com.kenny.pojo.Category;
import com.kenny.utils.PagedGridResult;
import com.kenny.vo.CategoryVO;
import com.kenny.vo.NewItemsVO;

import java.util.List;

public interface MyOrdersService {
    public PagedGridResult queryMyOrders(String userId,
                                         Integer orderStatus,
                                         Integer page,
                                         Integer pageSize);
}

