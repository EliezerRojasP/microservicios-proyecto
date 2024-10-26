package com.proyecto.gateway_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.proyecto.gateway_service.dto.TokenDto;

import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private final WebClient.Builder webClient;

    @Autowired
    public AuthFilter(WebClient.Builder webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Verificar si el encabezado de autorización existe
        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
        }

        String tokenHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return onError(exchange, "Token format invalid", HttpStatus.BAD_REQUEST);
        }

        String token = tokenHeader.substring(7);

        // Verificar el token mediante auth-service
        return webClient.build()
                .post()
                .uri("http://auth-service/auth/validate")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(TokenDto.class)
                .flatMap(t -> chain.filter(exchange))  // Si es válido, continuar con la solicitud
                .onErrorResume(e -> onError(exchange, "Token validation failed", HttpStatus.UNAUTHORIZED));
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }

	
}

