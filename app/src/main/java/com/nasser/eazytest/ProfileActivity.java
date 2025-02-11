package com.nasser.eazytest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private TextView txtName, txtEmail, txtPhone;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views (TextViews only, no EditTexts)
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);

        // Initialize AuthService
        authService = RetrofitClient.getInstance(this).create(AuthService.class);

        // Fetch User Data
        fetchUserData();

        // Enable Back Button in ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchUserData() {
        String token = getToken();
        String email = getStoredEmail();

        if (token.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_LONG).show();
            navigateToLogin();
            return;
        }

        authService.getUserByEmail("Bearer " + token, BuildConfig.SUPABASE_API_KEY, email)
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            User user = response.body().get(0);
                            txtName.setText(user.getName());
                            txtEmail.setText(user.getEmail());
                            txtPhone.setText(user.getPhone());
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getToken() {
        SharedPreferences prefs = getSharedPreferences("SecurePrefs", MODE_PRIVATE);
        return prefs.getString("auth_token", "");
    }

    private String getStoredEmail() {
        SharedPreferences prefs = getSharedPreferences("SecurePrefs", MODE_PRIVATE);
        return prefs.getString("user_email", "");
    }

    private void navigateToLogin() {
        SharedPreferences.Editor editor = getSharedPreferences("SecurePrefs", MODE_PRIVATE).edit();
        editor.clear().apply();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
