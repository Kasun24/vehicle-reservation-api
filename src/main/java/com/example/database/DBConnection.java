package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/vehicle_reservation?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "3755";

    public static Connection getConnection() {
        try {
            // **FORCE MySQL DRIVER TO LOAD**
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Now, establish connection
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ MySQL JDBC Driver not found! Ensure MySQL Connector/J is installed.", e);
        } catch (SQLException e) {
            throw new RuntimeException("❌ Database connection failed!", e);
        }
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Database connection successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
