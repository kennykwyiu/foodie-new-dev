package com.kenny.service;

import com.kenny.pojo.Carousel;
import com.kenny.pojo.Category;
import com.kenny.vo.CategoryVO;
import io.swagger.models.auth.In;

import java.util.List;

public interface CategoryService {
    public List<Category> queryAllRootLevelCat();

    public List<CategoryVO> getSubCatList(Integer rootCatId);
}
