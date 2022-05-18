package com.kaladevi.shield.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;

public class EmailServiceImpl {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String username, String firstName, Date date){

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(username);

        msg.setSubject("Your Password is Going to expiry");
        msg.setText(" Dear"+firstName+" Your Password is going to expiry on"+date+"Please change your password");
    }
}
