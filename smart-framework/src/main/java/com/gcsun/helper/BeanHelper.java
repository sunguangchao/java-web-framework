package com.gcsun.helper;

import com.gcsun.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by 11981 on 2017/9/17.
 */
public final class BeanHelper {
    //key-类名，value-Bean对象
    //用于存放Bean类域Bean实例的映射关系
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();
    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> beanClass : beanClassSet){
            Object obj = ReflectionUtil.newInstance(beanClass);
            BEAN_MAP.put(beanClass, obj);
        }
    }
    /**
     * 获取 Bean 映射
     */
    public static Map<Class<?>, Object> getBeanMap(){
        return BEAN_MAP;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls){
        if (!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("cannot get bean by class:" + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }
    /**
     * 设置 Bean 实例
     */
    public static void setBean(Class<?> cls, Object obj){
        BEAN_MAP.put(cls, obj);
    }

}
