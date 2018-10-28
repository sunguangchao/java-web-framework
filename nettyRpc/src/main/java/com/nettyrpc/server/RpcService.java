package com.nettyrpc.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RPC annotation for RPC service
 * 使用注解标注要发布的服务
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {
    Class<?> value();
}
/**
 *
 * @Retention(RetentionPolicy.RUNTIME)
 * 编译器将Annotation储存于class档中，可由VM读入
 * 注解会在class字节码文件中存在，在运行时可以通过反射获取到
 */
