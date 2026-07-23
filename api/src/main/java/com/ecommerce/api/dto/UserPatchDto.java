package com.ecommerce.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class UserPatchDto {

    private String name;
    @Pattern(regexp = "^\\d{10,11}$", message = "o numero deve conter 10 ou 11 digitos")
    private String number;

    @Email(message = "o email deve ter um formarto válido")
    private String email;

    private String password;

    public String getName(){
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
