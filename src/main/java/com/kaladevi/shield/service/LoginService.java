package com.kaladevi.shield.service;
import com.kaladevi.shield.model.LoginModel;
import com.kaladevi.shield.model.UserContentDetails;
import  org.springframework.stereotype.Service;

@Service
public interface LoginService {

    public LoginModel validateLoginCredentials(LoginModel loginModel);

    public LoginModel createUser(UserContentDetails userDetails);

}
