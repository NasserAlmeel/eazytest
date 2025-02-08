package com.nasser.eazytest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private EditText nameEdit, emailEdit, phoneEdit, passwordEdit;
    private Button signupButton;
    private ProgressBar progressBar;

    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        nameEdit = findViewById(R.id.edit_name);
        emailEdit = findViewById(R.id.edit_email);
        phoneEdit = findViewById(R.id.edit_phone);
        passwordEdit = findViewById(R.id.edit_password);
        signupButton = findViewById(R.id.btn_signup);
        progressBar = findViewById(R.id.progressBar);

        // Initialize AuthService
        authService = RetrofitClient.getInstance().create(AuthService.class);

        // Handle signup button click
        signupButton.setOnClickListener(v -> handleSignup());
    }

    private void handleSignup() {
        String name = nameEdit.getText().toString().trim();
        String email = emailEdit.getText().toString().trim();
        String phone = phoneEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        // Input validation
        if (!validateInputs(name, email, phone, password)) {
            return;
        }

        // Show loading indicator
        progressBar.setVisibility(View.VISIBLE);

        // Step 1: Create user with email and password
        SignupRequest signupRequest = new SignupRequest(email, password);
        Call<SignupResponse> call = authService.registerUser(signupRequest);

        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    // Save token
                    saveToken(response.body().getAccessToken());

                    // Step 2: Save additional fields to the database
                    saveAdditionalUserData(name, phone, email);
                } else {
                    Toast.makeText(SignupActivity.this, "Signup failed: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignupActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs(String name, String email, String phone, String password) {
        if (name.isEmpty()) {
            nameEdit.setError("Name is required");
            return false;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdit.setError("Enter a valid email");
            return false;
        }

        if (phone.isEmpty() || phone.length() < 8) {
            phoneEdit.setError("Enter a valid phone number");
            return false;
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordEdit.setError("Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", token);
        editor.apply();
    }

    private void saveAdditionalUserData(String name, String phone, String email) {
        String token = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("auth_token", "");
        User user = new User(name, phone, email);

        Call<Void> call = authService.saveUserData("Bearer " + token, user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    Toast.makeText(SignupActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
