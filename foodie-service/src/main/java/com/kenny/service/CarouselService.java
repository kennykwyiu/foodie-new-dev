package com.kenny.service;

import com.kenny.pojo.Carousel;

import java.util.List;

public interface CarouselService {
    public List<Carousel> queryAll(Integer isShow);
}
