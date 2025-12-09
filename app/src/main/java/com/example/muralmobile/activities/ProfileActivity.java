package com.example.muralmobile.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muralmobile.utils.SessionManager;
import com.example.muralmobile.models.PostResponse;
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;
import com.example.muralmobile.models.Post;
import com.example.muralmobile.models.PostResponse;
import com.example.muralmobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.muralmobile.utils.AdapterPosts;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private TextView tv_username;
    private String userId;
    private RecyclerView recyclerView;
    private AdapterPosts adapterPosts;
    private boolean isLoading = false;
    private ApiService apiService;
    private boolean isLastPage = false;
    private ArrayList<Post> posts;
    private int currentPage = 1;
    private TextView toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tv_username = findViewById(R.id.tv_username);

        posts = new ArrayList<Post>();
        adapterPosts = new AdapterPosts( posts, this);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        SessionManager sessionManager = new SessionManager(this);
        if(!sessionManager.isLoggedIn()){
            Toast.makeText(this, "Faça login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        Toast.makeText(this, "Bem-vindo " + sessionManager.getUserName(), Toast.LENGTH_SHORT).show();

        tv_username.setText(sessionManager.getUserName());
        userId = sessionManager.getUserId();
        recyclerView = findViewById(R.id.recycler_view_profile_posts);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapterPosts);
        fetchPosts(currentPage, userId);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int itensVisiveis = layoutManager.getChildCount();
                int itensTotais = layoutManager.getItemCount();
                int primeiroItemVisivel = layoutManager.findFirstVisibleItemPosition();

//                if(dx > 0)
//                    findViewById(R.id.profile_header).setVisibility(GONE);
//                else
//                    findViewById(R.id.profile_header).setVisibility(VISIBLE);
                if (!isLoading && !isLastPage) {
                    if ((itensVisiveis + primeiroItemVisivel) >= itensTotais - 3 && primeiroItemVisivel >= 0){
                        currentPage++;
                        fetchPosts(currentPage, userId);
                    }
                }
            }
        });

        //NAVIGATION BAR
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(ProfileActivity.this, "home", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_post) {
                Intent intent = new Intent(ProfileActivity.this, CreatePostActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_profile) {
                Toast.makeText(ProfileActivity.this, "perfil", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        toolbar = findViewById(R.id.toolbar_title);
        toolbar.setText(sessionManager.getUserName());

    }

    private void fetchPosts(int pagina, String userId){
        if(isLoading){ return;}
        isLoading=true;
        System.out.println("pagina: "+pagina);
        System.out.println("userId: "+userId);
        Call<PostResponse> call = apiService.getPostsPage(pagina, userId);
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
                    System.out.println("posts: "+newPosts);
                    newPosts = treatPostsToMatchUser(newPosts, userId);
                    System.out.println("userId: " +userId);
                    System.out.println("AGORA posts: "+newPosts);
                    posts.addAll(newPosts);
                    adapterPosts.notifyDataSetChanged();
                }
                else {
                    Log.e("MainActivity", "Resposta não bem-sucedida: " + response.code());
                }
            }

            private List<Post> treatPostsToMatchUser(List<Post> newPosts, String userId) {
                List<Post> userPosts = new ArrayList<>();
                for(Post i: newPosts){
                    if(i.getUserId().equals(userId)){
                        userPosts.add(i);
                    }
                }
                return userPosts;
            }


            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }
}
