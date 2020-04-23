package com.limengxiang.basics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping(value = "/redis-set")
    public String set(@RequestParam("sliceKey") String key, @RequestParam("value") String value) {
        redisTemplate.opsForValue().set(key, value);
        return "OK";
    }

    @RequestMapping(value = "/redis-get")
    public String get(@RequestParam("sliceKey") String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @RequestMapping(value = "/redis-del")
    public void del(@RequestParam("sliceKey") String key) {
        redisTemplate.opsForValue().set(key, "1");
    }

    @RequestMapping("/redis-hash-set")
    public String hashSet(@RequestParam("sliceKey") String key,
                          @RequestParam("field") String field,
                          @RequestParam("value") String value) {
        redisTemplate.opsForHash().put(key, field, value);
        return "OK";
    }

    @RequestMapping("/redis-hash-get")
    public Object hashSet(@RequestParam("sliceKey") String key,
                          @RequestParam("field") String field) {
        if (field.equals("")) {
            return redisTemplate.opsForHash().entries(key);
        } else {
            return redisTemplate.opsForHash().get(key, field);
        }
    }

    @RequestMapping("/redis-list-push")
    public Long listPush(@RequestParam("sliceKey") String key, @RequestParam("elem") String elem) {
        return redisTemplate.opsForList().leftPush(key, elem);
    }

    @RequestMapping("/redis-list-pop")
    public String listPop(@RequestParam("sliceKey") String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

}
