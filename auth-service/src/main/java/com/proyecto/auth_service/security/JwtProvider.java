package com.proyecto.auth_service.security;

import com.proyecto.auth_service.entidades.AuthUser;
import com.proyecto.auth_service.modelos.RequestDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;
    
    @Autowired
    RouterValidator routerValidator;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(AuthUser authUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", authUser.getId());           
        claims.put("role", authUser.getRole());       

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authUser.getUserName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    
    
    public boolean validate(String token, RequestDto dto) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }
        if(!isAdmin(token) && routerValidator.isAdminPath(dto))
        	return false;
        return true;
        
    }
    

    public String getUserNameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

   private boolean isAdmin(String token) {
	   return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("role").equals("admin");
   }
}
