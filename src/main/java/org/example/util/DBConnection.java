package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DBConfig.getUrl(),
                DBConfig.getUser(),
                DBConfig.getPassword()
        );
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Подключение успешно.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка подключения:");
            e.printStackTrace();
        }
    }
}
