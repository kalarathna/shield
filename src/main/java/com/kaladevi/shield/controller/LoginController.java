package com.kaladevi.shield.controller;

import com.kaladevi.shield.model.UserDetails;
import com.kaladevi.shield.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping(value="/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String login(String username, String password){
        String message= loginService.validateLoginCredentials(username,password);
               return message;
    }

    @PostMapping(value ="/createuser", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String createUser(@RequestBody UserDetails userDetails){

        Date d = new Date();
        userDetails.setFirstName("Kaladevi");
        userDetails.setLastName("Suresh");
        userDetails.setEmail("kala@gmail.com");
        userDetails.setPassword("kaladevi");
        userDetails.setDateOfBirth(d);
        userDetails.setOrganisationName("test");
        loginService.createUser(userDetails);
    return"null";
    }

}
