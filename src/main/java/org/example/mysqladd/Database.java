package org.example.mysqladd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/birthday";
    private static final String USER = "root";
    private static final String PASSWORD = "12345"; // আপনার পাসওয়ার্ড দিন

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
