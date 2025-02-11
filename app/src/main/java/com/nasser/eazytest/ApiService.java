package com.nasser.eazytest;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("photos")
    Call<List<Image>> getImages(
            @Query("page") int page,
            @Query("per_page") int perPage
    );
        @GET("api")
        Call<ApiResponse> getRandomUsers(
                @Query("results") int results,
                @Query("page") int page
        );
}