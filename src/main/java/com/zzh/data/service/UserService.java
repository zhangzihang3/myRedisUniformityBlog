package com.zzh.data.service;

import com.zzh.data.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 张子行
 * @since 2021-02-17
 */
public interface UserService extends IService<User> {
     void delCacheBykey(int id);
     void creatOptimisticOrder(int id);
     void creatPessimismOrder(int id);
     public Object getCacheBykey(int id);
     public void putCache(String key,Object value);
}
