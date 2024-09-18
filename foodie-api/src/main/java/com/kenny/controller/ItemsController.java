package com.kenny.controller;

import com.kenny.pojo.Items;
import com.kenny.pojo.ItemsImg;
import com.kenny.pojo.ItemsParam;
import com.kenny.pojo.ItemsSpec;
import com.kenny.service.ItemService;
import com.kenny.utils.JsonResult;
import com.kenny.utils.PagedGridResult;
import com.kenny.vo.CommentLevelCountsVO;
import com.kenny.vo.ItemInfoVO;
import com.kenny.vo.ShopcartVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Product API", tags = {"Related APIs for displaying product information"})
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController {
    @Autowired
    private ItemService itemService;

    final static Logger logger = LoggerFactory.getLogger(ItemsController.class);

    @ApiOperation(value = "Retrieve Product Details", notes = "Retrieve Product Details", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public JsonResult info(
            @ApiParam(name = "itemId", value = "product id", required = true)
            @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemSpecList(itemsSpecList);
        itemInfoVO.setItemParams(itemsParam);

        return JsonResult.ok(itemInfoVO);
    }

    @ApiOperation(value = "Retrieve Product Rating Level", notes = "Retrieve Product Rating Level", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public JsonResult commentLevel(
            @ApiParam(name = "itemId", value = "product id", required = true)
            @RequestParam String itemId) {

        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);

        return JsonResult.ok(countsVO);
    }

    @ApiOperation(value = "Retrieve Product Reviews", notes = "Retrieve Product Reviews", httpMethod = "GET")
    @GetMapping("/comments")
    public JsonResult comments(
            @ApiParam(name = "itemId", value = "Product ID", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "Rating level", required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "Next page to query", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "Number of items displayed per page", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = itemService.queryPagedComments(itemId,
                level,
                page,
                pageSize);

        return JsonResult.ok(grid);
    }

    @ApiOperation(value = "Search Product List", notes = "Search Product List", httpMethod = "GET")
    @GetMapping("/search")
    public JsonResult search(
            @ApiParam(name = "keywords", value = "Keywords", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "Sorting", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "Next page to query", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "Number of items displayed per page", required = false)
            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(keywords)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedGridResult grid = itemService.searchItems(keywords, sort, page, pageSize);
        return JsonResult.ok(grid);
    }

    @ApiOperation(value = "Search Product List by Category ID", notes = "Search Product List by Category ID", httpMethod = "GET")
    @GetMapping("/catItems")
    public JsonResult catItems(
            @ApiParam(name = "catId", value = "Third-level category ID", required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "Sorting", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "Next page to query", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "Number of items displayed per page", required = false)
            @RequestParam Integer pageSize) {

        if (catId == null) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedGridResult grid = itemService.searchItemsByThirdCat(catId, sort, page, pageSize);
        return JsonResult.ok(grid);
    }

    @ApiOperation(value = "Find the Latest Product Data by Product Specification IDs", notes = "Find the Latest Product Data by Product Specification IDs", httpMethod = "GET")
    @GetMapping("/refresh")
    public JsonResult refresh(@ApiParam(name = "itemSpecIds", value = "Concatenated specification IDs", required = true, example = "1001,1003,1005")
                              @RequestParam String itemSpecIds) {
        if (StringUtils.isBlank(itemSpecIds)) {
            return JsonResult.errorMsg("");
        }

        List<ShopcartVO> list = itemService.queryItemsBySpecIds(itemSpecIds);

        return JsonResult.ok(list);
    }
}
