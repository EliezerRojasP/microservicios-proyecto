package com.proyecto.gateway_service.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.proyecto.gateway_service.dto.TokenDto;

import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config>{
	
	private WebClient.Builder webClient;
	
	public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder;
    }
	
	 @Override
	    public GatewayFilter apply(Config config) {
	        return (exchange, chain) -> {
	            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
	                return onError(exchange, HttpStatus.UNAUTHORIZED);
	            }

	            
	            String tokenHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
	            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
	                return onError(exchange, HttpStatus.BAD_REQUEST);
	            }

	            String token = tokenHeader.substring(7); 

	     
	            return webClient.build()
	                    .post()
	                    .uri("http://auth-service/auth/validate")
	                    .bodyValue(new TokenDto(token))
	                    .retrieve()
	                    .bodyToMono(TokenDto.class)
	                    .flatMap(tokenDto -> {
	                
	                        return chain.filter(exchange);
	                    })
	                    .onErrorResume(error -> onError(exchange, HttpStatus.FORBIDDEN));
	        };
	    }

	
	public Mono<Void> onError(ServerWebExchange exchange, HttpStatusCode status) {
	    ServerHttpResponse response = exchange.getResponse();
	    response.setStatusCode(status);
	    return response.setComplete();
	}

	
	public static class Config{
		
	}
}
