package com.kenny.service;

import com.kenny.utils.PagedGridResult;

public interface ItemsEsService {
    public PagedGridResult searchItems(String keywords,
                                       String sort,
                                       Integer page,
                                       Integer pageSize);
}
