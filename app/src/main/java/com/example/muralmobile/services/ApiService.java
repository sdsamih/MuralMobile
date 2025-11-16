package com.example.muralmobile.services;

import com.example.muralmobile.models.PostResponse;
import com.example.muralmobile.models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("posts")
    Call<PostResponse> getPostsPage(@Query("page") int page);

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") String userId);

}
