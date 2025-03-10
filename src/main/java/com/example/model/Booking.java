package com.example.model;

import java.util.Date;

public class Booking {
    private int id;
    private int customerId;
    private int vehicleId;
    private int driverId;
    private String destination;
    private Date startDate;
    private Date endDate;
    private String status;
    private String bookingNumber;

    public Booking() {}

    public Booking(int id, int customerId, int vehicleId, int driverId, String destination, Date startDate, Date endDate, String status, String bookingNumber) {
        this.id = id;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.bookingNumber = bookingNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getBookingNumber() { return bookingNumber; }
    public void setBookingNumber(String bookingNumber) { this.bookingNumber = bookingNumber; }
}
