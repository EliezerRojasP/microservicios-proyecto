package com.proyecto.auth_service.modelos;


public class TokenDto {

	private String token;

	
	public TokenDto() {
	}

	
	public TokenDto(String token) {
		this.token = token;
	}

	// Getter y Setter
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
