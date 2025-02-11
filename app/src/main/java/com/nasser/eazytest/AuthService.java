package com.nasser.eazytest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import java.util.List;

public interface AuthService {

    /**
     * Save user data into the `users` table (Custom Signup Logic).
     * @param token  Authorization token.
     * @param apiKey Supabase API key.
     * @param user   User object containing user details.
     * @return Void Call object.
     */
    @Headers("Content-Type: application/json")
    @POST("rest/v1/users")
    Call<Void> saveUserData(
            @Header("Authorization") String token,
            @Header("apikey") String apiKey,
            @Body User user
    );

    /**
     * Get user data from the `users` table by email (Custom Login Logic).
     * @param token  Authorization token.
     * @param apiKey Supabase API key.
     * @param email  User email to filter.
     * @return Call object with a list of User objects.
     */
    @Headers("Content-Type: application/json")
    @GET("rest/v1/users")
    Call<List<User>> getUserByEmail(
            @Header("Authorization") String token,
            @Header("apikey") String apiKey,
            @Query(value = "email", encoded = true) String email //  Prevent encoding issues
    );

    /**
     * Update user data in the `users` table by UUID.
     * @param token  Authorization token.
     * @param apiKey Supabase API key.
     * @param userId UUID of the user to update.
     * @param user   Updated user data.
     * @return Void Call object.
     */
    @Headers("Content-Type: application/json")
    @PUT("rest/v1/users")
    Call<Void> updateUserData(
            @Header("Authorization") String token,
            @Header("apikey") String apiKey,
            @Query("id") String userId,
            @Body User user
    );

    /**
     * Execute SQL for table creation or schema updates.
     * @param token  Authorization token.
     * @param apiKey Supabase API key.
     * @param query  SQL query string to execute.
     * @return Void Call object.
     */
    @Headers("Content-Type: application/json")
    @POST("rest/v1/rpc/executesql")
    Call<Void> executeSQL(
            @Header("Authorization") String token,
            @Header("apikey") String apiKey,
            @Body String query
    );

    /**
     * Delete a user by UUID.
     * @param token  Authorization token.
     * @param apiKey Supabase API key.
     * @param userId UUID of the user to delete.
     * @return Void Call object.
     */
    @Headers("Content-Type: application/json")
    @DELETE("rest/v1/users")
    Call<Void> deleteUser(
            @Header("Authorization") String token,
            @Header("apikey") String apiKey,
            @Query("id") String userId
    );
}