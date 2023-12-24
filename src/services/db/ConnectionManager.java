package services.db;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;

public class ConnectionManager {
    private Connection conn;

    public ConnectionManager(String url, String user, String pass) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(url, user, pass);
        System.out.println("connected database successfully...");
    }

    public void close() throws SQLException{
        conn.close();
    }

    public PreparedStatement prepareStatement(String stmt) throws SQLException{
        if (conn.isClosed()) {
        }
        return conn.prepareStatement(stmt);
    }
}
