package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {

    private final static String serverName = "localhost";
    private final static String dbName = "review_page";
    private final static String portNumber = "1433";
    private final static String instance = ""; // Để trống nếu không có instance cụ thể
    private final static String userID = "sa";
    private final static String password = "12345";

    public static Connection getConnection() {
        String url;
        if (instance == null || instance.trim().isEmpty()) {
            url = "jdbc:sqlserver://" + serverName + ":" + portNumber + ";databaseName=" + dbName;
        } else {
            url = "jdbc:sqlserver://" + serverName + ":" + portNumber + "\\" + instance + ";databaseName=" + dbName;
        }

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url, userID, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Lỗi: Không tìm thấy driver JDBC.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Lỗi: Không thể kết nối tới SQL Server. Chi tiết: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
