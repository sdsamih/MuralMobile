package com.example.muralmobile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muralmobile.models.Post;
import com.example.muralmobile.models.PostResponse;
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;
import com.example.muralmobile.utils.AdapterPosts;
import com.example.muralmobile.utils.SessionManager;

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

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;

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

        // Scroll infinito
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visible = layoutManager.getChildCount();
                int total = layoutManager.getItemCount();
                int firstVisible = layoutManager.findFirstVisibleItemPosition();

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

                    List<Post> filtered = new ArrayList<>();
                    for (Post p : newPosts) {
                        if (p.getUserId().equals(userId)) filtered.add(p);
                    }

                    posts.addAll(filtered);
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
}
