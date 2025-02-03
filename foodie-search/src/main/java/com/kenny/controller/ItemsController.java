package com.kenny.controller;

import com.kenny.service.ItemsEsService;
import com.kenny.utils.JsonResult;
import com.kenny.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("items")
public class ItemsController {

    @Autowired
    private ItemsEsService itemsEsService;
    @GetMapping("/hello")
    public Object hello() {
        return "</br></br></br><H1>Hello Elasticsearch~~!</H1>";
    }

    @GetMapping("/es/search")
    public JsonResult search(
            String keywords,
            String sort,
            Integer page,
            Integer pageSize) {
        if (StringUtils.isBlank(keywords)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 20;
        }

        page --;

        PagedGridResult grid = itemsEsService.searchItems(keywords,
                sort,
                page,
                pageSize);
        return JsonResult.ok(grid);
    }
}
