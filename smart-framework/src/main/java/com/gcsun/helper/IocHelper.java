package com.gcsun.helper;

import com.gcsun.annotation.Inject;
import com.gcsun.util.ArrayUtil;
import com.gcsun.util.CollectionUtil;
import com.gcsun.util.ReflectionUtil;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by 11981 on 2017/9/17.
 * 实现依赖注入功能
 * IoC框架所管理的对象都是单例的
 */
public final class IocHelper {
    static {
        //获取所有的Bean类与Bean实例之间的映射关系（简称 Bean Map）
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)){
            //遍历Bean Map
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()){
                //从Bean Map中获取所有的Bean类与Bean实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                //获取Bean类定义的所有成员变量（简称Bean Field）
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)){
                    //遍历Bean Field
                    for (Field beanField : beanFields){
                        //判断Bean Field是否带有Inject注解
                        if (beanField.isAnnotationPresent(Inject.class)){
                            //在Bean Map中获取Bean Field对应的实例
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null){
                                //通过反射初始化BeanField的值
                                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}
