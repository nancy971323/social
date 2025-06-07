package com.nanco.social.model.dto;

import java.io.Serializable;

public class JwtResponse implements Serializable {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String userName;
    private String email;
    private String biography;

    public JwtResponse(String token, Long userId, String userName, String email, String biography) {
        this.token = token;
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.biography = biography;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
