package com.example.muralmobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.muralmobile.models.User;
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Response;

public class SessionManager {

    private static final String PREF_NAME = "user_session";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_JWT_TOKEN = "jwt_token";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            sharedPreferences = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {

            e.printStackTrace();
            // Fallback to regular SharedPreferences if encryption fails
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        editor = sharedPreferences.edit();
    }

    public void saveSession(String userName, String userId, String email, String token) {
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_JWT_TOKEN, token);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_JWT_TOKEN, null);
    }

    public boolean isLoggedIn() {
        return getUserId() != null;
    }


    public boolean isUserInvalid() {
        String userId = getUserId();
        if (userId == null) {
            return true;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<User> call = apiService.getUserById(userId);

        try {
            Response<User> response = call.execute();
            if (!response.isSuccessful() && response.code() == 404) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }

        return false;
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
