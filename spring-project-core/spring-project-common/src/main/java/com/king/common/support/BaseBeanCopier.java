package com.king.common.support;

import com.king.common.util.BeanUtil;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
/**
 * bean 复制
 *
 * @author jinfeng.wu
 * @date 2020/11/23 9:44
 */
public abstract class BaseBeanCopier {
    private static final BaseBeanCopier.BeanCopierKey KEY_FACTORY = (BaseBeanCopier.BeanCopierKey) KeyFactory.create(BaseBeanCopier.BeanCopierKey.class);
    private static final Type CONVERTER = TypeUtils.parseType("org.springframework.cglib.core.Converter");
    private static final Type BEAN_COPIER = TypeUtils.parseType(BaseBeanCopier.class.getName());
    private static final Signature COPY;
    private static final Signature CONVERT;

    public BaseBeanCopier() {
    }

    public static BaseBeanCopier create(Class source, Class target, boolean useConverter) {
        return create(source, target, (ClassLoader)null, useConverter);
    }

    public static BaseBeanCopier create(Class source, Class target, ClassLoader classLoader, boolean useConverter) {
        BaseBeanCopier.Generator gen;
        if (classLoader == null) {
            gen = new BaseBeanCopier.Generator();
        } else {
            gen = new BaseBeanCopier.Generator(classLoader);
        }

        gen.setSource(source);
        gen.setTarget(target);
        gen.setUseConverter(useConverter);
        return gen.create();
    }

    public abstract void copy(Object from, Object to, Converter converter);

    static {
        COPY = new Signature("copy", Type.VOID_TYPE, new Type[]{Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, CONVERTER});
        CONVERT = TypeUtils.parseSignature("Object convert(Object, Class, Object)");
    }

    public static class Generator extends AbstractClassGenerator {
        private static final Source SOURCE = new Source(BaseBeanCopier.class.getName());
        private final ClassLoader classLoader;
        private Class source;
        private Class target;
        private boolean useConverter;

        Generator() {
            super(SOURCE);
            this.classLoader = null;
        }

        Generator(ClassLoader classLoader) {
            super(SOURCE);
            this.classLoader = classLoader;
        }

        public void setSource(Class source) {
            if (!Modifier.isPublic(source.getModifiers())) {
                this.setNamePrefix(source.getName());
            }

            this.source = source;
        }

        public void setTarget(Class target) {
            if (!Modifier.isPublic(target.getModifiers())) {
                this.setNamePrefix(target.getName());
            }

            this.target = target;
        }

        public void setUseConverter(boolean useConverter) {
            this.useConverter = useConverter;
        }

        @Override
        protected ClassLoader getDefaultClassLoader() {
            return this.target.getClassLoader();
        }
        @Override
        protected ProtectionDomain getProtectionDomain() {
            return ReflectUtils.getProtectionDomain(this.source);
        }

        public BaseBeanCopier create() {
            Object key = BaseBeanCopier.KEY_FACTORY.newInstance(this.source.getName(), this.target.getName(), this.useConverter);
            return (BaseBeanCopier)super.create(key);
        }
        @Override
        public void generateClass(ClassVisitor v) {
            Type sourceType = Type.getType(this.source);
            Type targetType = Type.getType(this.target);
            ClassEmitter ce = new ClassEmitter(v);
            ce.begin_class(46, 1, this.getClassName(), BaseBeanCopier.BEAN_COPIER, (Type[])null, "<generated>");
            EmitUtils.null_constructor(ce);
            CodeEmitter e = ce.begin_method(1, BaseBeanCopier.COPY, (Type[])null);
            PropertyDescriptor[] getters = BeanUtil.getBeanGetters(this.source);
            PropertyDescriptor[] setters = BeanUtil.getBeanSetters(this.target);
            Map<String, Object> names = new HashMap(16);
            PropertyDescriptor[] var9 = getters;
            int var10 = getters.length;

            int i;
            PropertyDescriptor setter;
            for(i = 0; i < var10; ++i) {
                setter = var9[i];
                names.put(setter.getName(), setter);
            }

            Local targetLocal = e.make_local();
            Local sourceLocal = e.make_local();
            e.load_arg(1);
            e.checkcast(targetType);
            e.store_local(targetLocal);
            e.load_arg(0);
            e.checkcast(sourceType);
            e.store_local(sourceLocal);

            for(i = 0; i < setters.length; ++i) {
                setter = setters[i];
                PropertyDescriptor getter = (PropertyDescriptor)names.get(setter.getName());
                if (getter != null) {
                    MethodInfo read = ReflectUtils.getMethodInfo(getter.getReadMethod());
                    MethodInfo write = ReflectUtils.getMethodInfo(setter.getWriteMethod());
                    if (this.useConverter) {
                        Type setterType = write.getSignature().getArgumentTypes()[0];
                        e.load_local(targetLocal);
                        e.load_arg(2);
                        e.load_local(sourceLocal);
                        e.invoke(read);
                        e.box(read.getSignature().getReturnType());
                        EmitUtils.load_class(e, setterType);
                        e.push(write.getSignature().getName());
                        e.invoke_interface(BaseBeanCopier.CONVERTER, BaseBeanCopier.CONVERT);
                        e.unbox_or_zero(setterType);
                        e.invoke(write);
                    } else if (compatible(getter, setter)) {
                        e.load_local(targetLocal);
                        e.load_local(sourceLocal);
                        e.invoke(read);
                        e.invoke(write);
                    }
                }
            }

            e.return_value();
            e.end_method();
            ce.end_class();
        }

        private static boolean compatible(PropertyDescriptor getter, PropertyDescriptor setter) {
            return setter.getPropertyType().isAssignableFrom(getter.getPropertyType());
        }
        @Override
        protected Object firstInstance(Class type) {
            return ReflectUtils.newInstance(type);
        }
        @Override
        protected Object nextInstance(Object instance) {
            return instance;
        }
    }

    interface BeanCopierKey {
        Object newInstance(String source, String target, boolean useConverter);
    }
}
