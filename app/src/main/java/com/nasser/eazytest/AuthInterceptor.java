package com.nasser.eazytest;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Retrieve token from SharedPreferences
        String token = getTokenFromSharedPreferences(context);

        // Build a new request with headers
        Request newRequest = originalRequest.newBuilder()
                .addHeader("apikey", BuildConfig.SUPABASE_API_KEY) // Add API Key
                .addHeader("Authorization", "Bearer " + token) // Add Bearer token if available
                .build();

        return chain.proceed(newRequest);
    }

    /**
     * Retrieves the token from SharedPreferences.
     * @param context The application context.
     * @return The stored authentication token.
     */
    private String getTokenFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", ""); // Returns empty string if no token is found
    }
}