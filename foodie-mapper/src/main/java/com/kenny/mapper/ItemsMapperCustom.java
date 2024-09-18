package com.kenny.mapper;

import com.kenny.vo.ItemCommentVO;
import com.kenny.vo.SearchItemsVO;
import com.kenny.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {
    public List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);
    public List<SearchItemsVO> searchItems(@Param("paramsMap") Map<String, Object> map);
    public List<SearchItemsVO> searchItemsByThirdCat(@Param("paramsMap") Map<String, Object> map);
    public List<ShopcartVO> queryItemsBySpecIds(@Param("paramsList") List<String> specIdsList);
}