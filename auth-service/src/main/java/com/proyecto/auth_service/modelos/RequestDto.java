package com.proyecto.auth_service.modelos;

public class RequestDto {
	
	private String uri;
	private String method;
	
	// Constructor
    public RequestDto(String uri, String method) {
        this.uri = uri;
        this.method = method;
    }

    // Getters y setters
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
	
}
