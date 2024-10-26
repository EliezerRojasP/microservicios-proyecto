package com.proyecto.auth_service.modelos;

import jakarta.validation.constraints.NotBlank;

public class AuthUserDto {
    @NotBlank
    private String userName;
    
    @NotBlank
    private String password;

    public AuthUserDto() {
    }

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


