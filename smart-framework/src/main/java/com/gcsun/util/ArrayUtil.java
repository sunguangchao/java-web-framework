package com.gcsun.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by 11981 on 2017/9/16.
 * 数组工具类
 */
public final class ArrayUtil {
    /**
     * 判断数组是否非空
     * @param array
     * @return
     */
    public static boolean isNotEmpty(Object[] array){
        return !ArrayUtils.isEmpty(array);
    }

    public static boolean isEmpty(Object[] array){
        return ArrayUtils.isEmpty(array);
    }

}
