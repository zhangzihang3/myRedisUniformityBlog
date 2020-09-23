package com.zzh.data;

import com.zzh.data.entity.Testdata;
import com.zzh.data.service.TestdataService;
import com.zzh.data.service.impl.TestdataServiceImpl;
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
    TestdataServiceImpl testdataService;
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
        System.out.println(testdataService.getById(1));
        System.out.println("1");
    }

    @Test
    public void testRedis() {
        redisTemplate.opsForValue().set("666", "666");
    }
}
