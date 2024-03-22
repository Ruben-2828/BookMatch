package com.example.bookmatch.model;

public class User {

    private String username;
    private String email;
    private String tokenId;

    public User(String username, String email, String tokenId) {
        this.username = username;
        this.email = email;
        this.tokenId = tokenId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
