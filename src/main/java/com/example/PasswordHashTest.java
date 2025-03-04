package com.example;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashTest {
    public static void main(String[] args) {
        String plainPassword = "testpassword"; // Change this if needed
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        System.out.println("Hashed Password: " + hashedPassword);
    }
}
