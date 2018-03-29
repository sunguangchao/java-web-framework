
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