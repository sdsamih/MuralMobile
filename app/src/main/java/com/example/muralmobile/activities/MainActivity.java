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

    //JUST TESTING
    private ApiService apiService;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<Post> posts;

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
        apiService = RetrofitClient.getClient().create(ApiService.class);
        System.out.println("VAI FETCH::\n\n\n\n\n\n");
        posts = new ArrayList<>();
        adapter = new Adapter( posts, this);
        fetchPosts();
        System.out.println("no main: " + posts.toString());

        recyclerView = findViewById(R.id.recyclerViewMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        System.out.println("ADAPTER ");
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Post> fetchPosts(){
//        System.out.println("AQUI: "+apiService..toString());
        ArrayList<Post> postsToReturn = new ArrayList<>();
        Call<PostResponse> call = apiService.getAllPosts();
        System.out.println("DEPOIS: \n\n\n\n"+call.request().url());
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                if (response.isSuccessful()) {
                    System.out.println("RESPONDEU");
                    System.out.println("-------------");
                    System.out.println(response.body().getData().get(0).toString());
                    System.out.println(response.body().getData().toString());
                    System.out.println("-------------");
                    System.out.println(call.request().url());
                    posts.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
//                    String result = response.body().toString();
//                    System.out.println("RESULT: " + result);
//                    Log.d("MainActivity", result);
//                    Gson gson = new Gson();
//
//                    PostResponse responseContent = gson.fromJson(result, PostResponse.class);
//
//                    List<Post> posts = responseContent.getData();
//
//                    StringBuilder builder = new StringBuilder();
//                    for (Post post:posts){
//                        builder.append(post.getCaption()).append("\n");
//                    }
//
//                    runOnUiThread(() -> {
//                        TextView textView = findViewById(R.id.txt_main);
//                        textView.setText(builder.toString());
//                    });
//
                }
                else {
                    System.out.println("RESPOSTA NÃO FOI SUCESSFUL\n\n\n\n\n\n");
                    Log.e("MainActivity", "Resposta não bem-sucedida: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                System.out.println("FALHOU\n\n\n\n\n");
                System.out.println(t.toString());
            }
        });
        System.out.println("Final da função: " + postsToReturn.toString());
        System.out.println("outro: " + posts.toString());
        return postsToReturn;
    }

}