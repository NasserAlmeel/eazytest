package com.nasser.eazytest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private EditText nameEdit, emailEdit, phoneEdit, passwordEdit;
    private Button signupButton;
    private ProgressBar progressBar;
    private TextView loginText;

    private AuthService authService;
    private static final String TAG = "SignupActivity";

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
        loginText = findViewById(R.id.loginText);

        // Initialize AuthService
        authService = RetrofitClient.getInstance(this).create(AuthService.class);

        // Handle signup button click
        signupButton.setOnClickListener(v -> handleSignup());

        // Handle login text click
        loginText.setOnClickListener(v -> navigateToLogin());
    }

    private void handleSignup() {
        String name = nameEdit.getText().toString().trim();
        String email = emailEdit.getText().toString().trim();
        String phone = phoneEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        if (!validateInputs(name, email, phone, password)) {
            return;
        }

        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) {
            Toast.makeText(this, "Error hashing password. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        signupButton.setEnabled(false);

        // Step 1: Check if the email already exists in the database
        authService.getUserByEmail(
                "Bearer " + BuildConfig.SUPABASE_API_KEY,
                BuildConfig.SUPABASE_API_KEY,
                email
        ).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Email already exists
                    progressBar.setVisibility(View.GONE);
                    signupButton.setEnabled(true);
                    Toast.makeText(SignupActivity.this, "Email already registered. Try logging in.", Toast.LENGTH_LONG).show();
                } else {
                    // Step 2: If email does not exist, proceed with signup
                    createUser(name, email, phone, hashedPassword);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                signupButton.setEnabled(true);
                Log.e(TAG, "Error checking email existence", t);
                Toast.makeText(SignupActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUser(String name, String email, String phone, String password) {
        User user = new User(email, name, phone, password);

        authService.saveUserData(
                "Bearer " + BuildConfig.SUPABASE_API_KEY,
                BuildConfig.SUPABASE_API_KEY,
                user
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                signupButton.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    Log.e(TAG, "Signup failed: Response Code: " + response.code() + ", Error: " + response.message());
                    Toast.makeText(SignupActivity.this, "Signup failed. Check input and try again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                signupButton.setEnabled(true);
                Log.e(TAG, "Signup failed", t);
                Toast.makeText(SignupActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs(String name, String email, String phone, String password) {
        if (name.isEmpty() || name.length() < 3) {
            nameEdit.setError("Name must be at least 3 characters long.");
            nameEdit.requestFocus();
            return false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdit.setError("Enter a valid email address.");
            emailEdit.requestFocus();
            return false;
        }

        if (phone.isEmpty() || !phone.matches("^?[0-9]{8}$")) {
            phoneEdit.setError("Enter a valid phone number (8 digits)");
            phoneEdit.requestFocus();
            return false;
        }

        if (!isValidPassword(password)) {
            passwordEdit.setError("Password must contain uppercase, lowercase, number, and special character.");
            passwordEdit.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidPassword(String password) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
        return passwordPattern.matcher(password).matches();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error hashing password", e);
            return null;
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}