package com.example.muralmobile.models.signup;

public class SignUpRequest {
    private String cpf;
    private String email;
    private String name;
    private String bio;
    private String password;

    public SignUpRequest(String cpf, String email, String name, String bio, String password) {
        this.cpf = cpf;
        this.email = email;
        this.name = name;
        this.bio = bio;
        this.password = password;
    }
}
