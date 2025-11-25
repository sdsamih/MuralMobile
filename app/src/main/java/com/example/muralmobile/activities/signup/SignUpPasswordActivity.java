package com.example.muralmobile.activities.signup;

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

public class SignUpPasswordActivity extends AppCompatActivity {
    private ImageButton button_back;
    private Button button_next;
    private TextInputEditText input_password;
    private TextInputEditText input_confirmPassword;
    private String email;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            email = receivedIntent.getStringExtra("EMAIL");
            name = receivedIntent.getStringExtra("NAME");
        }

        input_password = findViewById(R.id.input_password);
        input_confirmPassword = findViewById(R.id.input_confirmPassword);
        button_next = findViewById(R.id.button_next);
        button_back = findViewById(R.id.button_back);

        button_next.setOnClickListener(v -> {
            String password = input_password.getText().toString().trim();
            String confirmPassword = input_confirmPassword.getText().toString().trim();

            boolean isValid = true;

            if (password.isEmpty()) {
                input_password.setError("Password is required!");
                isValid = false;
            } else if (password.length() < 6) {
                input_password.setError("Password must be at least 6 characters");
                isValid = false;
            } else {
                input_password.setError(null);
            }

            if (confirmPassword.isEmpty()) {
                input_confirmPassword.setError("Please confirm your password");
                isValid = false;
            } else if (!password.equals(confirmPassword)) {
                input_confirmPassword.setError("Passwords do not match");
                isValid = false;
            } else {
                input_confirmPassword.setError(null);
            }

            if(!isValid) {
                return;
            }

            Intent intent = new Intent(SignUpPasswordActivity.this, SignUpCPFActivity.class);
            intent.putExtra("EMAIL", email);
            intent.putExtra("NAME", name);
            intent.putExtra("PASSWORD", password);

            startActivity(intent);
        });

        button_back.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }
}