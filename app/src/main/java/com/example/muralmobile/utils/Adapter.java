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
import com.example.muralmobile.services.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

    private ArrayList<Post> posts;
    private Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("CREANDO");
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_post, parent, false);//modified-----------------------------
        return new MyViewHolder(itemLista);
    }

    public Adapter(ArrayList<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post post = posts.get(position);

        //exemplo de url de imagem do usuario:
        // https://i.ibb.co/V0pd8vZz/86d91faae9db.jpg

//        holder.postImage.;
//        holder.userImageProfile;
        holder.TVposterName.setText(post.getUser().getName());
        holder.TVlikes.setText(String.valueOf(post.getLikes()));
        holder.TVcommentsNumber.setText(String.valueOf(post.getComments()));
        holder.topComment.setText(post.getTopComment());
        String pictureUrl = RetrofitClient.getUrlBase() + "posts/download/" + post.getId();
//        Picasso.get().load(pictureUrl).fit().into(holder.postImage);
//        Picasso.get().load("https://i.ibb.co/V0pd8vZz/86d91faae9db.jpg").fit().into(holder.userImageProfile);

        System.out.println("PICTURE URL: "+ pictureUrl);
        Picasso.get()
                .load(pictureUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
//                .fit()
//                .centerInside()
                .into(holder.postImage);

        Picasso.get()
                .load("https://i.ibb.co/V0pd8vZz/86d91faae9db.jpg")
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .fit()
                .into(holder.userImageProfile);

        //userImageProfile
//        holder.NameTopCommentPoster.setText(post.getUser().getName());
        //        postImage;
//        userImageProfile;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView postImage;
        private ImageView userImageProfile;
        private TextView TVposterName;
        private TextView TVlikes;
        private TextView TVcommentsNumber;
        private TextView topComment;
//        private TextView NameTopCommentPoster;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.imageViewPost);
            userImageProfile = itemView.findViewById(R.id.imageViewUserProfile);
            TVcommentsNumber = itemView.findViewById(R.id.textViewCommentsN);
            TVposterName = itemView.findViewById(R.id.textViewUserProfile);
            TVlikes = itemView.findViewById(R.id.textViewLikes);
            topComment = itemView.findViewById(R.id.textViewTopComment);
//            NameTopCommentPoster = itemView.findViewById(R.id.textViewNameTopCommentPoster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
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
