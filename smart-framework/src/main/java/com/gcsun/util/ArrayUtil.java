package com.gcsun.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by 11981 on 2017/9/16.
 */
public final class ArrayUtil {
    public static boolean isNotEmpty(Object[] array){
        return !ArrayUtils.isEmpty(array);
    }

    public static boolean isEmpty(Object[] array){
        return ArrayUtils.isEmpty(array);
    }

}
