package com.example.muralmobile.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static String URL_BASE = "http://computacao.unir.br/mural/api/";
    private static Retrofit retrofit = null;

    public static String getUrlBase() {
        return URL_BASE;
    }

    /*
        - Ver os posts
        - Ver a quantidade de like dos posts
        - Ver os comentários dos posts
        - Logar em uma conta existente
        - Curtir os posts
        - Fazer posts
        - Fazer comentários
        - Cadastrar conta
        - Excluir posts
        - Editar o próprio perfil (nome, foto...)
        - Abrir perfil de outras pessoas para ver os posts
         */
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
