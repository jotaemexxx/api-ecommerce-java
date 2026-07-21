package com.ecommerce.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public class UserRequestDto {

    @NotBlank(message = "o nome não pode estar em branco")
    private String name;

    @NotBlank(message = "o numero nao pode estar vazio")
    private String number;

    @NotBlank(message = "o email nao pode estar vazio")
    @Email(message = "o email deve ter um formato válido")
    private String email;

    @NotNull(message = "a senha nao pode ser nula")
    @NotBlank(message = "a senha nao pode estar em branco")
    private String password;

    public UserRequestDto(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}


