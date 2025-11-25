package com.example.muralmobile.models.signup;

public class SignUpResponse {
    private String id;
    private String email;
    private String name;
    private String cpf;
    private String createdAt;
    private String updatedAt;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
