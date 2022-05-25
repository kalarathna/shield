package com.kaladevi.shield.model;

import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserContentDetails {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String organisationName;
    private Boolean enableMFA;
    private String secretKey;





//    private boolean active;
//    private Set<Role> roles;
//    private boolean mfa;
//    private String secret;

//    public UserContentDetails(UserContentDetails user) {
//
//        this.email = user.email;
//        this.password = user.password;
//        this.email = user.email;
//        this.active = user.active;
//        this.roles = user.roles;
//    }
//    public UserContentDetails(String username, String password, String email) {
//        this.email = username;
//        this.password = password;
//        this.email = email;
//        this.active = true;
//        this.roles = new HashSet<>() {{ new Role("USER"); }};
//    }





}
