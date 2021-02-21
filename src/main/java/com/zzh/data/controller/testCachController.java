package com.zzh.data.controller;


import com.zzh.data.entity.R;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CacheConfig(cacheManager = "redisCacheManager")
@RestController
@RequestMapping("/testCache")
public class testCachController {
    @Cacheable(value = "testCache")
    @GetMapping("/add")
    public R add(){
        return R.ok().put("add","add");
    }
    @RequestMapping("/delete")
    public R delete(){
        return R.ok().put("delete","delete");
    }
    @RequestMapping("/alter")
    public R alter(){
        return R.ok().put("alter","alter");
    }
    @RequestMapping("/select")
    public R select(){
        return R.ok().put("select","select");
    }
}
