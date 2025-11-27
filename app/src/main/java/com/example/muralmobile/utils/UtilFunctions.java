package com.example.muralmobile.utils;

import com.example.muralmobile.models.Like;
import com.example.muralmobile.models.User;
import com.example.muralmobile.services.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UtilFunctions {

    // Função para buscar usuário (já existia)
    public static void fetchUserById(
            ApiService apiService,
            String userId,
            UserCallback callback
    ) {
        apiService.getUserById(userId)
                .enqueue(new Callback<User>() {
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

    // 🔹 NOVA FUNÇÃO: dar like no post
    public static void likePost(
            ApiService apiService,
            String postId,
            LikeCallback callback
    ) {

        //insira aqui o token se quiser testar a função de like:
        //(quando faz login, é retornado um token de acesso (accessToken), é só colar ele no lugar dentro do metodo createToken logo abaixo
        String token = createToken("seu token");
        apiService.likePost(postId, token )
                .enqueue(new Callback<Like>() {
                    @Override
                    public void onResponse(Call<Like> call, Response<Like> response) {

                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onError("Erro HTTP: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Like> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    // 🔹 Callback do like
    public interface LikeCallback {
        void onSuccess();
        void onError(String error);
    }

    // 🔹 Callback do usuário
    public interface UserCallback {
        void onSuccess(User user);
        void onError(String error);
    }

    public static String createToken(String token){
        return "Bearer " + token;
    }

    public interface LikedCallback {
        void onResult(boolean isLiked);
        void onError(String error);
    }

    public static void isPostLiked(
            ApiService apiService,
            String postId,
            LikedCallback callback
    ) {
        //aqui tem que pegar do shared preferences
        String token = createToken("seu token");

        apiService.isLiked(postId, token)
                .enqueue(new Callback<Like>() {
                    @Override
                    public void onResponse(Call<Like> call, Response<Like> response) {

                        if (response.isSuccessful()) {

                            // Se vier um objeto Like → usuário já curtiu
                            if (response.body() != null) {
                                callback.onResult(true); // já deu like
                            } else {
                                callback.onResult(false); // nunca deu like
                            }

                        } else if (response.code() == 404) {
                            callback.onResult(false); // não curtido
                        } else {
                            callback.onError("Erro HTTP: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Like> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

}
