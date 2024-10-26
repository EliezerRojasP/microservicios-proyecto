package com.proyecto.auth_service.modelos;

public class AuthUserDto {

    private String userName;
    private String password;

    public AuthUserDto() {
    }

    public AuthUserDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // Getters y Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

