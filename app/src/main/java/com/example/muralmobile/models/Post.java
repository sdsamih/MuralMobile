package com.example.muralmobile.models;

import java.util.Date;
import java.util.List;

public class Post {
    private String id;



    private String caption;
    private String imageUrl;
    private Boolean isPublic;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isVideo;
    private List<Like> likes;
    private User user;

    public String getCaption() {
        return caption;
    }


    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", caption='" + caption + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isPublic=" + isPublic +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isVideo=" + isVideo +
                ", likes=" + likes +
                ", user=" + user +
                '}';
    }
}

