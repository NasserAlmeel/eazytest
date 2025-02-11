package com.nasser.eazytest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import java.util.List;

public interface AuthService {

    /**
     * Save user data into the `users` table.
     *
     * @param user   User object containing user details.
     * @param token  Authorization token.
     * @param apiKey Supabase API key.
     * @return Void Call object.
     */
    @POST("rest/v1/users")
    Call<Void> saveUserData(
            @Body User user,
            @Header("Authorization") String token,
            @Header("apikey") String apiKey
    );

    /**
     * Get user data from the `users` table by email.
     *
     * @param email  User email to filter.
     * @param token  Authorization token.
     * @param apiKey Supabase API key.
     * @return Call object with a list of User objects.
     */
    @GET("rest/v1/users")
    Call<List<User>> getUserByEmail(
            @Query("email") String email,
            @Header("Authorization") String token,
            @Header("apikey") String apiKey
    );

    /**
     * Update user data in the `users` table by UUID.
     *
     * @param userId UUID of the user to update.
     * @param user   Updated user data.
     * @param token  Authorization token.
     * @param apiKey Supabase API key.
     * @return Void Call object.
     */
    @PUT("rest/v1/users")
    Call<Void> updateUserData(
            @Query("id") String userId, // UUID as a String
            @Body User user,
            @Header("Authorization") String token,
            @Header("apikey") String apiKey
    );

    /**
     * Login user using a custom Supabase RPC function.
     *
     * @param loginRequest Object containing email and password.
     * @param token        Authorization token.
     * @param apiKey       Supabase API key.
     * @return Call object for LoginResponse.
     */
    @POST("rest/v1/rpc/login_user")
    Call<LoginResponse> loginUser(
            @Body LoginRequest loginRequest,
            @Header("Authorization") String token,
            @Header("apikey") String apiKey
    );

    /**
     * Register a new user in the `users` table.
     *
     * @param signupRequest Object containing user details.
     * @param token         Authorization token.
     * @param apiKey        Supabase API key.
     * @return Call object for SignupResponse.
     */
    @POST("rest/v1/users")
    Call<SignupResponse> registerUser(
            @Body SignupRequest signupRequest,
            @Header("Authorization") String token,
            @Header("apikey") String apiKey
    );


    /**
     * Execute SQL for table creation or schema updates.
     *
     * @param query  SQL query string to execute.
     * @param token  Authorization token.
     * @param apiKey Supabase API key.
     * @return Void Call object.
     */
    @POST("rest/v1/rpc/executesql")
    Call<Void> executeSQL(
            @Body String query,
            @Header("Authorization") String token,
            @Header("apikey") String apiKey
    );

    /**
     * Reset user password using a custom Supabase RPC function.
     *
     * @param resetRequest Object containing reset details (e.g., email).
     * @param token        Authorization token.
     * @param apiKey       Supabase API key.
     * @return Void Call object.
     */
    @POST("rest/v1/rpc/reset_password")
    Call<Void> resetPassword(
            @Body PasswordResetRequest resetRequest,
            @Header("Authorization") String token,
            @Header("apikey") String apiKey
    );

    /**
     * Delete a user by UUID.
     *
     * @param userId UUID of the user to delete.
     * @param token  Authorization token.
     * @param apiKey Supabase API key.
     * @return Void Call object.
     */
    @POST("rest/v1/users/delete") // Replace with the actual endpoint for deleting a user in your API
    Call<Void> deleteUser(
            @Query("id") String userId, // UUID as a String
            @Header("Authorization") String token,
            @Header("apikey") String apiKey
    );

    @POST("rest/v1/users")
    Call<Void> saveUserData(
            @Header("Authorization") String token,
            @Header("apikey") String apiKey,
            @Body User user
    );
}
