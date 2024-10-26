package com.proyecto.auth_service.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.auth_service.entidades.AuthUser;
import com.proyecto.auth_service.modelos.AuthUserDto;
import com.proyecto.auth_service.modelos.RequestDto;
import com.proyecto.auth_service.modelos.TokenDto;
import com.proyecto.auth_service.service.AuthUserService;


@RestController
@RequestMapping("/auth")
public class AuthUserController {
	
	@Autowired
	AuthUserService authUserService;

	
	@PostMapping("/token")
    public ResponseEntity<TokenDto> token(@RequestBody AuthUserDto dto) {
        TokenDto tokenDto = authUserService.Token(dto); 
        if (tokenDto == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(tokenDto);
    }
	
	@PostMapping("/validate")
	public ResponseEntity<TokenDto> validate(@RequestParam String token, @RequestBody RequestDto dto) {
	    TokenDto tokenDto = authUserService.validate(token, dto);
	    if (tokenDto == null) {
	        return ResponseEntity.badRequest().build();
	    }
	    return ResponseEntity.ok(tokenDto);
	}


	
	@PostMapping("/create")
	public ResponseEntity<AuthUser> create(@RequestBody AuthUserDto dto) {
	    AuthUser authUser = authUserService.save(dto);  
	    if (authUser == null)
	        return ResponseEntity.badRequest().build();
	    return ResponseEntity.ok(authUser);
	}
}
