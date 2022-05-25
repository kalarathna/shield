package com.kaladevi.shield.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class LoginModel {

    private String userName;
    private String password;
    private String code;
    private String firstName;
    private String lastName;
    private String organisationName;
    private UUID loginUUID;
    private String status;
    private Boolean errorFlg;
    private Boolean enableMFA;
    private String secretImageURI;
    private String jwtToken;
    private Set<Role> roles;
    private boolean active;

    public LoginModel() {
    }

    public LoginModel(LoginModel login) {
        this.userName = login.userName;
        this.password = login.password;
        this.code = login.code;
        this.firstName = login.firstName;
        this.loginUUID = login.loginUUID;
        this.status = login.status;
        this.errorFlg = login.errorFlg;
        this.enableMFA = login.enableMFA;
        this.secretImageURI = login.secretImageURI;
        this.roles=login.roles;
        this.active=login.active;
    }

//    public User(String username, String password) {
//        this.userName = username;
//        this.password = password;
//        this.active = true;
//        this.roles = new HashSet<>() {{ new Role("USER"); }};
//    }
}
