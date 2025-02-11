package com.nasser.eazytest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private EditText nameEdit, emailEdit, phoneEdit, passwordEdit;
    private Button signupButton;
    private ProgressBar progressBar;

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

        // Initialize AuthService
        authService = RetrofitClient.getInstance(this).create(AuthService.class);

        // Handle signup button click
        signupButton.setOnClickListener(v -> handleSignup());
    }

    private void handleSignup() {
        String name = nameEdit.getText().toString().trim();
        String email = emailEdit.getText().toString().trim();
        String phone = phoneEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(name, email, phone, password)) {
            return;
        }

        // Hash the password securely
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) {
            Toast.makeText(this, "Error hashing password. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading indicator and disable button
        progressBar.setVisibility(View.VISIBLE);
        signupButton.setEnabled(false);

        // Create a User object
        User user = new User(name, phone, email, hashedPassword);

        // Save user data to the database
        // Ensure `user` is correctly passed as a User object
        authService.saveUserData(
                "Bearer " + BuildConfig.SUPABASE_API_KEY, // ✅ Authorization token (String)
                BuildConfig.SUPABASE_API_KEY, // ✅ API key (String)
                new User(name, phone, email, hashedPassword) // ✅ User object (Correct type)
        ).enqueue(new Callback<Void>() {


            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                signupButton.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    Log.e(TAG, "Signup failed: Response Code: " + response.code());
                    String message = response.code() == 400
                            ? "Invalid input data. Please check your details."
                            : "Signup failed. Please try again later.";
                    Toast.makeText(SignupActivity.this, message, Toast.LENGTH_LONG).show();
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

        if (phone.isEmpty() || !phone.matches("^[+]?[0-9]{10,13}$")) {
            phoneEdit.setError("Enter a valid phone number (10-13 digits, optional +).");
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
