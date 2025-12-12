package com.example.muralmobile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muralmobile.models.Post;
import com.example.muralmobile.models.PostResponse;
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;
import com.example.muralmobile.utils.AdapterPosts;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private ApiService apiService;
    private RecyclerView recyclerView;
    private AdapterPosts adapterPosts;
    private ArrayList<Post> posts = new ArrayList<>();
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializa API
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_tasks_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapterPosts = new AdapterPosts(posts, getContext());
        recyclerView.setAdapter(adapterPosts);

        fetchPosts(currentPage);

        // scroll infinito
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int itensVisiveis = layoutManager.getChildCount();
                int itensTotais = layoutManager.getItemCount();
                int primeiroItemVisivel = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((itensVisiveis + primeiroItemVisivel) >= itensTotais - 3 &&
                            primeiroItemVisivel >= 0) {
                        currentPage++;
                        fetchPosts(currentPage);
                    }
                }
            }
        });

        return view;
    }

    private void fetchPosts(int pagina) {
        if (isLoading) return;

        isLoading = true;

        apiService.getPostsPage(pagina).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                isLoading = false;

                if (response.isSuccessful()) {
                    List<Post> newPosts = response.body().getData();

                    if (newPosts == null || newPosts.isEmpty()) {
                        isLastPage = true;
                        return;
                    }

                    posts.addAll(newPosts);
                    adapterPosts.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                isLoading = false;
            }
        });
    }
}
