package com.proyecto.auth_service.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.netflix.spectator.impl.PatternMatcher;
import com.proyecto.auth_service.modelos.RequestDto;

@Component
@ConfigurationProperties(prefix = "admin-paths")
public class RouterValidator {
	
	private List<RequestDto> paths;

	public List<RequestDto> getPaths() {
		return paths;
	}

	public void setPaths(List<RequestDto> paths) {
		this.paths = paths;
	}
	
	public boolean isAdminPath(RequestDto dto) {
        return paths.stream().anyMatch(p -> 
            PatternMatcher.matches(p.getUri(), dto.getUri()) && p.getMethod().equals(dto.getMethod())
        );
    }
}
