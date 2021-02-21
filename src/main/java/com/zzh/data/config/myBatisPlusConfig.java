package com.zzh.data.config;

import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zzh
 */
//@EnableTransactionManagement开启事务，可加可不加
@Configuration
public class myBatisPlusConfig {
    //注册乐观锁插件
//    @Bean
//    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
//        return new OptimisticLockerInterceptor();
//    }

    //逻辑删除插件，删除只会改字段状态
//    @Bean
//    public ISqlInjector sqlInjector() {
//        return new DefaultSqlInjector();
//    }

    //注册分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
