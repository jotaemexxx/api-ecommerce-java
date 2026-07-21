package com.ecommerce.api.dto;

public class UserResponseDto {

    private Long id;
    private String name;
    private String number;
    private String email;


    public UserResponseDto() {}

    public UserResponseDto(Long id, String name, String number, String email) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.email = email;


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

}
