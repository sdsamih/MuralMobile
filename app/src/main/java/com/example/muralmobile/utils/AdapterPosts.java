package com.example.muralmobile.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muralmobile.R;
import com.example.muralmobile.activities.ExpandCommentsDialog;
import com.example.muralmobile.activities.LoginActivity;
import com.example.muralmobile.models.Comment;
import com.example.muralmobile.models.Like;
import com.example.muralmobile.models.Post;
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Post post = posts.get(position);

        holder.TVposterName.setText(post.getUser().getName());
        holder.TVlikes.setText(String.valueOf(post.getLikes()));
        holder.TVcommentsNumber.setText(String.valueOf(post.getCount().getComments()));

        String postPictureUrl =
                post.getMidia().get(0).getImageUrl();

        Picasso.get()
                .load(postPictureUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.ic_launcher_foreground)
                .fit()
                .centerCrop()
                .into(holder.postImage);

        String avatarUrl = post.getUser().getAvatarUrl();

        Picasso.get()
                .load(avatarUrl)
                .placeholder(R.drawable.userperfil)
                .error(R.drawable.ic_launcher_foreground)
                .fit()
                .centerCrop()
                .into(holder.userImageProfile);

        SessionManager sessionManager = new SessionManager(context);
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        if (sessionManager.isLoggedIn()) {
            String currentUserId = sessionManager.getUserId();
            boolean isLikedByCurrentUser = false;

            if (post.getLikesArr() != null) {
                for (Like like : post.getLikesArr()) {
                    if (like.getUserId().equals(currentUserId)) {
                        isLikedByCurrentUser = true;
                        break;
                    }
                }
            }

            post.setLiked(isLikedByCurrentUser);

            if (isLikedByCurrentUser) {
                holder.imageButtonLike.setImageResource(R.drawable.red_heart);
            } else {
                holder.imageButtonLike.setImageResource(R.drawable.heart);
            }

            if (post.getUser().getId().equals(currentUserId)) {
                holder.buttonMenu.setVisibility(View.VISIBLE);
                holder.buttonMenu.setOnClickListener(v -> {
                    PopupMenu popup = new PopupMenu(context, v);
                    popup.getMenuInflater().inflate(R.menu.post_options_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(item -> {
                        int itemId = item.getItemId();
                        if (itemId == R.id.action_delete) {
                            String token = sessionManager.getToken();
                            String bearerToken = "Bearer " + token;
                            Call<Void> deleteCall = api.deletePost(post.getId(), bearerToken);
                            deleteCall.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        posts.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, posts.size());
                                        Toast.makeText(context, "Post excluído com sucesso", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Erro ao excluir o post", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(context, "Erro ao excluir o post", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return true;
                        }
                        return false;
                    });
                    popup.show();
                });
            } else {
                holder.buttonMenu.setVisibility(View.GONE);
            }
        } else {
            post.setLiked(false);
            holder.imageButtonLike.setImageResource(R.drawable.heart);
            holder.buttonMenu.setVisibility(View.GONE);
        }

        holder.imageButtonLike.setOnClickListener(v -> {
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(context, "Faça login", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                return;
            }

            String token = sessionManager.getToken();
            String bearerToken = "Bearer " + token;

            if (post.isLiked()) {
                post.setLiked(false);
                post.getCount().setLikes(post.getCount().getLikes() - 1);
                holder.imageButtonLike.setImageResource(R.drawable.heart);
                Call<Void> unlikeCall = api.unlikePost(post.getId(), bearerToken);
                unlikeCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) { }
                });
            } else {
                post.setLiked(true);
                post.getCount().setLikes(post.getCount().getLikes() + 1);
                holder.imageButtonLike.setImageResource(R.drawable.red_heart);
                Call<Like> likeCall = api.likePost(post.getId(), bearerToken);
                likeCall.enqueue(new Callback<Like>() {
                    @Override
                    public void onResponse(Call<Like> call, Response<Like> response) {
                    }
                    @Override
                    public void onFailure(Call<Like> call, Throwable t) { }
                });
            }
            holder.TVlikes.setText(String.valueOf(post.getLikes()));
        });

        holder.imageButtonComments.setOnClickListener(v -> showCommentsDialog(post));

    }

    private void showCommentsDialog(Post post) {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            SessionManager sessionManager = new SessionManager(context);
            ApiService api = RetrofitClient.getClient().create(ApiService.class);

            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(context, "Faça login para ver os comentários", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                return;
            }

            String token = sessionManager.getToken();
            String bearerToken = "Bearer " + token;

            Call<ArrayList<Comment>> call = api.getComments(post.getId(), bearerToken);

            call.enqueue(new Callback<ArrayList<Comment>>() {
                @Override
                public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                    if (response.isSuccessful()) {
                        ArrayList<Comment> comments = response.body();
                        ExpandCommentsDialog dialog = new ExpandCommentsDialog(comments, post.getId());
                        dialog.setOnCommentPostedListener(() -> showCommentsDialog(post));
                        dialog.show(activity.getSupportFragmentManager(), "expand_comments_dialog");
                    } else {
                        Toast.makeText(context, "Failed to load comments", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {
                    Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
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
        private ImageButton imageButtonLike;
        private ImageButton imageButtonComments;
        private ImageButton buttonMenu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.postImage);
            userImageProfile = itemView.findViewById(R.id.imageViewUserProfile);
            TVcommentsNumber = itemView.findViewById(R.id.textViewCommentsN);
            TVposterName = itemView.findViewById(R.id.textViewUserProfile);
            TVlikes = itemView.findViewById(R.id.textViewLikes);
            imageButtonLike = itemView.findViewById(R.id.imageButtonLikes);
            imageButtonComments = itemView.findViewById(R.id.imageButtonComments);
            buttonMenu = itemView.findViewById(R.id.buttonMenu);
        }
    }
}
