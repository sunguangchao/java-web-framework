
public class DBUtil{
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost:3306/demo";
	private static final String username = "root";
	private static final String password = "root";

	//定义一个用于放置数据库连接的局部线程变量（使每个线程都拥有自己的连接）
	private static ThreadLocal<Connection> connContainer = new ThreadLocal<Connection>();

	public static Connection getConnection(){
		Connection conn = connContainer.get();
		try{
			if (conn == null) {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, username, password);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			connContainer.set(conn);
		}
		return conn;
	}


	public static void closeConnection(){
		Connection conn = connContainer.get();
		try{
			if (conn != null) {
				conn.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			connContainer.remove();
		}
	}
}


//使用：作为一个布尔状态标志，用于指示发生了一个重要的一次性事件，例如完成初始化或任务结束
//使用理由：状态标志并不依赖于程序内任何其他状态，且通常只有一种状态转换
//例子：判断业务是否结束
volatile  boolean isOK = false;
public void isOK(){
	isOK = true;
}

public void doWork(){
	//循环监听状态位变化
	while (isOK) {
		//do work
	}
}

//使用：当读远多于写，结合使用内部锁和 volatile 变量来减少同步的开销
//使用理由：利用volatile保证读取操作的可见性；利用synchronized保证复合操作的原子性
@ThreadSafe
public class Counter{
	private volatile int value;
	//利用volatile保证读取操作的可见性
	public int getValue(){
		return vlaue;
	}
	//利用synchronized保证复合操作的原子性
	public synchronized int increment(){
		return value++;
	}
}

//双重检查锁定：实现线程安全的延迟初始化，同时降低同步开销
//    1.多线程并发创建对象时，会通过加锁保证只有一个线程能创建对象
//    2.对象创建完毕，执行get方法将不需要获取锁，直接返回创建对象
//
public class Singleton {
	//通过volatile声明，实现线程安全的延迟初始化
    private volatile static Singleton singleton;
    
    private Singleton(){}
    //双重锁设计
    public static Singleton getInstance(){
    	//1.多线程并发创建对象时，会通过加锁保证只有一个线程能创建对象
        if (singleton == null){
            synchronized (Singleton.class){
                if (singleton == null){
                    singleton = new Singleton();
                }
            }
        }
        //2.对象创建完毕，执行get方法将不需要获取锁，直接返回创建对象
        return singleton;
    }
}

