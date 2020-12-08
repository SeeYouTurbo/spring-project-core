package com.king.common.util;

import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ClassUtil extends ClassUtils {
    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();

    public static MethodParameter getMethodParameter(Constructor<?> constructor, int parameterIndex) {
        SynthesizingMethodParameter synthesizingMethodParameter = new SynthesizingMethodParameter(constructor, parameterIndex);
        synthesizingMethodParameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
        return (MethodParameter)synthesizingMethodParameter;
    }

    public static MethodParameter getMethodParameter(Method method, int parameterIndex) {
        SynthesizingMethodParameter synthesizingMethodParameter = new SynthesizingMethodParameter(method, parameterIndex);
        synthesizingMethodParameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
        return (MethodParameter)synthesizingMethodParameter;
    }

    public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
        Class<?> targetClass = method.getDeclaringClass();
        Method specificMethod = getMostSpecificMethod(method, targetClass);
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        Annotation annotation = AnnotatedElementUtils.findMergedAnnotation(specificMethod, annotationType);
        if (null != annotation)
            return (A)annotation;
        return (A)AnnotatedElementUtils.findMergedAnnotation(specificMethod.getDeclaringClass(), annotationType);
    }

    public static <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
        Annotation annotation = handlerMethod.getMethodAnnotation(annotationType);
        if (null != annotation)
            return (A)annotation;
        Class<?> beanType = handlerMethod.getBeanType();
        return (A)AnnotatedElementUtils.findMergedAnnotation(beanType, annotationType);
    }
}
