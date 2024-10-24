package com.usuario_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class SecurityConfig {
	
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/usuario/**", "/carro/**", "/moto/**").authenticated()
                .anyRequest().permitAll()  
            )
            .oauth2Login(oauth2Login -> oauth2Login
            		.defaultSuccessUrl("/api/auth/success", true));
            
            return http.build();
    }
}
