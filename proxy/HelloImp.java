package proxy;

/**
 * Created by 11981 on 2017/9/19.
 */
public class HelloImp implements Hello {
    @Override
    public void say(String name){
        System.out.println("Hello! " + name);
    }
}
