package com.proyecto.auth_service.modelos;

import jakarta.validation.constraints.NotBlank;

public class NewUserDto {
	@NotBlank
    private String userName;
    
    @NotBlank
    private String password;
    
    @NotBlank
    private String role;
    
    public NewUserDto() {
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
       
	
}
