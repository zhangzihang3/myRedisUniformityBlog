package com.zzh.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zzh.data.entity.User;
import com.zzh.data.enums.cacheKey;
import com.zzh.data.mapper.UserMapper;
import com.zzh.data.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 张子行
 * @since 2021-02-17
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void delCacheBykey(int id) {
        stringRedisTemplate.delete(cacheKey.STOCKCACHEKEY.getCacheKey() + id);
        log.info("删除缓存成功");
    }

    /**
     * 乐观锁更新，mybatisPlus也可配置更新插件
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void creatOptimisticOrder(int id) {
        //普通查询
        User user = this.getById(id);
        user.setAge(user.getAge() - 1);
        //乐观锁更新库存
        userMapper.optimisticUpdateById(user);
        //创建订单
        log.info("乐观锁创建订单");
    }

    /**
     * 悲观锁,更新
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void creatPessimismOrder(int id) {
        //悲观锁查询
        User user = userMapper.pessimismSelectUserById(id);
        user.setAge(user.getAge() - 1);
        //普通更新库存
        this.updateById(user);
        //创建订单
        log.info("悲观锁创建订单");
    }

    @Override
    public Object getCacheBykey(int id) {
        return redisTemplate.opsForValue().get(cacheKey.STOCKCACHEKEY.getCacheKey() + id);
    }

    @Override
    public void putCache(String key,Object value) {
        redisTemplate.opsForValue().set(key,value);
    }
}
