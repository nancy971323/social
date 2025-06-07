package com.nanco.social.model.dto;

import java.io.Serializable;

public class LoginRequest implements Serializable {
    private String phoneNumber;
    private String password;

    public LoginRequest() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
