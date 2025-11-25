package com.example.muralmobile.activities.signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.muralmobile.R;
import com.example.muralmobile.activities.LoginActivity;
import com.example.muralmobile.models.login.LoginRequest;
import com.example.muralmobile.models.login.LoginResponse;
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private Button button_next;
    private ImageButton button_back;
    private TextInputEditText input_email;
    private TextInputEditText input_name;
    private ApiService apiService;
    public boolean isLoading = false;

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

        input_email = findViewById(R.id.input_email);
        input_name = findViewById(R.id.input_name);
        button_next = findViewById(R.id.button_next);
        button_back = findViewById(R.id.button_back);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        button_next.setOnClickListener(v -> {
            String email = input_email.getText().toString().trim();
            String name = input_name.getText().toString().trim();

            boolean isValid = true;

            if (email.isEmpty()) {
                input_email.setError("Email is required!");
                isValid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                input_email.setError("Invalid email address");
                isValid = false;
            } else {
                input_email.setError(null);
            }

            if (name.isEmpty()) {
                input_name.setError("Name is required!");
                isValid = false;
            }
            else if (name.length() < 2) {
                input_name.setError("Name must be at least 2 characters");
                isValid = false;
            } else {
                input_name.setError(null);
            }

            if(!isValid) {
                return;
            }

            if(isLoading) {
                return;
            }

            isLoading = true;
            button_next.setEnabled(false);

            LoginRequest request = new LoginRequest(email, "dummy");

            Call<LoginResponse> call = apiService.login(request);

            //fiz a logica do login pois no endpoint nao tem get user by email para verificar existencia
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    isLoading = false;
                    button_next.setEnabled(true);

                    if (response.isSuccessful()) {
                        input_email.setError("Email already exists!");
                        input_email.requestFocus();
                        return;
                    }

                    int code = response.code();
                    switch (code) {
                        case 404:
                            //nao existe este email para fazer login, entao pode criar uma conta com ele
                            Intent intent = new Intent(SignUpActivity.this, SignUpPasswordActivity.class);
                            intent.putExtra("EMAIL", email);
                            intent.putExtra("NAME", name);
                            startActivity(intent);
                            break;

                        case 401:
                            //email ja existe
                            input_email.setError("Email already exists!");
                            input_email.requestFocus();
                            break;

                        default:
                            Toast.makeText(SignUpActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    isLoading = false;
                    button_next.setEnabled(true);

                    Toast.makeText(SignUpActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                    Log.e("API LOGIN", "Connection failed: " + t.getMessage());
                }
            });

        });

        button_back.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }
}