package com.nettyrpc.client.proxy;

import com.nettyrpc.client.RPCFuture;

public interface IAsyncObjectProxy {
    public RPCFuture call(String funcName, Object... args);
}
