package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by 11981 on 2017/9/19.
 */
public class DynamicProxy implements InvocationHandler {
    private Object target;

    public DynamicProxy(Object target){
        this.target = target;
    }

    public Object invoke(Object object, Method method, Object[] args)
            throws Throwable{
        before();
        Object result = method.invoke(target, args);
        after();
        return result;

    }
    private void before(){
        System.out.println("before");
    }

    private void after(){
        System.out.println("after");
    }

    public static void main(String[] args) {
        Hello hello = new HelloImp();
        //用通用的DynamicProxy去包装HelloImp实例
        DynamicProxy dynamicProxy = new DynamicProxy(hello);
        //调用Proxy.newProxyInstance方法动态地创建一个Hello接口的代理类
        Hello helloProxy = (Hello) Proxy.newProxyInstance(
                hello.getClass().getClassLoader(),
                hello.getClass().getInterfaces(),
                dynamicProxy
        );

        helloProxy.say("jack");
    }
}
