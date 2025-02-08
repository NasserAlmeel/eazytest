package com.nasser.eazytest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private TextView headerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup Drawer Layout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Setup Drawer Toggle Button
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Get Header View
        View headerView = navigationView.getHeaderView(0);
        headerEmail = headerView.findViewById(R.id.header_email);

        // Load User Email
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", "User Email");
        headerEmail.setText(userEmail);

        // Handle Navigation Item Clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            handleNavigationItemSelected(item);
            return true;
        });
    }

    // Handle Navigation Item Selections
    private void handleNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            openProfile();
        } else if (id == R.id.nav_version) {
            Toast.makeText(MainActivity.this, "Version 1.0.0", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_developer) {
            Toast.makeText(MainActivity.this, "Developed by Nasser", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            logoutUser();
        }

        drawerLayout.closeDrawers();
    }

    // Open Profile Activity
    private void openProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", "No Email Found");

        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.putExtra("email", userEmail);
        startActivity(intent);
    }

    // Logout Function
    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
