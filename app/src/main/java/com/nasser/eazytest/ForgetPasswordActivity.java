package com.nasser.eazytest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText emailEditText;
    private Button resetPasswordButton, backButton;
    private ProgressBar progressBar;

    private AuthService authService;
    private static final String TAG = "ForgetPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        resetPasswordButton = findViewById(R.id.submitButton);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.backButton);

        // Initialize AuthService
        authService = RetrofitClient.getInstance(this).create(AuthService.class);

        // Handle reset password button click
        resetPasswordButton.setOnClickListener(v -> handleResetPassword());

        backButton.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void handleResetPassword() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Email is required.");
            emailEditText.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address.");
            emailEditText.requestFocus();
            return;
        }

        // Retrieve the token from SharedPreferences
        String token = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("auth_token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "User session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading indicator
        progressBar.setVisibility(View.VISIBLE);
        resetPasswordButton.setEnabled(false);

        PasswordResetRequest resetRequest = new PasswordResetRequest(email);

        Call<Void> call = authService.resetPassword( resetRequest,"Bearer " + token, BuildConfig.SUPABASE_API_KEY);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                resetPasswordButton.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(ForgetPasswordActivity.this, "Password reset email sent successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Failed to send reset email: " + response.message());
                    Toast.makeText(ForgetPasswordActivity.this, "Failed to send reset email. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                resetPasswordButton.setEnabled(true);
                Log.e(TAG, "Error: " + t.getMessage(), t);
                Toast.makeText(ForgetPasswordActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
