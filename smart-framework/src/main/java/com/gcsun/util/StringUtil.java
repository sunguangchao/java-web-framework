package com.gcsun.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by 11981 on 2017/9/15.
 */
public final class StringUtil {

    public static final String SEPARETOR = String.valueOf((char)29);

    public static boolean isEmpty(String str){
        if (str != null){
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }
    /**
     * 判断字符串是否非空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    /**
     * 分割固定格式的字符串
     */

    public static String[] spiltString(String str, String separator){
        return StringUtils.splitByWholeSeparator(str, separator);
    }

}
