package com.example.muralmobile.models;

public class _count {

    private int likes;
    private int comments;

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void addLike(){
        this.likes++;
    }

    public void addComment(){
        this.comments++;
    }
}
