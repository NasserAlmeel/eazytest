package com.nasser.eazytest;

public class SignupRequest {
    private String email;
    private String password;
    private String name;
    private String phone;

    public SignupRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters



    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
