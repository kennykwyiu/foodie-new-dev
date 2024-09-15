package com.kenny.service;

import com.kenny.pojo.Items;
import com.kenny.pojo.ItemsImg;
import com.kenny.pojo.ItemsParam;
import com.kenny.pojo.ItemsSpec;

import java.util.List;

public interface ItemService {
    public Items queryItemById(String itemId);
    public List<ItemsImg> queryItemImgList(String itemId);
    public List<ItemsSpec> queryItemSpecList(String itemId);
    public ItemsParam queryItemParam(String itemId);

}
