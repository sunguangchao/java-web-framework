package com.gcsun.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11981 on 2017/9/18.
 */
public class ProxyChain {
    private final Class<?> targetClass;
    private final Object targetObject;
    private final Method targetMethod;
    private final MethodProxy methodProxy;
    private final Object[] methodParams;



    private List<Proxy> proxyList = new ArrayList<Proxy>();
    private int proxyIndex = 0;

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList){
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }
    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }
    public Object doProxyChain() throws Throwable{
        Object methodResult;
        //proxyIndex充当代理对象的计数器
        if (proxyIndex < proxyList.size()){
            //未达到上限，从proxyList中提取Proxy对象
            methodResult = proxyList.get(proxyIndex++).doProxy(this);
        }else {
            //达到上限，执行目标对象的业务逻辑
            methodResult = methodProxy.invokeSuper(targetObject, methodParams);
        }
        return methodResult;

    }

}
