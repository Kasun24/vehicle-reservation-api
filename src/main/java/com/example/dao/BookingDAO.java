package com.example.dao;

import com.example.database.DBConnection;
import com.example.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    // 🔹 Create a new booking (User)
    public boolean createBooking(Booking booking) {
        String booking_number = generateBookingNumber();
        String sql = "INSERT INTO bookings (user_id, vehicle_id, driver_id, destination, start_date, end_date, status, booking_number) VALUES (?, ?, ?, ?, ?, ?, 'PENDING',?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getVehicleId());
            stmt.setInt(3, booking.getDriverId());
            stmt.setString(4, booking.getDestination());

            if (booking.getStartDate() != null) {
                stmt.setDate(5, new java.sql.Date(booking.getStartDate().getTime()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }

            if (booking.getEndDate() != null) {
                stmt.setDate(6, new java.sql.Date(booking.getEndDate().getTime()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }
            stmt.setString(7, booking_number);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    // 🔹 Update a booking (User)
    public boolean updateBooking(Booking booking) {
        String sql = "UPDATE bookings SET user_id = ?, vehicle_id = ?, driver_id = ?, destination = ?, start_date = ?, end_date = ?, status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getVehicleId());
            stmt.setInt(3, booking.getDriverId());
            stmt.setString(4, booking.getDestination());

            if (booking.getStartDate() != null) {
                stmt.setDate(5, new java.sql.Date(booking.getStartDate().getTime()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }

            if (booking.getEndDate() != null) {
                stmt.setDate(6, new java.sql.Date(booking.getEndDate().getTime()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }

            stmt.setString(7, booking.getStatus());
            stmt.setInt(8, booking.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    // 🔹 Get all bookings (Admin)
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("vehicle_id"),
                        rs.getInt("driver_id"),
                        rs.getString("destination"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status"),
                        rs.getString("booking_number")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        return bookings;
    }

    // 🔹 Get bookings by username (User)
    public List<Booking> getBookingsByUsername(String username) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.* FROM bookings b JOIN users u ON b.user_id = u.id WHERE u.username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("vehicle_id"),
                        rs.getInt("driver_id"),
                        rs.getString("destination"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status"),
                        rs.getString("booking_number")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        return bookings;
    }

    // 🔹 Get a booking by its ID
    public Booking getBookingById(int id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Booking(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("vehicle_id"),
                        rs.getInt("driver_id"),
                        rs.getString("destination"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status"),
                        rs.getString("booking_number")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        return null;
    }

    // 🔹 Delete a booking
    public boolean deleteBooking(int id) {
        String query = "DELETE FROM bookings WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0; // Returns true if deletion was successful
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    private String generateBookingNumber() {
        String prefix = "BKG-" + new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        String sql = "SELECT COUNT(*) FROM bookings WHERE booking_number LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prefix + "%"); // Search for existing bookings on the same date
            ResultSet rs = stmt.executeQuery();
            int count = 1;

            if (rs.next()) {
                count = rs.getInt(1) + 1; // Increment count for unique sequence
            }

            return String.format("%s-%04d", prefix, count); // Format: BKG-YYYYMMDD-XXXX
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

}
