package com.king.common.support;
/**
 * bean字段
 *
 * @author jinfeng.wu
 * @date 2020/8/17 14:11
 */
public class BeanProperty {
    private final String name;
    private final Class<?> type;

    public String getName() {
        return this.name;
    }

    public Class<?> getType() {
        return this.type;
    }

    public BeanProperty(final String name, final Class<?> type) {
        this.name = name;
        this.type = type;
    }
}
