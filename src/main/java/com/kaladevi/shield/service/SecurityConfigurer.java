//package com.kaladevi.shield.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private LoginService loginService;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//
//        auth.userDetailsService(loginService);
//    }
//}
