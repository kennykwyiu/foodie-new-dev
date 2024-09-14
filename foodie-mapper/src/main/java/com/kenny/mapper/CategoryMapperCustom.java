package com.kenny.mapper;

import com.kenny.vo.CategoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom {
    public List<CategoryVO> getSubCatList(Integer rootCatId);
    public List getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);
}