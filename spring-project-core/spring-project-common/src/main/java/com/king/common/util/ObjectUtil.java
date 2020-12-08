package com.king.common.util;

import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

/**
 * 对象实用工具类
 *
 * @author jinfeng.wu
 * @date 2020/8/17 13:57
 */
public class ObjectUtil extends ObjectUtils {
    public ObjectUtil() {
    }

    public static boolean isNotEmpty(@Nullable Object obj) {
        return !isEmpty(obj);
    }
}
