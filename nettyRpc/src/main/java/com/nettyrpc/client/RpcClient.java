package com.nettyrpc.client;

import com.nettyrpc.client.proxy.IAsyncObjectProxy;
import com.nettyrpc.client.proxy.ObjectProxy;
import com.nettyrpc.registry.ServiceDiscovery;

import java.lang.reflect.Proxy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * RPC客户端
 * @author sunguangchao
 */
public class RpcClient {
    private String serverAddress;
    private ServiceDiscovery serviceDiscovery;
    //手动创建一个线程池
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));


    public RpcClient(String serverAddress){
        this.serverAddress = serverAddress;
    }

    public RpcClient(ServiceDiscovery serviceDiscovery){
        this.serviceDiscovery = serviceDiscovery;
    }

    /**
     * 动态的创建一个被代理类的实例
     * @param interfaceClass
     * @param <T>
     * @return
     */
    public static <T> T create(Class<T> interfaceClass){
        return (T)Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ObjectProxy<T>(interfaceClass)
        );
    }

    /**
     *
     * @param interfaceClass
     * @param <T>
     * @return
     */
    public static <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass){
        return new ObjectProxy<T>(interfaceClass);
    }

    /**
     * 线程池任务提交方法
     * @param task
     */
    public static void submit(Runnable task){
        threadPoolExecutor.submit(task);
    }

    /**
     * 停止方法
     */
    public void stop(){
        threadPoolExecutor.shutdown();
        serviceDiscovery.stop();
        ConnectManage.getInstance().stop();
    }
}
