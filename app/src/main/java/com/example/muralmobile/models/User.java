package com.example.muralmobile.models;

import java.util.List;

public class User {

    private String id;
    private String name;
    private String email;
    private String avatarUrl;
    private String bio;
    private String createdAt;
    private String updatedAt;
    private List<Like> likes;
    private List<Object> comments;
    private int totalLikes;
    private int totalComments;
    private int totalPosts;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getBio() { return bio; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public List<Like> getLikes() { return likes; }
    public List<Object> getComments() { return comments; }
    public int getTotalLikes() { return totalLikes; }
    public int getTotalComments() { return totalComments; }
    public int getTotalPosts() { return totalPosts; }
}
