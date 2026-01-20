package com.example.vulnapp.model;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class User {
    private Long id;
    private String username;
    private String passwordHash;

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.passwordHash = hashPassword(password);
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean checkPassword(String plainPassword) {
        return BCrypt.checkpw(plainPassword, this.passwordHash);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String plainPassword) {
        this.passwordHash = hashPassword(plainPassword);
    }
}
