package com.nasser.eazytest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class UserDetailsActivity extends AppCompatActivity {
    private ImageView profileImageView;
    private TextView nameTextView, emailTextView, phoneTextView, locationTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        profileImageView = findViewById(R.id.profileImageView);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        locationTextView = findViewById(R.id.locationTextView);
        backButton = findViewById(R.id.backButton);

        // Get data from Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String image = intent.getStringExtra("image");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String location = intent.getStringExtra("location");

        // Set data to UI elements
        nameTextView.setText(name);
        emailTextView.setText(email);
        phoneTextView.setText(phone);
        locationTextView.setText(location);

        Glide.with(this)
                .load(image)
                .into(profileImageView);

        // Handle Back Button Click
        backButton.setOnClickListener(v -> finish());
    }
}