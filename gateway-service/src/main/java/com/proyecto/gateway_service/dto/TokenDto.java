package com.proyecto.gateway_service.dto;

public class TokenDto {
	
	private String token;

    // Constructor sin argumentos
    public TokenDto() {
    }

    // Constructor con argumentos
    public TokenDto(String token) {
        this.token = token;
    }

    // Getter
    public String getToken() {
        return token;
    }

    // Setter
    public void setToken(String token) {
        this.token = token;
    }
}
