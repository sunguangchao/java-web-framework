package com.gcsun.proxy;

import com.gcsun.annotation.Transaction;
import com.gcsun.helper.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by 11981 on 2017/9/18.
 * 事务代理切面类
 */
public class TransactionProxy implements Proxy{
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);
    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue(){
            return false;
        }
    };

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable{
        Object result;
        //保证同一线程中事务控制的相关逻辑只会被执行一次
        boolean flag = FLAG_HOLDER.get();
        //获取目标方法
        Method method = proxyChain.getTargetMethod();
        //判断该方法是否带有Transaction注解
        if (!flag && method.isAnnotationPresent(Transaction.class)){
            FLAG_HOLDER.set(true);
            try {
                //开启事务
                DatabaseHelper.beginTransaction();
                LOGGER.debug("begin transaction");
                //执行目标方法
                result = proxyChain.doProxyChain();
                //提交事务
                DatabaseHelper.commitTransaction();
                LOGGER.debug("commit transaction");
            }catch (Exception e){
                //如果发生异常，回滚事务
                DatabaseHelper.rollbackTransaction();
                LOGGER.debug("rollback transaction");
                throw e;
            }finally {
                //移除本地变量中的标志
                FLAG_HOLDER.remove();
            }
        }else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }

}
