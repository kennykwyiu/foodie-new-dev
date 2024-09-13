package com.kenny.service;

import com.kenny.pojo.Carousel;
import com.kenny.pojo.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> queryAllRootLevelCat();
}
