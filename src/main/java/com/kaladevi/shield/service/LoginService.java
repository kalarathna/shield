package com.kaladevi.shield.service;
import com.kaladevi.shield.entity.UserDetailsEntity;
import com.kaladevi.shield.model.LoginModel;
import com.kaladevi.shield.model.UserContentDetails;
import  org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface LoginService {

    public LoginModel validateLoginCredentials(LoginModel loginModel);

    public LoginModel createUser(UserContentDetails userDetails);

    public LoginModel verifyOTP(String userName, String code);

//    public Optional<UserDetailsEntity> findByUsername(String username);



}
