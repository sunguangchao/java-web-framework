package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by 11981 on 2017/9/19.
 */
public class DynamicProxy2 implements InvocationHandler{
    private Object target;
    public DynamicProxy2(Object target){
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{

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

    public <T> T getProxy(){
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                this
        );
    }

    public static void main(String[] args) {
        DynamicProxy2 dynamicProxy2 = new DynamicProxy2(new HelloImp());
        Hello helloProxy = dynamicProxy2.getProxy();
        helloProxy.say("jack");
    }
}
