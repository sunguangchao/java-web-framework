package com.gcsun;

import com.gcsun.helper.*;
import com.gcsun.util.ClassUtil;

/**
 * Created by 11981 on 2017/9/18.
 */
public final class HelperLoader {
    public static void init(){
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> cls : classList){
            ClassUtil.loadClass(cls.getName());
        }
    }
}
