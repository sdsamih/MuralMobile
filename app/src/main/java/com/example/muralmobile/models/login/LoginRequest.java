package com.example.muralmobile.models.login;

public class LoginRequest {
    private String identifier;
    private String password;
    public LoginRequest(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }
}
