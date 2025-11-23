package com.example.muralmobile.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muralmobile.R;
import com.example.muralmobile.models.Post;
import com.example.muralmobile.models.User;
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyViewHolder> {

    private ArrayList<Post> posts;
    private Context context;

    public AdapterPosts(ArrayList<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_post, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Post post = posts.get(position);

        holder.TVposterName.setText(post.getUser().getName());
        holder.TVlikes.setText(String.valueOf(post.getLikes()));
        holder.TVcommentsNumber.setText(String.valueOf(post.getComments()));
        holder.topComment.setText(post.getTopComment());

        // IMAGEM DO POST
        String postPictureUrl =
                post.getImageUrl();

        System.out.println("post picture url: "+ postPictureUrl);
        Picasso.get()
                .load(postPictureUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .fit()
                .centerCrop()
                .into(holder.postImage);

        //IMAGEM DO USUÁRIO
        String userId = post.getUser().getId();

        RetrofitClient.getClient()
                .create(ApiService.class)
                .getUserById(userId)
                .enqueue(new retrofit2.Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, retrofit2.Response<User> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            User user = response.body();
                            String avatarUrl = user.getAvatarUrl();
                            System.out.println(avatarUrl);

                            if (avatarUrl != null) {

                                Picasso.get()
                                        .load(avatarUrl)
                                        .placeholder(R.drawable.ic_launcher_background)
                                        .error(R.drawable.ic_launcher_foreground)
                                        .fit()
                                        .centerCrop()
                                        .into(holder.userImageProfile);

                            } else {
                                holder.userImageProfile.setImageResource(R.drawable.ic_launcher_foreground);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        holder.userImageProfile.setImageResource(R.drawable.ic_launcher_foreground);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView postImage;
        private ImageView userImageProfile;
        private TextView TVposterName;
        private TextView TVlikes;
        private TextView TVcommentsNumber;
        private TextView topComment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.postImage);
            userImageProfile = itemView.findViewById(R.id.imageViewUserProfile);
            TVcommentsNumber = itemView.findViewById(R.id.textViewCommentsN);
            TVposterName = itemView.findViewById(R.id.textViewUserProfile);
            TVlikes = itemView.findViewById(R.id.textViewLikes);
            topComment = itemView.findViewById(R.id.textViewTopComment);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }
    }
}
