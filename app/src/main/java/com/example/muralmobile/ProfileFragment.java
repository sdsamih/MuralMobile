package com.example.muralmobile;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muralmobile.models.Post;
import com.example.muralmobile.models.PostResponse;
import com.example.muralmobile.models.User;
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;
import com.example.muralmobile.utils.AdapterPosts;
import com.example.muralmobile.utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextView tvUsername, tvPostsCount;
    private RecyclerView recyclerView;
    private AdapterPosts adapterPosts;
    private ArrayList<Post> posts;

    private ApiService apiService;

    private LinearLayout profile_header;
    private ImageView profilePic;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private TextView toolBar_profile;
    private boolean isHeaderCollapsed = false;


    private String userId;

    public ProfileFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUsername = view.findViewById(R.id.tv_username);
        tvPostsCount = view.findViewById(R.id.tv_posts_count);
        // AQUI: ID correto do XML!
        recyclerView = view.findViewById(R.id.recycler_view_profile_posts_fragment);

        SessionManager sessionManager = new SessionManager(requireContext());

        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(requireContext(), "Faça login", Toast.LENGTH_SHORT).show();
            return;
        }

        tvUsername.setText(sessionManager.getUserName());
        userId = sessionManager.getUserId();

        apiService = RetrofitClient.getClient().create(ApiService.class);

        posts = new ArrayList<>();
        adapterPosts = new AdapterPosts(posts, getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapterPosts);

        fetchPosts(currentPage, userId);

        profile_header = view.findViewById(R.id.profile_header);
        profilePic = view.findViewById(R.id.profile_image);
        setUserPic();
//        LinearLayout header = view.findViewById(R.id.profile_header);
        View header = view.findViewById(R.id.profile_header);
        // guarda a altura original
        header.post(() -> {
            header.setTag(header.getHeight());
        });


        // Scroll infinito
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visible = layoutManager.getChildCount();
                int total = layoutManager.getItemCount();
                int firstVisible = layoutManager.findFirstVisibleItemPosition();

                if (dy > 15) {
                    collapseHeader(header);
                }

                if (!recyclerView.canScrollVertically(-1)) {
                    expandHeader(header);
                }


                if (!isLoading && !isLastPage) {
                    if (visible + firstVisible >= total - 3 && firstVisible >= 0) {
                        currentPage++;
                        fetchPosts(currentPage, userId);
                    }
                }
            }
        });
    }

    private void fetchPosts(int page, String userId) {
        if (isLoading) return;
        isLoading = true;

        Call<PostResponse> call = apiService.getPostsPage(page, userId);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                isLoading = false;

                if (response.isSuccessful()) {
                    List<Post> newPosts = response.body().getData();

                    if (newPosts == null || newPosts.isEmpty()) {
                        isLastPage = true;
                        return;
                    }

//                    List<Post> filtered = new ArrayList<>();
//                    for (Post p : newPosts) {
//                        if (p.getUserId().equals(userId)) filtered.add(p);
//                    }

//                    posts.addAll(filtered);
                    posts.addAll(newPosts);
                    adapterPosts.notifyDataSetChanged();

                    tvPostsCount.setText(String.valueOf(posts.size()));

                } else {
                    Log.e("ProfileFragment", "Erro resposta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                isLoading = false;
                Log.e("ProfileFragment", "Erro API", t);
            }
        });
    }

    private void setUserPic(){
        apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<User> call = apiService.getUserById(this.userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
//                    User user = new User();
                    System.out.println("URL: "+response.body().getAvatarUrl());
//                    Picasso.get().load(response.body().getAvatarUrl()).fit().into(imageView);
                    String avatarUrl = response.body().getAvatarUrl();
                    Picasso.get()
                            .load(avatarUrl)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_foreground)
                            .fit()
                            .centerCrop()
                            .into(profilePic);

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void collapseHeader(View header) {
        if (isHeaderCollapsed) return;
        isHeaderCollapsed = true;

        int startHeight = header.getHeight();

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, 0);
        animator.setDuration(200);

        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = header.getLayoutParams();
            params.height = value;
            header.setLayoutParams(params);
            header.setAlpha(value / (float) startHeight);
        });

        animator.start();
    }


    private void expandHeader(View header) {
        if (!isHeaderCollapsed) return;
        isHeaderCollapsed = false;

        int targetHeight = (int) header.getTag();

        ValueAnimator animator = ValueAnimator.ofInt(header.getHeight(), targetHeight);
        animator.setDuration(200);

        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = header.getLayoutParams();
            params.height = value;
            header.setLayoutParams(params);
            header.setAlpha(value / (float) targetHeight);
        });

        animator.start();
    }



}
