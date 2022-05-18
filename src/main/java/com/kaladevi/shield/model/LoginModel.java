package com.kaladevi.shield.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LoginModel {

    private String username;
    private String password;
    private Integer code;
    private String firstName;
    private UUID loginUUID;
    private String status;

    public LoginModel() {
    }

    public LoginModel(String username, Integer code) {
        this.username = username;
        this.code = code;
    }
}
