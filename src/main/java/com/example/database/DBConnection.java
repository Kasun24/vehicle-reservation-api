package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Replace 'your-root-password' with your actual MySQL root password
    private static final String URL = "jdbc:mysql://localhost:3306/vehicle_reservation?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "3755";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
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
