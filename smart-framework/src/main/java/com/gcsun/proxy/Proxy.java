package com.gcsun.proxy;

/**
 * Created by 11981 on 2017/9/18.
 */
public interface Proxy {

    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
