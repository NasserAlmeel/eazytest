package com.nasser.eazytest;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<Image> imageList = new ArrayList<>();
    private ProgressBar progressBar;

    private boolean isLoading = false;
    private int currentPage = 1;
    private final int pageSize = 20; // Number of images to fetch per page

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Adapter
        imageAdapter = new ImageAdapter(this, imageList);
        recyclerView.setAdapter(imageAdapter);

        // Initialize ApiService
        apiService = RetrofitClient.getInstance(this).create(ApiService.class);

        // Load Initial Data
        loadImages(currentPage, pageSize);

        // Setup Infinite Scrolling
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && layoutManager != null &&
                        layoutManager.findLastVisibleItemPosition() >= imageList.size() - 1) {
                    // Load more data when scrolled to the bottom
                    currentPage++;
                    loadImages(currentPage, pageSize);
                }
            }
        });
    }

    private void loadImages(int page, int limit) {
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);

        Call<List<Image>> call = apiService.getImages(page, limit);
        call.enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    imageList.addAll(response.body());
                    imageAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "No more images to load.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Image>> call, Throwable t) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
