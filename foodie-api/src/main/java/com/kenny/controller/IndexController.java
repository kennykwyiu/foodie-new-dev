package com.kenny.controller;

import com.kenny.enums.YesOrNo;
import com.kenny.pojo.Carousel;
import com.kenny.service.CarouselService;
import com.kenny.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "Home Page", tags = {"Related APIs for home page display"})
@RestController
@RequestMapping("index")
public class IndexController {
    @Autowired
    private CarouselService carouselService;

    final static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @ApiOperation(value = "Get list of home page carousel images", notes = "Get list of home page carousel images", httpMethod = "GET")
    @GetMapping("/carousel")
    public JsonResult carousel() {
        List<Carousel> result = carouselService.queryAll(YesOrNo.YES.type);
        return JsonResult.ok(result);
    }
}
