package com.king.common.util;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * 集合util
 *
 * @author jinfeng.wu
 * @date 2020/8/17 14:10
 */
public class CollectionUtil extends CollectionUtils {
    public CollectionUtil() {
    }

    public static <T> boolean contains(@Nullable T[] array, final T element) {
        return Func.isEmpty(array) ? false : Arrays.stream(array).anyMatch((x) -> ObjectUtil.nullSafeEquals(x, element));
    }

    public static boolean isArray(Object obj) {
        return null == obj ? false : obj.getClass().isArray();
    }

    public static boolean isNotEmpty(@Nullable Collection<?> coll) {
        return !CollectionUtils.isEmpty(coll);
    }

    public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
        return !CollectionUtils.isEmpty(map);
    }
}

