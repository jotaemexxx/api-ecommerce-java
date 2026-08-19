package com.ecommerce.api.dto;

public class LoginResponseDto {

    private String token;
    private String email;

    public LoginResponseDto() {}

    public LoginResponseDto(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
