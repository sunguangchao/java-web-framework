package com.gcsun.annotation;

import java.lang.annotation.*;

/**
 * Created by 11981 on 2017/9/17.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    /**
     * 注解
     */
    Class<? extends Annotation> value();

}
