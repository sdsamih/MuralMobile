package com.example.muralmobile.models;

import java.util.Date;

public class Comment {
    private String id;
    private String content;
    private Date createdAt;
    private User user;
    private String UserId;
    private String PostId;

    public Comment() {
    }

    public Comment(String id, String content, Date createdAt, User user, String userId, String postId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.user = user;
        UserId = userId;
        PostId = postId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }
}
