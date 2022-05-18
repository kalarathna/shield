package com.kaladevi.shield.service;

import com.kaladevi.shield.model.UserContentDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException{

        return null;
    }
}
