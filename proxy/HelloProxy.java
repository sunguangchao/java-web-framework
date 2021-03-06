package proxy;

/**
 * Created by 11981 on 2017/9/19.
 * 静态代理
 */
public class HelloProxy implements Hello {
    private Hello hello;
    public HelloProxy(){
        hello = new HelloImp();
    }

    public void say(String name){
        before();
        hello.say(name);
        after();
    }

    private void before(){
        System.out.println("before");
    }

    private void after(){
        System.out.println("after");
    }

    public static void main(String[] args) {
        Hello helloProxy = new HelloProxy();
        helloProxy.say("jack");
    }
}
