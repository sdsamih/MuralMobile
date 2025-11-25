package com.example.muralmobile.models;

import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Post {
    private String id;
    private String caption;
    private boolean _public;
    private String createdAt;
    private String updatedAt;
    private String userId;
    private List<Midia> Media;
    private List<Like> likes;
    private User user;
    private Count _count;

    public String getId() { return id; }
    public String getCaption() { return caption; }
    public boolean isPublic() { return _public; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public String getUserId() { return userId; }
    public List<Midia> getMidia() { return Media; }
    public List<Like> getLikesArr() { return likes; }
    public int getLikes() { return _count.getLikes(); }
    public User getUser() { return user; }
    public Count getCount() { return _count; }


}
