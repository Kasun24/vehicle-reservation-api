package com.example.model;

public class Vehicle {
    private int id;
    private String model;
    private String brand;
    private int year;

    // Constructors
    public Vehicle() {}

    public Vehicle(int id, String model, String brand, int year) {
        this.id = id;
        this.model = model;
        this.brand = brand;
        this.year = year;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}