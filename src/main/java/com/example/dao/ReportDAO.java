package com.example.dao;

import com.example.database.DBConnection;

import java.sql.*;
import java.util.*;

public class ReportDAO {

    // 🔹 Generate Booking Report
    public List<Map<String, Object>> getBookingReport(String startDate, String endDate, String status) {
        List<Map<String, Object>> reportList = new ArrayList<>();
        String sql = "SELECT b.id, c.name AS customer_name, v.model AS vehicle, " +
                "b.destination, b.start_date, b.end_date, b.status " +
                "FROM bookings b " +
                "JOIN customers c ON b.customer_id = c.id " +
                "JOIN vehicles v ON b.vehicle_id = v.id " +
                "WHERE (? IS NULL OR b.start_date >= ?) " +
                "AND (? IS NULL OR b.end_date <= ?) " +
                "AND (? IS NULL OR b.status = ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, startDate);
            stmt.setString(2, startDate);
            stmt.setString(3, endDate);
            stmt.setString(4, endDate);
            stmt.setString(5, status);
            stmt.setString(6, status);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("booking_id", rs.getInt("id"));
                row.put("customer_name", rs.getString("customer_name"));
                row.put("vehicle", rs.getString("vehicle"));
                row.put("destination", rs.getString("destination"));
                row.put("start_date", rs.getString("start_date"));
                row.put("end_date", rs.getString("end_date"));
                row.put("status", rs.getString("status"));
                reportList.add(row);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reportList;
    }

    // 🔹 Generate Payment Report
    public List<Map<String, Object>> getPaymentReport(String startDate, String endDate, String status) {
        List<Map<String, Object>> reportList = new ArrayList<>();
        String sql = "SELECT p.id, p.booking_id, p.amount, p.tax, p.discount, p.total_amount, " +
                "p.status, p.payment_date " +
                "FROM payments p " +
                "WHERE (? IS NULL OR p.payment_date >= ?) " +
                "AND (? IS NULL OR p.payment_date <= ?) " +
                "AND (? IS NULL OR p.status = ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, startDate);
            stmt.setString(2, startDate);
            stmt.setString(3, endDate);
            stmt.setString(4, endDate);
            stmt.setString(5, status);
            stmt.setString(6, status);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("payment_id", rs.getInt("id"));
                row.put("booking_id", rs.getInt("booking_id"));
                row.put("amount", rs.getDouble("amount"));
                row.put("tax", rs.getDouble("tax"));
                row.put("discount", rs.getDouble("discount"));
                row.put("total_amount", rs.getDouble("total_amount"));
                row.put("status", rs.getString("status"));
                row.put("payment_date", rs.getString("payment_date"));
                reportList.add(row);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reportList;
    }
}
