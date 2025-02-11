package com.nasser.eazytest;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the response body for the signup API.
 */
public class SignupResponse {
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("user_id")
    private String userId; // Changed to String to support UUID

    @SerializedName("phone")
    private String phone;

    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "SignupResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
