package com.kenny.service;

import com.kenny.pojo.Items;
import com.kenny.pojo.ItemsImg;
import com.kenny.pojo.ItemsParam;
import com.kenny.pojo.ItemsSpec;
import com.kenny.utils.PagedGridResult;
import com.kenny.vo.CommentLevelCountsVO;
import com.kenny.vo.ShopcartVO;

import java.util.List;

public interface ItemService {
    public Items queryItemById(String itemId);
    public List<ItemsImg> queryItemImgList(String itemId);
    public List<ItemsSpec> queryItemSpecList(String itemId);
    public ItemsParam queryItemParam(String itemId);
    public CommentLevelCountsVO queryCommentCounts(String itemId);
    public PagedGridResult queryPagedComments(String itemId,
                                              Integer level,
                                              Integer page,
                                              Integer pageSize);
    public PagedGridResult searchItems(String keywords,
                                              String sort,
                                              Integer page,
                                              Integer pageSize);
    public PagedGridResult searchItemsByThirdCat(Integer catId,
                                              String sort,
                                              Integer page,
                                              Integer pageSize);
    public List<ShopcartVO> queryItemsBySpecIds(String specIds);
    public ItemsSpec queryItemSpecById(String specId);

    public String queryItemMainImgById(String itemId);

    public void decreaseItemSpecStock(String itemSpecId, int buyCounts);
}
