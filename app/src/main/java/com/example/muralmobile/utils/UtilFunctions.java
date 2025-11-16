package com.example.muralmobile.utils;

import android.util.Log;

import com.example.muralmobile.models.User;
import com.example.muralmobile.services.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UtilFunctions {

    // Função util fácil de chamar em qualquer activity:
    public static void fetchUserById(
            ApiService apiService,
            String userId,
            UserCallback callback
    ) {

        Call<User> call = apiService.getUserById(userId);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Interface de callback
    public interface UserCallback {
        void onSuccess(User user);
        void onError(String error);
    }
}
