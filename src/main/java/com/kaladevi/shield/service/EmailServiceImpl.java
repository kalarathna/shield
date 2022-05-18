package com.kaladevi.shield.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class EmailServiceImpl  {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String username, String firstName, LocalDate date){

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(username);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localCreateDate = LocalDate.parse(date.toString(), formatter);
        msg.setSubject("Your Password is Going to expiry");
        msg.setText(" Dear"+firstName+" Your Password is going to expiry on "+date+" Please change your password");

        javaMailSender.send(msg);
    }
}
