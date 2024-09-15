package com.kenny.service;

import com.kenny.pojo.Items;
import com.kenny.pojo.ItemsImg;
import com.kenny.pojo.ItemsParam;
import com.kenny.pojo.ItemsSpec;
import com.kenny.vo.CommentLevelCountsVO;

import java.util.List;

public interface ItemService {
    public Items queryItemById(String itemId);
    public List<ItemsImg> queryItemImgList(String itemId);
    public List<ItemsSpec> queryItemSpecList(String itemId);
    public ItemsParam queryItemParam(String itemId);
    public CommentLevelCountsVO queryCommentCounts(String itemId);

}
