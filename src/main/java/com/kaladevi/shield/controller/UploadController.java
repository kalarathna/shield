package com.kaladevi.shield.controller;


import com.kaladevi.shield.model.PasswordNotification;
import com.kaladevi.shield.model.UserContent;
import com.kaladevi.shield.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
public class UploadController {

    @Autowired
    UploadService uploadService;


    @PostMapping(value = "/shield/uploadfile", produces="application/json")
    public String uploadFiles(@RequestParam("multipartFile") MultipartFile file, @RequestParam("email") String email )throws Exception{
        UserContent userContent= new UserContent();
        userContent.setUsername(email);
        userContent.setFile(file);
//        byte[] byteFile = userContent.getFile().getBytes();
//        userContent.setFile(byteFile);
       return uploadService.uploadFile(userContent);

    }

    @PostMapping(value = "/shield/savefile", produces = "application/json")
    public String saveFile(@RequestBody UserContent userContent){
        return uploadService.saveText(userContent);
    }

    @PostMapping(value="/shield/deletefile", produces = "application/json")
    public String deleteFile(@RequestBody UserContent userContent){
        uploadService.deleteFile(userContent);
        return null;
    }

    @PostMapping(value ="/shield/getcontent", produces = "application/json" )
    public List<UserContent> getUserContent(String username){
        return uploadService.getUserContent(username);
    }

    @PostMapping(value  ="/shield/sendemail", produces = "application/json")
    public String sendPasswordExpiryNotification(@RequestParam("username") String username){
        return uploadService.sendPasswordExpiryNotification(username);
    }

    @PostMapping(value  ="/shield/savepassword", produces = "application/json")
    public String savePasswordNotification(@RequestBody PasswordNotification passwordNotification){
        return uploadService.savePasswordExpiryNotification(passwordNotification);
    }

}
