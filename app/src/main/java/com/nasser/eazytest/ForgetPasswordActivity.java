package com.nasser.eazytest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText emailEditText;
    private Button resetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // Initialize Views
        emailEditText = findViewById(R.id.emailEditText);
        resetPasswordButton = findViewById(R.id.submitButton);

        // Handle Reset Password
        resetPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            } else {
                sendResetPasswordRequest(email);
            }
        });
    }

    private void sendResetPasswordRequest(String email) {
        // Simulate password reset action
        Toast.makeText(this, "Password reset link sent to: " + email, Toast.LENGTH_SHORT).show();
        finish(); // Close activity after action
    }
}
