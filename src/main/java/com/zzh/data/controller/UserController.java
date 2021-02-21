package com.zzh.data.controller;


import com.zzh.data.entity.User;
import com.zzh.data.enums.cacheKey;
import com.zzh.data.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 张子行
 * @since 2021-02-17
 */
@Slf4j
@CacheConfig(cacheManager = "redisCacheManager")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    private static final int DELAYEDTIME = 1000;
    /**
     * 有任务进来，放入corePoolSize线程,当corePoolSize线程满了，
     * 放入LinkedBlockingDeque队列，当队列也满了且corePoolSize+LinkedBlockingDeque<maximumPoolSize
     * 开辟新线程执行队列里面的任务，当corePoolSize+LinkedBlockingDeque>maximumPoolSize
     * 执行我们的拒绝策略
     */
    private static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
            5,
            10,
            5,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(3),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    /**
     * 读取线程
     */
    @Cacheable(value = "testCache", key = "add")
    @GetMapping("/get")
    public User creatPessimismOrder(@RequestParam("id") int id) {
        User user = (User) userService.getCacheBykey(id);
        if (user != null) {
            return user;
        }
        user = userService.getById(id);
        userService.putCache(cacheKey.STOCKCACHEKEY.getCacheKey() + id, user);
        return user;
    }

    /**
     * 更新线程（先更新数据库，在删缓存）
     */
    @RequestMapping("/update")
    public String update(@RequestParam("id") int id) {
        try {
            userService.creatPessimismOrder(id);
            //userService.creatOptimisticOrder(id);
            userService.delCacheBykey(id);
        } catch (Exception e) {
            log.info(e.getMessage());
            return "操作失败";
        }
        return "操作成功";
    }

    /**
     * 更新线程（先更新数据库，在删缓存）优化
     */
    @RequestMapping("/update3")
    public String update3(@RequestParam("id") int id) {
        try {
            userService.creatPessimismOrder(id);
            //userService.creatOptimisticOrder(id);
            userService.delCacheBykey(id);
            //延时删除缓存
            //poolExecutor.execute(new delayDelCache(id));
            // TODO 通知消息队列，删除缓存


        } catch (Exception e) {
            log.info(e.getMessage());
            return "操作失败";
        }
        return "操作成功";
    }

    /**
     * 更新线程（先删缓存，再更新数据库）
     */
    @RequestMapping("/update2")
    public String update2(@RequestParam("id") int id) {
        try {
            userService.delCacheBykey(id);
            userService.creatPessimismOrder(id);
            //userService.creatOptimisticOrder(id);
        } catch (Exception e) {
            log.info(e.getMessage());
            return "操作失败";
        }
        return "操作成功";
    }

    /**
     * 更新线程（先删缓存，再更新数据库）优化,延时双删
     */
    @RequestMapping("/update2")
    public String update4(@RequestParam("id") int id) {
        try {
            //删除缓存
            userService.delCacheBykey(id);
            //悲观锁更新数据库（查库存数据，更新库存，创建订单）
            userService.creatPessimismOrder(id);
            //userService.creatOptimisticOrder(id);
            //延时删除缓存
            poolExecutor.execute(new delayDelCache(id));
        } catch (Exception e) {
            log.info(e.getMessage());
            return "操作失败";
        }
        return "操作成功";
    }

    /**
     * 延时删除缓存Runnable接口
     */
    private class delayDelCache implements Runnable {
        private int id;

        public delayDelCache(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                log.info("延时双删准备开始---------------");
                Thread.sleep(DELAYEDTIME);
                userService.delCacheBykey(id);
                log.info("延时双删完成---------------");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

