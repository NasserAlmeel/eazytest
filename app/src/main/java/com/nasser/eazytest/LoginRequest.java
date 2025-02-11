package com.nasser.eazytest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Represents the request body for the login API.
 */
public class LoginRequest {
    private String email;
    private String hashedPassword;

    // Constructor
    public LoginRequest(String email, String plainPassword) {
        this.email = email;
        this.hashedPassword = hashPassword(plainPassword);
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.hashedPassword = hashPassword(plainPassword);
    }

    // Hash the plain text password using SHA-256
    private String hashPassword(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(plainPassword.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
