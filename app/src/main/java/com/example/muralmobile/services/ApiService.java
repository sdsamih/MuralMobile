package com.example.muralmobile.services;

import com.example.muralmobile.models.Comment;
import com.example.muralmobile.models.Like;
import com.example.muralmobile.models.PostResponse;
import com.example.muralmobile.models.User;
import com.example.muralmobile.models.login.LoginRequest;
import com.example.muralmobile.models.login.LoginResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("posts")
    Call<PostResponse> getPostsPage(@Query("page") int page);

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") String userId);

    @GET("posts/{id}/comments")
    Call<ArrayList<Comment>> getComments(@Path("id")String postId);

    @POST("posts/{id}/like")
    Call<Like> likePost(
            @Path("id") String postId,
            @Header("Authorization") String bearerToken
    );
    @Headers("Content-Type: application/json")
    @POST("auths/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("posts/{id}/liked")
    Call<Like> isLiked(
            @Path("id") String postId,
            @Header("Authorization") String bearerToken
    );

}
