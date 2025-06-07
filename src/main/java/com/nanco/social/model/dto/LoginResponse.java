package com.nanco.social.model.dto;

import com.nanco.social.model.User;
import java.io.Serializable;

public class LoginResponse implements Serializable {
    private Long userId;
    private String userName;
    private String email;
    private String biography;

    public LoginResponse() {
    }

    public LoginResponse(User user) {
        if (user != null) {
            this.userId = user.getUserId();
            this.userName = user.getUserName();
            this.email = user.getEmail();
            this.biography = user.getBiography();
        }
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
