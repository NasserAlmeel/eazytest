package com.nasser.eazytest;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
    private static final String BASE_URL = BuildConfig.SUPABASE_URL;
    private static final String API_KEY = BuildConfig.SUPABASE_API_KEY;
    private static final String TAG = "RetrofitClient"; //  Logging Tag

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
                    validateConfig();

                    // Create an OkHttpClient with interceptors
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) // Logs HTTP requests and responses
                            .addInterceptor(new Interceptor() { // Adds `apikey` and Bearer token to all requests
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request originalRequest = chain.request();
                                    String userToken = getTokenFromSharedPreferences(context); // Get User's Auth Token

                                    Request.Builder requestBuilder = originalRequest.newBuilder()
                                            .header("apikey", API_KEY)
                                            .header("Content-Type", "application/json");

                                    //  If user token exists, use it instead of API key for Authorization
                                    if (userToken != null && !userToken.isEmpty()) {
                                        requestBuilder.header("Authorization", "Bearer " + userToken);
                                    } else {
                                        requestBuilder.header("Authorization", "Bearer " + API_KEY);
                                    }

                                    Request newRequest = requestBuilder.build();

                                    //  Log request headers for debugging
                                    Log.d(TAG, "Headers: " + newRequest.headers());

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
     * Validates if BASE_URL and API_KEY are properly set.
     */
    private static void validateConfig() {
        if (BASE_URL == null || BASE_URL.isEmpty()) {
            throw new IllegalStateException("Base URL is not configured in BuildConfig");
        }
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalStateException("API Key is not configured in BuildConfig");
        }
    }

    /**
     * Retrieves the token from SharedPreferences.
     *
     * @param context The application context.
     * @return The token string if it exists, otherwise an empty string.
     */
    private static String getTokenFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SecurePrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", ""); //  Returns empty string if no token is found
    }
}