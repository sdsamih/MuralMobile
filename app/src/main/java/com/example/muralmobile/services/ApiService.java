package com.example.muralmobile.services;

import com.example.muralmobile.models.Comment;
import com.example.muralmobile.models.Like;
import com.example.muralmobile.models.PostResponse;
import com.example.muralmobile.models.User;
import com.example.muralmobile.models.login.LoginRequest;
import com.example.muralmobile.models.login.LoginResponse;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("posts")
    Call<PostResponse> getPostsPage(@Query("page") int page);



    @GET("posts")
    Call<PostResponse> getPostsPage(
            @Query("page") int page,
            @Query("userId") String userId);

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") String userId);

    @GET("posts/{id}/comments")
    Call<ArrayList<Comment>> getComments(@Path("id")String postId);

    @POST("posts/{id}/like")
    Call<Like> likePost(
            @Path("id") String postId,
            @Header("Authorization") String bearerToken
    );

    @DELETE("posts/{id}/like")
    Call<Void> unlikePost(
            @Path("id") String postId,
            @Header("Authorization") String bearerToken
    );

    @Headers("Content-Type: application/json")
    @POST("auths/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @Multipart
    @POST("posts")
    Call<Void> createPost(
            @Header("Authorization") String authorization,
            @Part("caption") RequestBody caption,
            @Part("public") RequestBody isPublic,
            @Part MultipartBody.Part media
    );

}
