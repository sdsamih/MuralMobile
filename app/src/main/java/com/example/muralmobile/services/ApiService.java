package com.example.muralmobile.services;

import com.example.muralmobile.models.PostResponse;
import com.example.muralmobile.models.User;
import com.example.muralmobile.models.login.LoginRequest;
import com.example.muralmobile.models.login.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("posts")
    Call<PostResponse> getPostsPage(@Query("page") int page);

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") String userId);

    @Headers("Content-Type: application/json")
    @POST("auths/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

}
