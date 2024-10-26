package com.proyecto.auth_service.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userName;
    private String password;

    public AuthUser() {}

    public AuthUser(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Método builder
    public static AuthUserBuilder builder() {
        return new AuthUserBuilder();
    }

    // Clase builder
    public static class AuthUserBuilder {
        private int id;
        private String userName;
        private String password;

        public AuthUserBuilder id(int id) {
            this.id = id;
            return this;
        }

        public AuthUserBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public AuthUserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AuthUser build() {
            return new AuthUser(id, userName, password);
        }
    }
}

