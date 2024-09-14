package com.kenny.mapper;

import com.kenny.my.mapper.MyMapper;
import com.kenny.pojo.Category;
import com.kenny.vo.CategoryVO;

import java.util.List;

public interface CategoryMapperCustom {
    public List<CategoryVO> getSubCatList(Integer rootCatId);
}