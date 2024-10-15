package com.kenny.controller;

import com.kenny.enums.YesOrNo;
import com.kenny.pojo.Carousel;
import com.kenny.pojo.Category;
import com.kenny.service.CarouselService;
import com.kenny.service.CategoryService;
import com.kenny.utils.JsonResult;
import com.kenny.utils.JsonUtils;
import com.kenny.utils.RedisOperator;
import com.kenny.vo.CategoryVO;
import com.kenny.vo.NewItemsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "Home Page", tags = {"Related APIs for home page display"})
@RestController
@RequestMapping("index")
public class IndexController {
    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisOperator redisOperator;

    final static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @ApiOperation(value = "Get list of home page carousel images", notes = "Get list of home page carousel images", httpMethod = "GET")
    @GetMapping("/carousel")
    public JsonResult carousel() {
        List<Carousel> result = new ArrayList<>();
        String carouselStr = redisOperator.get("carousel");

        if (StringUtils.isBlank(carouselStr)) {
            result = carouselService.queryAll(YesOrNo.YES.type);
            redisOperator.set("carousel", JsonUtils.objectToJson(result));
        } else {
            result = JsonUtils.jsonToList(carouselStr, Carousel.class);
        }
        return JsonResult.ok(result);
    }

    /**
     * 1. Once the advertisement (carousel image) is updated in the backend operation system, the cache can be cleared and then reset.
     * 2. Scheduled reset, for example, reset at 3 AM every day.
     * 3. Each carousel image could represent an advertisement, and each advertisement may have an expiration time. When expired, reset.
     */

    /**
     * Home page category display requirements:
     * 1. When the homepage is first loaded, query the main categories and render them on the homepage.
     * 2. If the mouse hovers over a main category, load the content of its subcategories. If the subcategories are already loaded, there is no need to load them again (lazy loading).
     */
    @ApiOperation(value = "Get Product Categories (Primary Categories)", notes = "Get Product Categories (Primary Categories)", httpMethod = "GET")
    @GetMapping("/cats")
    public JsonResult cats() {
        List<Category> categories = new ArrayList<>();
        String catsStr = redisOperator.get("cats");
        if (StringUtils.isBlank(catsStr)) {
            categories = categoryService.queryAllRootLevelCat();
            redisOperator.set("cats", JsonUtils.objectToJson(categories));
        } else {
            categories = JsonUtils.jsonToList(catsStr, Category.class);
        }
        return JsonResult.ok(categories);
    }

    @ApiOperation(value = "Get Subcategories of Products", notes = "Get Subcategories of Products", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public JsonResult subCat(
            @ApiParam(name = "rootCatId", value = "Primary category ID", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return JsonResult.errorMsg("Subcategory does not exist");
        }

        List<CategoryVO> subCatList = categoryService.getSubCatList(rootCatId);
        return JsonResult.ok(subCatList);
    }

    @ApiOperation(value = "Retrieve the Latest 6 Items for Each Primary Category", notes = "Retrieve the Latest 6 Items for Each Primary Category", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public JsonResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "Primary category ID", required = true)
            @PathVariable Integer rootCatId) {

        if (rootCatId == null) {
            return JsonResult.errorMsg("Category does not exist");
        }

        List<NewItemsVO> list = categoryService.getSixNewItemsLazy(rootCatId);
        return JsonResult.ok(list);
    }

}
