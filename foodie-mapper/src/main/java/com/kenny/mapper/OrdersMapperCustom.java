package com.kenny.mapper;

import com.kenny.pojo.OrderStatus;
import com.kenny.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom {
    public List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> map);
    public int getMyOrderStatusCounts(@Param("paramsMap") Map<String, Object> map);
    public List<OrderStatus> getMyOrderTrend(@Param("paramsMap") Map<String, Object> map);
}