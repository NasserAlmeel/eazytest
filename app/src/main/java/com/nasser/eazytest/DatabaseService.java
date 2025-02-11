package com.nasser.eazytest;

import android.content.Context;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * DatabaseService handles database connections, schema validation, and table creation.
 */
public class DatabaseService {
    private static final String TAG = "DatabaseService";
    private static DatabaseService instance;
    private final AuthService authService;

    private static final String SUPABASE_URL = BuildConfig.SUPABASE_URL; // Ensure configured in BuildConfig
    private static final String SUPABASE_API_KEY = BuildConfig.SUPABASE_API_KEY; // Ensure configured in BuildConfig

    // Private constructor for Singleton
    private DatabaseService(Context context) {
        authService = RetrofitClient.getInstance(context).create(AuthService.class);
    }

    // Singleton instance
    public static synchronized DatabaseService getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseService(context);
        }
        return instance;
    }

    /**
     * Check if a specific table exists in the database.
     *
     * @param token     Authorization token for the request.
     * @param tableName The name of the table to check.
     * @param callback  The callback to return whether the table exists.
     */

    /**
     * Create the 'users' table if it doesn't exist.
     *
     * @param token Authorization token for the request.
     */
    private void createUsersTable(String token) {
        String query = "CREATE TABLE IF NOT EXISTS users (" +
                "id UUID PRIMARY KEY DEFAULT gen_random_uuid()," +
                "email TEXT UNIQUE NOT NULL," +
                "name TEXT NOT NULL," +
                "phone TEXT," +
                "password TEXT NOT NULL," +
                "created_at TIMESTAMP DEFAULT NOW()" +
                ");";

        Call<Void> call = authService.executeSQL("Bearer " + token, SUPABASE_API_KEY, query);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Users table created successfully.");
                } else {
                    Log.e(TAG, "Failed to create 'users' table. Response code: " + response.code() +
                            ", message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error creating 'users' table", t);
            }
        });
    }

    /**
     * Callback interface for checking table existence.
     */
    interface TableExistsCallback {
        void onResult(boolean exists);
    }
}
