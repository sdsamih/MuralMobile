package com.example.muralmobile.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muralmobile.models.Post;
import com.example.muralmobile.models.PostResponse;
import com.example.muralmobile.R;
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;
import com.example.muralmobile.utils.Adapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private ApiService apiService;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<Post> posts;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        apiService = RetrofitClient.getClient().create(ApiService.class); //Instância do Retrofit

        posts = new ArrayList<Post>(); //Lista de posts carregados no feed
        adapter = new Adapter( posts, this);

        fetchPosts(currentPage);

        recyclerView = findViewById(R.id.recyclerViewMain); //recyclerview do feed principal
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() { //Listener para saber quando chegou no fim do recycler
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int itensVisiveis = layoutManager.getChildCount();
                int itensTotais = layoutManager.getItemCount();
                int primeiroItemVisivel = layoutManager.findFirstVisibleItemPosition();


                if (!isLoading && !isLastPage) { // Se não estiver carregando e não for última página

                    // Chegou no fim da lista?
                    if ((itensVisiveis + primeiroItemVisivel) >= itensTotais && primeiroItemVisivel >= 0){ //se chegar no fim da lista e já tiver carregado o primeiro
                        currentPage++;
                        fetchPosts(currentPage);
                    }
                }
            }
        });
    }

    private void fetchPosts(int pagina){

        if(isLoading){ return;} //Se chamar enquanto tá carregando, não fazer nada
        isLoading=true;

        Call<PostResponse> call = apiService.getPostsPage(pagina); //Chamada do endpoint /posts com paginação

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                isLoading= false;

                if (response.isSuccessful()) {
                    List<Post> newPosts = (List<Post>) response.body().getData();

                    if (newPosts == null || newPosts.isEmpty()) {
                        isLastPage = true; // Rolou todos posts
                        return;
                    }
                    posts.addAll(newPosts);
                    adapter.notifyDataSetChanged(); //Atualizar adapter do feed
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