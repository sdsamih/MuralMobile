package com.example.muralmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.muralmobile.R;
import com.example.muralmobile.models.User;
import com.example.muralmobile.models.login.LoginRequest;
import com.example.muralmobile.models.login.LoginResponse;
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;
import com.example.muralmobile.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private Button button_create;
    private Button button_login;
    private TextInputEditText input_email;
    private TextInputEditText input_password;
    private ApiService apiService;
    public boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        input_email = findViewById(R.id.input_loginEmail);
        input_password = findViewById(R.id.input_loginPassword);
        button_create = findViewById(R.id.button_create);
        button_login = findViewById(R.id.button_login);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        button_login.setOnClickListener(v -> {
            String email = input_email.getText().toString().trim();
            String password = input_password.getText().toString().trim();

            if (email.isEmpty() && password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter your credentials to log in!", Toast.LENGTH_SHORT).show();
            } else if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            } else {
                if(isLoading) {
                    return;
                }
                isLoading = true;
                button_login.setText("Logging in");
                button_login.setEnabled(false);

                LoginRequest request = new LoginRequest(email, password);

                Call<LoginResponse> call = apiService.login(request);

                call.enqueue(new Callback<LoginResponse>() {

                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        if (response.isSuccessful()) {
                            LoginResponse resp = response.body();

                            String token = resp.getAccessToken();
                            String userId = resp.getSub();
                            String emailResp = resp.getEmail();

                            apiService.getUserById(userId).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> userCall, Response<User> userResponse) {
                                    isLoading = false;
                                    button_login.setText("Log in");
                                    button_login.setEnabled(true);

                                    if (userResponse.isSuccessful() && userResponse.body() != null) {
                                        String realName = userResponse.body().getName();

                                        Log.d("DEBUG_LOGIN", "Nome vindo da API: " + realName);

                                        SessionManager sessionManager = new SessionManager(LoginActivity.this);
                                        sessionManager.saveSession(realName, userId, emailResp, token);

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Erro ao recuperar nome do usuário", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> userCall, Throwable t) {
                                    isLoading = false;
                                    button_login.setText("Log in");
                                    button_login.setEnabled(true);
                                    Toast.makeText(LoginActivity.this, "Falha ao buscar dados do usuário", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            isLoading = false;
                            button_login.setText("Log in");
                            button_login.setEnabled(true);

                            int code = response.code();
                            switch(code) {
                                case 404:
                                    Toast.makeText(LoginActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(LoginActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        isLoading = false;
                        button_login.setText("Log in");
                        button_login.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                        Log.e("API LOGIN", "Connection failed: " + t.getMessage());
                    }
                });
            }
        });

        button_create.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });


    }
}