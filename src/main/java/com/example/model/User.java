package com.example.model;

public class User {
    private int id;
    private String username;
    private String password; // Hashed password
    private String role; // e.g., "USER", "ADMIN"
    private String name; // Full name of the user
    private String address; // User address
    private String nic; // National Identity Card number
    private String phone; // Contact number

    public User() {}

    public User(int id, String username, String password, String role, String name, String address, String nic, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.address = address;
        this.nic = nic;
        this.phone = phone;
    }

    // ✅ Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
