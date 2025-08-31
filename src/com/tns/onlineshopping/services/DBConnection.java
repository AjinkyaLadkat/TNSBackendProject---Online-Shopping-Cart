package com.tns.onlineshopping.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // JDBC URL for your MySQL database - update "online_shopping" if your database name is different
    private static final String URL = "jdbc:mysql://localhost:3306/online_shopping?useSSL=false&serverTimezone=UTC";
    // MySQL username
    private static final String USER = "root";
    // MySQL password - update to your actual password
    private static final String PASSWORD = "root";

    // Static method to get a database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
