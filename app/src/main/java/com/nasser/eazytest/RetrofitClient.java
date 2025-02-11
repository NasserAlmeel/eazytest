package com.nasser.eazytest;

import android.content.Context;
import android.content.SharedPreferences;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RetrofitClient {
    private static volatile Retrofit retrofit;
    private static final String BASE_URL = BuildConfig.SUPABASE_URL; // Ensure this is configured in your `BuildConfig`
    private static final String API_KEY = BuildConfig.SUPABASE_API_KEY; // Ensure this is configured in your `BuildConfig`

    /**
     * Provides a singleton instance of Retrofit.
     *
     * @param context The application context used to retrieve the shared preferences.
     * @return The Retrofit instance.
     */
    public static Retrofit getInstance(Context context) {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    if (BASE_URL == null || BASE_URL.isEmpty()) {
                        throw new IllegalStateException("Base URL is not configured in BuildConfig");
                    }
                    if (API_KEY == null || API_KEY.isEmpty()) {
                        throw new IllegalStateException("API Key is not configured in BuildConfig");
                    }

                    // Create an OkHttpClient with interceptors
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) // Logs HTTP requests and responses
                            .addInterceptor(new Interceptor() { // Adds the `apikey` and Bearer token to all requests
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request originalRequest = chain.request();
                                    String token = getTokenFromSharedPreferences(context);

                                    Request.Builder requestBuilder = originalRequest.newBuilder()
                                            .addHeader("apikey", API_KEY) // Add API Key header
                                            .addHeader("Authorization", "Bearer " + token); // Add Authorization token if available

                                    Request newRequest = requestBuilder.build();
                                    return chain.proceed(newRequest);
                                }
                            })
                            .build();

                    // Initialize Retrofit
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    /**
     * Retrieves the token from SharedPreferences.
     *
     * @param context The application context.
     * @return The token string if it exists, otherwise an empty string.
     */
    private static String getTokenFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", ""); // Returns empty string if no token is found
    }
}
