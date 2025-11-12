package com.example.muralmobile.models;

public class Count {

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

    @Override
    public String toString() {
        return "Count{" +
                "likes=" + likes +
                ", comments=" + comments +
                '}';
    }
}
