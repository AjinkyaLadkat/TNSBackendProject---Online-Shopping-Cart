package com.tns.onlineshopping.application;

import java.sql.*;

public class TestJDBCDriver {
    public static void main(String[] args) {
        // Test 1: Check if driver is available
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL JDBC Driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL JDBC Driver not found in classpath!");
            e.printStackTrace();
            return;
        }
        
        // Test 2: Test actual database connection
        try {
            String url = "jdbc:mysql://localhost:3306/online_shopping";
            String username = "root";  // Your MySQL username
            String password = "root";  // Your MySQL password
            
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Database connection successful!");
            conn.close();
            System.out.println("✅ Connection closed successfully!");
            
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed!");
            e.printStackTrace();
        }
    }
}
