package com.gcsun.helper;

import com.gcsun.ConfigConstant;
import com.gcsun.util.PropsUtil;

import java.util.Properties;

/**
 * Created by 11981 on 2017/9/15.
 * 加载配置项
 * 属性文件助手类
 */
public final class ConfigHelper {
    //ConfigConstant为一个接口
    private static final Properties CONFIG_PROPS = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);

    public static String getJdbcDriver(){
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_DRIVER);
    }

    public static String getJdbcUrl(){
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_URL);
    }

    public static String getJdbcUsername(){
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_USERNAME);
    }
    /**
     * 获取 JDBC 密码
     */
    public static String getJdbcPassword() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_PASSWORD);
    }

    /**
     * 获取应用基础包名
     */
    public static String getAppBasePackage() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_BASE_PACKAGE);
    }

    /**
     * 获取应用 JSP 路径
     */
    public static String getAppJspPath() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_JSP_PATH, "/WEB-INF/view/");
    }

    /**
     * 获取应用静态资源路径
     */
    public static String getAppAssetPath() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_ASSET_PATH, "/asset/");
    }

    /**
     * 获取应用文件上传限制
     */
    public static int getAppUploadLimit() {
        return PropsUtil.getInt(CONFIG_PROPS, ConfigConstant.APP_UPLOAD_LIMIT, 10);
    }

    /**
     * 根据属性名获取 String 类型的属性值
     */
    public static String getString(String key) {
        return PropsUtil.getString(CONFIG_PROPS, key);
    }

    /**
     * 根据属性名获取 int 类型的属性值
     */
    public static int getInt(String key) {
        return PropsUtil.getInt(CONFIG_PROPS, key);
    }

    /**
     * 根据属性名获取 boolean 类型的属性值
     */
    public static boolean getBoolean(String key) {
        return PropsUtil.getBoolean(CONFIG_PROPS, key);
    }
}
