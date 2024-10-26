package com.proyecto.auth_service.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.auth_service.entidades.AuthUser;
import com.proyecto.auth_service.modelos.AuthUserDto;
import com.proyecto.auth_service.modelos.TokenDto;
import com.proyecto.auth_service.repositorio.AuthUserRepository;
import com.proyecto.auth_service.security.JwtProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthUserService {

    @Autowired
    AuthUserRepository authUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;
    
    private static final Logger log = LoggerFactory.getLogger(AuthUserService.class);

    public AuthUser save(AuthUserDto dto) {
        try {
            AuthUser authUser = new AuthUser(dto.getUserName(), passwordEncoder.encode(dto.getPassword()));
            return authUserRepository.save(authUser);
        } catch (Exception e) {
            log.error("Error guardando el usuario: ", e);
            return null;
        }
    }

    public TokenDto Token(AuthUserDto dto) {
        try {
            AuthUser user = authUserRepository.findByUserName(dto.getUserName()).orElse(null);
            if (user == null) {
                log.error("Usuario no encontrado: " + dto.getUserName());
                return null;
            }
            if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                log.error("Contraseña incorrecta para usuario: " + dto.getUserName());
                return null;
            }
            String token = jwtProvider.createToken(user);
            log.info("Token generado para usuario: " + dto.getUserName());
            return new TokenDto(token);
        } catch (Exception e) {
            log.error("Error en login: ", e);
            return null;
        }
    }

    public TokenDto validate(String token) {
        try {
            if (!jwtProvider.validate(token)) {
                log.error("Token inválido");
                return null;
            }
            log.info("Token válido");
            return new TokenDto(token);
        } catch (Exception e) {
            log.error("Error validando el token: ", e);
            return null;
        }
    }
}
