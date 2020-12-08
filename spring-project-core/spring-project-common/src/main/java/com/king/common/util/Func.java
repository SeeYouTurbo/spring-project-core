package com.king.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.king.common.support.Charsets;
import com.king.common.support.RandomType;
import org.springframework.beans.BeansException;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.function.Supplier;

/**
 * 工具的集合体
 *
 * @author jinfeng.wu
 * @date 2020/8/17 14:13
 */
public class Func {

    public static <T> T requireNotNull(T obj) {
        return Objects.requireNonNull(obj);
    }

    public static <T> T requireNotNull(T obj, String message) {
        return Objects.requireNonNull(obj, message);
    }

    public static <T> T requireNotNull(T obj, Supplier<String> messageSupplier) {
        return Objects.requireNonNull(obj, messageSupplier);
    }

    public static boolean isNull(@Nullable Object obj) {
        return Objects.isNull(obj);
    }

    public static boolean notNull(@Nullable Object obj) {
        return Objects.nonNull(obj);
    }

    public static String firstCharToLower(String str) {
        return StringUtil.lowerFirst(str);
    }

    public static String firstCharToUpper(String str) {
        return StringUtil.upperFirst(str);
    }

    public static boolean isBlank(@Nullable final CharSequence cs) {
        return StringUtil.isBlank(cs);
    }

    public static boolean isNotBlank(@Nullable final CharSequence cs) {
        return StringUtil.isNotBlank(cs);
    }

    public static boolean isAnyBlank(final CharSequence... css) {
        return StringUtil.isAnyBlank(css);
    }

    public static boolean isNoneBlank(final CharSequence... css) {
        return StringUtil.isNoneBlank(css);
    }

    public static boolean isArray(@Nullable Object obj) {
        return ObjectUtil.isArray(obj);
    }

    public static boolean isEmpty(@Nullable Object obj) {
        return ObjectUtil.isEmpty(obj);
    }

    public static boolean isNotEmpty(@Nullable Object obj) {
        return !ObjectUtil.isEmpty(obj);
    }

    public static boolean isEmpty(@Nullable Object[] array) {
        return ObjectUtil.isEmpty(array);
    }

    public static boolean isNotEmpty(@Nullable Object[] array) {
        return ObjectUtil.isNotEmpty(array);
    }

    public static boolean hasEmpty(Object... os) {
        Object[] var1 = os;
        int var2 = os.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Object o = var1[var3];
            if (isEmpty(o)) {
                return true;
            }
        }

        return false;
    }

    public static boolean allEmpty(Object... os) {
        Object[] var1 = os;
        int var2 = os.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Object o = var1[var3];
            if (!isEmpty(o)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    public static boolean equalsSafe(@Nullable Object o1, @Nullable Object o2) {
        return ObjectUtil.nullSafeEquals(o1, o2);
    }

    public static <T> boolean contains(@Nullable T[] array, final T element) {
        return CollectionUtil.contains(array, element);
    }

    public static boolean contains(@Nullable Iterator<?> iterator, Object element) {
        return CollectionUtil.contains(iterator, element);
    }

    public static boolean contains(@Nullable Enumeration<?> enumeration, Object element) {
        return CollectionUtil.contains(enumeration, element);
    }

    public static String toStr(Object str) {
        return toStr(str, "");
    }

    public static String toStr(Object str, String defaultValue) {
        return null == str ? defaultValue : String.valueOf(str);
    }

    public static boolean isNumeric(final CharSequence cs) {
        return StringUtil.isNumeric(cs);
    }

    public static int toInt(final Object value) {
        return NumberUtil.toInt(String.valueOf(value));
    }

    public static int toInt(final Object value, final int defaultValue) {
        return NumberUtil.toInt(String.valueOf(value), defaultValue);
    }

    public static long toLong(final Object value) {
        return NumberUtil.toLong(String.valueOf(value));
    }

    public static long toLong(final Object value, final long defaultValue) {
        return NumberUtil.toLong(String.valueOf(value), defaultValue);
    }

    public static Double toDouble(Object value) {
        return toDouble(String.valueOf(value), -1.0D);
    }

    public static Double toDouble(Object value, Double defaultValue) {
        return NumberUtil.toDouble(String.valueOf(value), defaultValue);
    }

    public static Float toFloat(Object value) {
        return toFloat(String.valueOf(value), -1.0F);
    }

    public static Float toFloat(Object value, Float defaultValue) {
        return NumberUtil.toFloat(String.valueOf(value), defaultValue);
    }

    public static Boolean toBoolean(Object value) {
        return toBoolean(value, (Boolean)null);
    }

    public static Boolean toBoolean(Object value, Boolean defaultValue) {
        if (value != null) {
            String val = String.valueOf(value);
            val = val.toLowerCase().trim();
            return Boolean.parseBoolean(val);
        } else {
            return defaultValue;
        }
    }

    public static Integer[] toIntArray(String str) {
        return toIntArray(",", str);
    }

    public static Integer[] toIntArray(String split, String str) {
        if (StringUtil.isEmpty(str)) {
            return new Integer[0];
        } else {
            String[] arr = str.split(split);
            Integer[] ints = new Integer[arr.length];

            for(int i = 0; i < arr.length; ++i) {
                Integer v = toInt(arr[i], 0);
                ints[i] = v;
            }

            return ints;
        }
    }

    public static List<Integer> toIntList(String str) {
        return Arrays.asList(toIntArray(str));
    }

    public static List<Integer> toIntList(String split, String str) {
        return Arrays.asList(toIntArray(split, str));
    }

    public static String[] toStrArray(String str) {
        return toStrArray(",", str);
    }

    public static String[] toStrArray(String split, String str) {
        return isBlank(str) ? new String[0] : str.split(split);
    }

    public static List<String> toStrList(String str) {
        return Arrays.asList(toStrArray(str));
    }

    public static List<String> toStrList(String split, String str) {
        return Arrays.asList(toStrArray(split, str));
    }

    public static String to62String(long num) {
        return NumberUtil.to62String(num);
    }

    public static String join(Collection<?> coll) {
        return StringUtil.join(coll);
    }

    public static String join(Collection<?> coll, String delim) {
        return StringUtil.join(coll, delim);
    }

    public static String join(Object[] arr) {
        return StringUtil.join(arr);
    }

    public static String join(Object[] arr, String delim) {
        return StringUtil.join(arr, delim);
    }

    public static String randomUUID() {
        return StringUtil.randomUUID();
    }

    public static String escapeHtml(String html) {
        return StringUtil.escapeHtml(html);
    }

    public static String random(int count) {
        return StringUtil.random(count);
    }

    public static String random(int count, RandomType randomType) {
        return StringUtil.random(count, randomType);
    }

    public static String md5Hex(final String data) {
        return DigestUtil.md5Hex(data);
    }

    public static String md5Hex(final byte[] bytes) {
        return DigestUtil.md5Hex(bytes);
    }

    public static String sha1(String srcStr) {
        return DigestUtil.sha1(srcStr);
    }

    public static String sha256(String srcStr) {
        return DigestUtil.sha256(srcStr);
    }

    public static String sha384(String srcStr) {
        return DigestUtil.sha384(srcStr);
    }

    public static String sha512(String srcStr) {
        return DigestUtil.sha512(srcStr);
    }

    public static String encrypt(String data) {
        return DigestUtil.encrypt(data);
    }

    public static String toJson(Object object) {
        return JsonUtil.toJson(object);
    }

    public static byte[] toJsonAsBytes(Object object) {
        return JsonUtil.toJsonAsBytes(object);
    }

    public static JsonNode readTree(String jsonString) {
        return JsonUtil.readTree(jsonString);
    }

    public static JsonNode readTree(InputStream in) {
        return JsonUtil.readTree(in);
    }

    public static JsonNode readTree(byte[] content) {
        return JsonUtil.readTree(content);
    }

    public static JsonNode readTree(JsonParser jsonParser) {
        return JsonUtil.readTree(jsonParser);
    }

    public static <T> T parse(byte[] bytes, Class<T> valueType) {
        return JsonUtil.parse(bytes, valueType);
    }

    public static <T> T parse(String jsonString, Class<T> valueType) {
        return JsonUtil.parse(jsonString, valueType);
    }

    public static <T> T parse(InputStream in, Class<T> valueType) {
        return JsonUtil.parse(in, valueType);
    }

    public static <T> T parse(byte[] bytes, TypeReference<?> typeReference) {
        return JsonUtil.parse(bytes, typeReference);
    }

    public static <T> T parse(String jsonString, TypeReference<?> typeReference) {
        return JsonUtil.parse(jsonString, typeReference);
    }

    public static <T> T parse(InputStream in, TypeReference<?> typeReference) {
        return JsonUtil.parse(in, typeReference);
    }

    public static String encode(String source) {
        return UrlUtil.encode(source, Charsets.UTF_8);
    }

    public static String encode(String source, Charset charset) {
        return UrlUtil.encode(source, charset);
    }

    public static String decode(String source) {
        return StringUtils.uriDecode(source, Charsets.UTF_8);
    }

    public static String decode(String source, Charset charset) {
        return StringUtils.uriDecode(source, charset);
    }



    public static Duration between(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive);
    }

    @Nullable
    public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
        return AnnotatedElementUtils.findMergedAnnotation(annotatedElement, annotationType);
    }

    public static <T> T newInstance(String clazzStr) {
        return BeanUtil.newInstance(clazzStr);
    }

    public static Object getProperty(Object bean, String propertyName) {
        return BeanUtil.getProperty(bean, propertyName);
    }

    public static void setProperty(Object bean, String propertyName, Object value) {
        BeanUtil.setProperty(bean, propertyName, value);
    }

    public static <T> T clone(T source) {
        return BeanUtil.clone(source);
    }

    public static <T> T copy(Object source, Class<T> clazz) {
        return BeanUtil.copy(source, clazz);
    }

    public static void copy(Object source, Object targetBean) {
        BeanUtil.copy(source, targetBean);
    }

    public static <T> T copyProperties(Object source, Class<T> clazz) throws BeansException {
        return BeanUtil.copyProperties(source, clazz);
    }

    public static Map<String, Object> toMap(Object bean) {
        return BeanUtil.toMap(bean);
    }

    public static <T> T toBean(Map<String, Object> beanMap, Class<T> valueType) {
        return BeanUtil.toBean(beanMap, valueType);
    }

}
