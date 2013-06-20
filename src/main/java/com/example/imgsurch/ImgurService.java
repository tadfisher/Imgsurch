package com.example.imgsurch;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface ImgurService {
    @GET("/gallery/search")
    void searchImages(@Query("q") String query, Callback<Response> cb);
}
