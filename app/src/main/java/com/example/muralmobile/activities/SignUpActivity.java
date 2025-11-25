package com.example.muralmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.muralmobile.R;

public class SignUpActivity extends AppCompatActivity {
    private Button button_next;
    private ImageButton button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button_next = findViewById(R.id.button_next);
        button_back = findViewById(R.id.button_back);

        button_next.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUpPasswordActivity.class);
            startActivity(intent);
        });

        button_back.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }
}