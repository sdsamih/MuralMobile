package com.example.muralmobile;

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






}

