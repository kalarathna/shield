package com.kaladevi.shield.service;
import com.kaladevi.shield.model.UserDetails;
import  org.springframework.stereotype.Service;

@Service
public interface LoginService {

    public String validateLoginCredentials(String username, String password);

    public String createUser(UserDetails userDetails);

}
