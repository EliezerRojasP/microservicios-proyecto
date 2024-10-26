package com.proyecto.auth_service.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.auth_service.entidades.AuthUser;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Integer>{
	Optional<AuthUser> findByUserName(String username);
}
