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
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;
import com.example.muralmobile.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpPasswordActivity extends AppCompatActivity {

    private static final String TAG = "SignUpPasswordActivity";
    private TextInputEditText inputPassword;
    private TextInputEditText inputConfirmPassword;
    private Button buttonCreateAccount;

    private String name;
    private String email;
    private String cpf;

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

        inputPassword = findViewById(R.id.input_password);
        inputConfirmPassword = findViewById(R.id.input_confirmPassword);
        buttonCreateAccount = findViewById(R.id.button_createAccount);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        cpf = getIntent().getStringExtra("cpf");

        buttonCreateAccount.setOnClickListener(v -> {
            String password = inputPassword.getText().toString();
            String confirmPassword = inputConfirmPassword.getText().toString();

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUpPasswordActivity.this, "Senhas não conferem", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> userRequest = new HashMap<>();
            userRequest.put("cpf", cpf);
            userRequest.put("email", email);
            userRequest.put("name", name);
            userRequest.put("bio", "");
            userRequest.put("password", password);

            Log.d(TAG, "Request body: " + userRequest.toString());

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<User> call = apiService.createUser(userRequest);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.code() == 201) {
                        User user = response.body();
                        Log.d(TAG, "Response successful: " + response.body());
                        if (user != null) {
                            SessionManager sessionManager = new SessionManager(getApplicationContext());
                            sessionManager.saveSession(user.getName(), user.getId(), user.getEmail(), null);

                            Intent intent = new Intent(SignUpPasswordActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    } else {
                        String errorBody = "";
                        try {
                            if (response.errorBody() != null) {
                                errorBody = response.errorBody().string();
                            }
                        } catch (IOException e) {
                            errorBody = "Error reading error body";
                        }
                        Log.e(TAG, "Response unsuccessful: " + response.code() + " - " + response.message() + " - " + errorBody);
                        Toast.makeText(SignUpPasswordActivity.this, "Erro ao criar usuário: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e(TAG, "API call failed: ", t);
                    //Toast.makeText(SignUpPasswordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
