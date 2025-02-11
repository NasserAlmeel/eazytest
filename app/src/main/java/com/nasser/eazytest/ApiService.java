package com.nasser.eazytest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    /**
     * Fetch images with pagination support.
     *
     * @param page  The current page number to fetch.
     * @param limit The number of images to fetch per page.
     * @return A Call object containing a list of Image objects.
     */
    @GET("images")
    Call<List<Image>> getImages(
            @Query("page") int page,
            @Query("limit") int limit
    );
}
