package com.kaladevi.shield.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserContentDetails {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String organisationName;
    private Date dateOfBirth;




}
