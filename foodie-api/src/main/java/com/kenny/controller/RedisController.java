package com.kenny.controller;

import com.kenny.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("redis")
public class RedisController {
    @Autowired
    private RedisOperator redisOperator;


    @GetMapping("/set")
    public Object set(String key, String value) {
        redisOperator.set(key, value);
        return "OK";
    }

    @GetMapping("/get")
    public String get(String key) {
        return (String) redisOperator.get(key);
    }

    @GetMapping("/delete")
    public Object delete(String key) {
        redisOperator.del(key);
        return "OK";
    }
}
