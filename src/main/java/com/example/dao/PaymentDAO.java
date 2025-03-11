package com.example.dao;

import com.example.database.DBConnection;
import com.example.model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    // 🔹 Add a new payment
    public boolean addPayment(Payment payment) {
        String sql = "INSERT INTO payments (booking_id, amount, tax, discount, total_amount, status, payment_date) " +
                "VALUES (?, ?, ?, ?, ?, 'PENDING', NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payment.getBookingId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setBigDecimal(3, payment.getTax());
            stmt.setBigDecimal(4, payment.getDiscount());
            stmt.setBigDecimal(5, payment.getTotalAmount());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error while adding payment: " + e.getMessage());
        }
    }

    // 🔹 Get all payments
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("id"),
                        rs.getInt("booking_id"),
                        rs.getBigDecimal("amount"),
                        rs.getBigDecimal("tax"),
                        rs.getBigDecimal("discount"),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("status"),
                        rs.getTimestamp("payment_date")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching payments: " + e.getMessage());
        }
        return payments;
    }

    // 🔹 Get payment by Booking ID
    public Payment getPaymentByBookingId(int bookingId) {
        String sql = "SELECT * FROM payments WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Payment(
                        rs.getInt("id"),
                        rs.getInt("booking_id"),
                        rs.getBigDecimal("amount"),
                        rs.getBigDecimal("tax"),
                        rs.getBigDecimal("discount"),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("status"),
                        rs.getTimestamp("payment_date")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching payment by booking ID: " + e.getMessage());
        }
        return null;
    }

    // 🔹 Update all payment details
    public boolean updatePayment(Payment payment) {
        String sql = "UPDATE payments SET amount = ?, tax = ?, discount = ?, total_amount = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, payment.getAmount());
            stmt.setBigDecimal(2, payment.getTax());
            stmt.setBigDecimal(3, payment.getDiscount());
            stmt.setBigDecimal(4, payment.getTotalAmount());
            stmt.setInt(5, payment.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;  // Ensures at least one row is updated
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    // 🔹 Update payment status (Mark as PAID / CANCELLED) and set payment_date
    public boolean updatePaymentStatus(int id, String status) {
        String sql = "UPDATE payments SET status = ?, payment_date = NOW() WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error while updating payment status: " + e.getMessage());
        }
    }
    public boolean deletePayment(int paymentId) {
        String sql = "DELETE FROM payments WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, paymentId);
            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0; // ✅ Returns true if deleted successfully
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }
}


