package com.kaladevi.shield.controller;

import com.kaladevi.shield.model.LoginModel;
import com.kaladevi.shield.model.UserContentDetails;
import com.kaladevi.shield.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping(value="/shield/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginModel login(@RequestBody LoginModel loginModel){
        return loginService.validateLoginCredentials(loginModel);
    }

    @PostMapping(value ="/shield/createuser", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginModel createUser(@RequestBody UserContentDetails userDetails){

        System.out.println(userDetails.getEmail());
         return loginService.createUser(userDetails);

    }

    @PostMapping(value ="/shield/verifyOTP", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginModel verifyOTP(@RequestParam("userName") String userName, @RequestParam("code") String code){
       return loginService.verifyOTP(userName,code);
    }

}
