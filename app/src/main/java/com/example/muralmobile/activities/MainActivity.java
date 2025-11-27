package com.example.muralmobile.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast; // Import Toast
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.muralmobile.R;
import com.example.muralmobile.models.Post;
import com.example.muralmobile.models.PostResponse;
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;
import com.example.muralmobile.utils.AdapterPosts;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ApiService apiService;
    private RecyclerView recyclerView;
    private AdapterPosts adapterPosts;
    private ArrayList<Post> posts;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        posts = new ArrayList<Post>();
        adapterPosts = new AdapterPosts( posts, this);
        fetchPosts(currentPage);

        recyclerView = findViewById(R.id.recycler_view_tasks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapterPosts);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int itensVisiveis = layoutManager.getChildCount();
                int itensTotais = layoutManager.getItemCount();
                int primeiroItemVisivel = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((itensVisiveis + primeiroItemVisivel) >= itensTotais && primeiroItemVisivel >= 0){
                        currentPage++;
                        fetchPosts(currentPage);
                    }
                }
            }
        });

        //NAVIGATION BAR
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_post) {
                Toast.makeText(MainActivity.this, "postar", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_profile) {
                Toast.makeText(MainActivity.this, "perfil", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void fetchPosts(int pagina){
        if(isLoading){ return;}
        isLoading=true;
        Call<PostResponse> call = apiService.getPostsPage(pagina);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                isLoading= false;
                if (response.isSuccessful()) {
                    List<Post> newPosts = (List<Post>) response.body().getData();
                    if (newPosts == null || newPosts.isEmpty()) {
                        isLastPage = true;
                        return;
                    }
                    posts.addAll(newPosts);
                    adapterPosts.notifyDataSetChanged();
                }
                else {
                    Log.e("MainActivity", "Resposta não bem-sucedida: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }
}
