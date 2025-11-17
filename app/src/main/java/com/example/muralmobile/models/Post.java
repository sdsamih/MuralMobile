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
    private String topComment;
    private Count _count;

    public Count getCount() {
        return _count;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getLikes(){
        return this._count.getLikes();
    }
    public int getComments(){
        return this._count.getComments();
    }

    public void setCount(Count count) {
        this._count = count;
    }

    public String getTopComment() {
        return topComment;
    }

    public void setTopComment(String topComment) {
        this.topComment = topComment;
    }

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
                ", topComment='" + topComment + '\'' +
                ", _count=" + _count.toString() +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getVideo() {
        return isVideo;
    }

    public void setVideo(Boolean video) {
        isVideo = video;
    }

    public List<Like> getLikesList() {
        return likes;
    }

    public void setLikesList(List<Like> likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

