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
import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {
    private Button button_next;
    private ImageButton button_back;
    private TextInputEditText inputName;
    private TextInputEditText inputEmail;
    private TextInputEditText inputCpf;

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

        inputName = findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);
        inputCpf = findViewById(R.id.input_cpf);
        button_next = findViewById(R.id.button_next);

        button_next.setOnClickListener(v -> {
            String name = inputName.getText().toString();
            String email = inputEmail.getText().toString();
            String cpf = inputCpf.getText().toString();

            Intent intent = new Intent(getApplicationContext(), SignUpPasswordActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("cpf", cpf);
            startActivity(intent);
        });
    }
}