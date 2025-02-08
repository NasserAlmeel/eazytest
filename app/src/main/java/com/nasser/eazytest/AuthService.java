package com.nasser.eazytest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AuthService {
    String API_KEY = BuildConfig.SUPABASE_API_KEY;

    // Login user with email and password
    @Headers({
            "apikey: " + API_KEY,
            "Content-Type: application/json"
    })
    @POST("auth/v1/token?grant_type=password")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    // Register a new user
    @Headers({
            "apikey: " + API_KEY,
            "Content-Type: application/json"
    })
    @POST("auth/v1/signup")
    Call<SignupResponse> registerUser(@Body SignupRequest signupRequest);

    // Get user details
    @Headers({
            "apikey: " + API_KEY
    })
    @GET("auth/v1/user")
    Call<User> getUser(
            @Header("Authorization") String token
    );

    // Update user details
    @Headers({
            "apikey: " + API_KEY,
            "Content-Type: application/json"
    })
    @PUT("auth/v1/user")
    Call<Void> updateUser(
            @Header("Authorization") String token,
            @Body User user
    );

    // Delete user account
    @Headers({
            "apikey: " + API_KEY
    })
    @DELETE("auth/v1/user")
    Call<Void> deleteUser(@Header("Authorization") String token);

    // Save user data in the custom database table
    @Headers({
            "apikey: " + API_KEY,
            "Content-Type: application/json"
    })
    @POST("rest/v1/users")
    Call<Void> saveUserData(
            @Header("Authorization") String token,
            @Body User user
    );

    // Update user password
    @Headers({
            "apikey: " + API_KEY,
            "Content-Type: application/json"
    })
    @PATCH("auth/v1/password")
    Call<Void> updatePassword(
            @Header("Authorization") String token,
            @Body PasswordUpdateRequest request
    );

    @Headers({
            "apikey: " + API_KEY,
            "Content-Type: application/json"
    })
    @POST("auth/v1/recover")
    Call<Void> resetPassword(@Body String email);
}
