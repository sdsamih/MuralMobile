package com.example.muralmobile.models;

import java.util.List;

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
    private boolean isLiked;

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", caption='" + caption + '\'' +
                ", _public=" + _public +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", userId='" + userId + '\'' +
                ", Media=" + Media +
                ", likes=" + likes +
                ", user=" + user +
                ", _count=" + _count +
                ", isLiked=" + isLiked +
                '}';
    }

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

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isLiked() {
        return isLiked;
    }


}
