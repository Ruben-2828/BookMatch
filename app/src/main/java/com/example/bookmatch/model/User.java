package com.example.bookmatch.model;

//TODO: Eliminare e spostare eventualmente le informazioni di questo utili nell'altro user

import java.util.HashMap;
import java.util.Map;

public class User {

    private String username;
    private String email;
    private String tokenId;
    private String fullName;

    private String profileImage;

    public User(String username, String email, String tokenId) {
        this.username = username;
        this.email = email;
        this.tokenId = tokenId;
    }

    public User(String username, String email, String tokenId, String fullName) {
        this(username, email, tokenId);
        this.fullName = fullName;
    }

    public User(String username, String email, String tokenId, String fullName, String profileImage){
        this(username, email, tokenId, fullName);
        this.profileImage = profileImage;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Map<String, Object> toHashMap(){
        Map<String, Object> user = new HashMap<>();

        //mapping user info
        user.put("username", username);
        user.put("fullName", fullName);
        user.put("email", email);
        user.put("profileImage", profileImage);

        return user;
    }

}
