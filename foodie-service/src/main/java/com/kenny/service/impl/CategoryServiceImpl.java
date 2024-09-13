package com.kenny.service.impl;

import com.kenny.mapper.CarouselMapper;
import com.kenny.mapper.CategoryMapper;
import com.kenny.pojo.Carousel;
import com.kenny.pojo.Category;
import com.kenny.service.CarouselService;
import com.kenny.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryAllRootLevelCat() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", 1);

        List<Category> categories = categoryMapper.selectByExample(example);
        return categories;
    }
}
