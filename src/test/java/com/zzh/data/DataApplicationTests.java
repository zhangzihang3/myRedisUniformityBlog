package com.zzh.data;

import com.zzh.data.entity.User;
import com.zzh.data.mapper.UserMapper;
import com.zzh.data.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
class DataApplicationTests {
    @Autowired
    UserService userService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserMapper userMapper;

    @Test
    public void contextLoads() {
        userService.creatOptimisticOrder(1);
    }

    @Test
    public void testRedis() {
        redisTemplate.opsForValue().set("666", "666");
    }
}
