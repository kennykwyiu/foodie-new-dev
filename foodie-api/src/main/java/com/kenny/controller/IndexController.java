package com.kenny.controller;

import com.kenny.enums.YesOrNo;
import com.kenny.pojo.Carousel;
import com.kenny.pojo.Category;
import com.kenny.service.CarouselService;
import com.kenny.service.CategoryService;
import com.kenny.utils.JsonResult;
import com.kenny.vo.CategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "Home Page", tags = {"Related APIs for home page display"})
@RestController
@RequestMapping("index")
public class IndexController {
    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    final static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @ApiOperation(value = "Get list of home page carousel images", notes = "Get list of home page carousel images", httpMethod = "GET")
    @GetMapping("/carousel")
    public JsonResult carousel() {
        List<Carousel> result = carouselService.queryAll(YesOrNo.YES.type);
        return JsonResult.ok(result);
    }

    /**
     * Home page category display requirements:
     * 1. When the homepage is first loaded, query the main categories and render them on the homepage.
     * 2. If the mouse hovers over a main category, load the content of its subcategories. If the subcategories are already loaded, there is no need to load them again (lazy loading).
     */
    @ApiOperation(value = "Get Product Categories (Primary Categories)", notes = "Get Product Categories (Primary Categories)", httpMethod = "GET")
    @GetMapping("/cats")
    public JsonResult cats() {
        List<Category> categories = categoryService.queryAllRootLevelCat();
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
        return  JsonResult.ok(subCatList);
    }

}
