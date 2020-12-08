package com.king.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 描述哈哈
 *
 * @author jinfeng.wu
 * @date 2020/12/8 10:22
 */
public class VvMetaObjectHandler implements MetaObjectHandler {
    private static final Logger log = LoggerFactory.getLogger(VvMetaObjectHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {}

    @Override
    public void updateFill(MetaObject metaObject) {}
}