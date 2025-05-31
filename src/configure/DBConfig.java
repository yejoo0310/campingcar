package configure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConfig {
	public Connection getConnection() throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/camping_car", "root", "1234");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBC 드라이버 로드 실패", e);
		}
	}
}
