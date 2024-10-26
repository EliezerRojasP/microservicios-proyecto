package com.proyecto.gateway_service.dto;

public class RequestDto {
	
	private String uri;
	private String method;
	
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

	public RequestDto(String uri, String method) {
		super();
		this.uri = uri;
		this.method = method;
	}
	
	
	
}

