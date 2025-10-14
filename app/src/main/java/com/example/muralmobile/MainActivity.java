package com.example.muralmobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private final OkHttpClient client = new OkHttpClient();

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
        fetchPosts();


    }

    private void fetchPosts() {
        String url = "http://computacao.unir.br/mural/api/posts";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MainActivity", "Erro na requisição", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    Log.d("MainActivity", result);

                    Gson gson = new Gson();

                    PostResponse responseContent = gson.fromJson(result, PostResponse.class);

                    List<Post> posts = responseContent.getData();

                    StringBuilder builder = new StringBuilder();
                    for (Post post:posts){
                        builder.append(post.getCaption()).append("\n");
                    }

                    runOnUiThread(() -> {
                        TextView textView = findViewById(R.id.txt_main);
                        textView.setText(builder.toString());
                    });

                } else {
                    Log.e("MainActivity", "Resposta não bem-sucedida: " + response.code());
                }
            }
        });
    }

}