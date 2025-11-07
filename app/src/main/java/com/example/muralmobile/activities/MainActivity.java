package com.example.muralmobile.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.muralmobile.models.Post;
import com.example.muralmobile.models.PostResponse;
import com.example.muralmobile.R;
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private ApiService apiService;

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
        fetchPosts();


    }

//    private void fetchPosts() {
//        String url = "http://computacao.unir.br/mural/api/posts";
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("MainActivity", "Erro na requisição", e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String result = response.body().string();
//                    Log.d("MainActivity", result);
//
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
//                } else {
//                    Log.e("MainActivity", "Resposta não bem-sucedida: " + response.code());
//                }
//            }
//        });
//    }

    private void fetchPosts(){
//        System.out.println("AQUI: "+apiService..toString());
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
    }

}