package com.king.mybatis.aspect;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.king.common.util.Func;
import com.king.common.util.WebUtil;
import com.king.mybatis.base.BaseEntity;
import io.micrometer.core.instrument.util.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;
@Aspect
@Order(0)
public class BaseMapperAspect {

    @Pointcut("this(com.baomidou.mybatisplus.core.mapper.BaseMapper)")
    public void aspectMapperPointCut() {}

    @Before("aspectMapperPointCut() && execution(public * *.insert(..)) && args(baseModel)")
    public void beforeInsert(BaseEntity baseModel) {
        baseModel.setCreateTime(LocalDateTime.now());
        baseModel.setDeleteFlag(0);
        baseModel.setUpdateTime(LocalDateTime.now());
        // 用户获取工具
        WebUtil.VvCurrentAccount dto = WebUtil.getCurrentAccount(Boolean.FALSE);
        if (dto != null && Func.isNotBlank(dto.getUserCode())){
            baseModel.setCreateUser(dto.getUserCode());
        }
    }

    @Before("aspectMapperPointCut() && execution(public * *.updateById(..)) && args(baseModel)")
    public void beforeUpdateById(BaseEntity baseModel) {
        updateEntity(baseModel);
    }

    @Before(value = "aspectMapperPointCut() && execution(public * *.update(..)) && args(baseModel, wrapper)", argNames = "baseModel,wrapper")
    public void beforeUpdate(BaseEntity baseModel, Wrapper wrapper) {
        updateEntity(baseModel);
    }

    private void updateEntity(BaseEntity baseModel) {
        baseModel.setUpdateTime(LocalDateTime.now());
        // 用户获取工具
        WebUtil.VvCurrentAccount dto = WebUtil.getCurrentAccount(Boolean.FALSE);
        if (dto != null && StringUtils.isNotBlank(dto.getUserCode())){
            baseModel.setUpdateUser(dto.getUserCode());
        }
    }
}
