package com.nasser.eazytest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UserModel> userList = new ArrayList<>();
    private ProgressBar progressBar, loadMoreProgressBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private boolean isLoading = false;
    private int currentPage = 1;
    private final int PAGE_SIZE = 10;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ✅ Initialize Views
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        loadMoreProgressBar = findViewById(R.id.loadMoreProgressBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        // ✅ Find NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);

        // ✅ Get Header View
        View headerView = navigationView.getHeaderView(0);

        // ✅ Find TextView in Header
        TextView userNameTextView = headerView.findViewById(R.id.nav_user_name);

        // ✅ Retrieve User's Name from SharedPreferences or API
        SharedPreferences sharedPreferences = getSharedPreferences("SecurePrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("user_name", "Hi"); // Default: "User Name"

        // ✅ Set User Name in Drawer
        userNameTextView.setText(userName);

        // ✅ Setup Toolbar as ActionBar
        setSupportActionBar(toolbar);

        // ✅ Setup Navigation Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(userAdapter);

        // ✅ Initialize Retrofit Inside MainActivity
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://randomuser.me/") // ✅ API Base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // ✅ Load Initial Data
        fetchRandomUsers(currentPage);

        // ✅ Infinite Scroll Listener
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                // ✅ Load more users when reaching the bottom
                if (!isLoading && lastVisibleItemPosition >= totalItemCount - 3) {
                    currentPage++;
                    fetchRandomUsers(currentPage);
                }
            }
        });
    }

    private void fetchRandomUsers(int page) {
        isLoading = true;
        if (page == 1) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            loadMoreProgressBar.setVisibility(View.VISIBLE);
        }

        apiService.getRandomUsers(PAGE_SIZE, page).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                loadMoreProgressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    userList.addAll(response.body().getResults());
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                loadMoreProgressBar.setVisibility(View.GONE);
                Log.e("API_ERROR", "Error fetching users", t);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_version) {
            Toast.makeText(this, "App Version: 1.0.0", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_developer) {
            Toast.makeText(this, "Developed by Nasser Almeel", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_logout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}