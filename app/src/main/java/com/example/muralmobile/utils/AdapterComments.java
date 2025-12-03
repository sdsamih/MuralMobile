package com.example.muralmobile.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muralmobile.R;
import com.example.muralmobile.models.Comment;
import com.example.muralmobile.models.Post;
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyViewHolder> {

    private ArrayList<Comment> comments;
    private Context context;

    public AdapterComments(ArrayList<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterComments.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_post, parent, false);
        return new AdapterComments.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterComments.MyViewHolder holder, int position) {

        Comment comment = comments.get(position);
        holder.TVposterName.setText(comment.getUser().getName());
        holder.TVpostText.setText(comment.getContent());
        //IMAGEM DO USUÁRIO
        String avatarUrl = comment.getUser().getAvatarUrl();

        Picasso.get()
                .load(avatarUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .fit()
                .centerCrop()
                .into(holder.userImageProfile);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private ImageView userImageProfile;
        private TextView TVposterName;
        private TextView TVpostText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userImageProfile = itemView.findViewById(R.id.imageViewUserProfile);
            TVposterName = itemView.findViewById(R.id.textViewUserProfile);
            TVpostText = itemView.findViewById(R.id.textViewComment);


        }
    }
}
