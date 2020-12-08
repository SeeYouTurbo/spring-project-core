package com.king.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.king.mybatis.aspect.BaseMapperAspect;
import com.king.mybatis.handler.VvMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
/**
 * 描述哈哈
 *
 * @author jinfeng.wu
 * @date 2020/12/8 10:23
 */
@Configuration
@MapperScan({"cn.vv.**.mapper.**"})
public class MybatisPlusConfiguration {
    @Bean
    @ConditionalOnMissingBean({PaginationInterceptor.class})
    public PaginationInterceptor mybatisPaginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return (MetaObjectHandler)new VvMetaObjectHandler();
    }

    @Bean
    public LogicSqlInjector logicSqlInjector() {
        return new LogicSqlInjector();
    }

    @Bean
    @Profile({"dev", "test"})
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public BaseMapperAspect baseMapperAspect() {
        return new BaseMapperAspect();
    }
}
