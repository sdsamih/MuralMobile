package com.example.muralmobile.models;
import com.example.muralmobile.models.Post;



import java.util.List;

public class PostResponse {
    private List<Post> data;

    public List<Post> getData() {
        return data;
    }

    public void setData(List<Post> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PostResponse{" +
                "data=" + data +
                '}';
    }
}
