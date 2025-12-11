package com.example.muralmobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muralmobile.R;
import com.example.muralmobile.models.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.userName.setText(comment.getUser().getName());
        holder.commentContent.setText(comment.getContent());

        // User avatar can be loaded here with Picasso or Glide
        Picasso.get().load(comment.getUser().getAvatarUrl()).into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;
        TextView commentContent;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.comment_user_photo);
            userName = itemView.findViewById(R.id.comment_user_name);
            commentContent = itemView.findViewById(R.id.comment_content);
        }
    }
}
