package com.example.muralmobile.models.login;

public class LoginResponse {
    private String accessToken;
    private String email;
    private String sub;

    public String getAccessToken() {
        return accessToken;
    }

    public String getSub() {
        return sub;
    }

    public String getEmail() {
        return email;
    }
}
