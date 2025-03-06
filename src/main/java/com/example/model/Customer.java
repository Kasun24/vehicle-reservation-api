package com.example.model;

public class Customer {
    private int id;
    private String username;
    private String name;
    private String address;
    private String telephone;
    private String nic;

    public Customer() {}

    public Customer(int id, String username, String name, String address, String telephone, String nic) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.nic = nic;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }
}
