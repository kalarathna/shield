package com.kaladevi.shield.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaladevi.shield.model.PasswordNotification;
import com.kaladevi.shield.model.UserContent;
import com.kaladevi.shield.service.UploadService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
public class UploadController {

    @Autowired
    UploadService uploadService;


    @PostMapping(value = "/shield/uploadfile", produces="multipart/form-data")
    public @ResponseBody String uploadFiles(@RequestParam("file") MultipartFile file, @RequestParam("expiryDate") String expiryDate, @RequestParam("userName") String userName, @RequestParam("documentName") String documentName)throws Exception{
       return uploadService.uploadFile(file, expiryDate,userName,documentName);

    }

//    @PostMapping(value = "/shield/savefile", produces = "multipart/form-data")
//    public @ResponseBody
//    String saveFile(@RequestBody UserContent userContent){
//        String response="";
//        Gson gson =new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'").create();
//        response=gson.toJson(uploadService.saveText(userContent));
//        return response ;
//    }

    @PostMapping(value="/shield/deletefile", produces = "application/json")
    public @ResponseBody String deleteFile(@RequestParam("userContentId") String userContentId){
        String status=uploadService.deleteFile(userContentId);
        return status;

    }

    @PostMapping(value ="/shield/getcontent", produces = "application/json" )
    public @ResponseBody String getUserContent(@RequestBody String userName){
        String response="";
        UserContent userContentList= uploadService.getUserContent(userName);
        Gson gson =new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'").create();
        response=gson.toJson(userContentList);
        return response;
    }

    @PostMapping(value  ="/shield/sendemail", produces = "application/json")
    public @ResponseBody String sendPasswordExpiryNotification(@RequestParam("username") String username){
        return uploadService.sendPasswordExpiryNotification(username);
    }

    @PostMapping(value  ="/shield/savepassword", produces = "application/json")
    public @ResponseBody String savePasswordNotification(@RequestBody PasswordNotification passwordNotification){
        return uploadService.savePasswordExpiryNotification(passwordNotification);
    }

    @PostMapping(value="/shield/sharecontent", produces="applictaion/json")
    public @ResponseBody String shareContent(@RequestParam("userName") String userName, @RequestParam("shareEmail") String shareEmail, @RequestParam("documentId") String documentId){
        return uploadService.shareContent(userName,shareEmail,documentId);
    }

    @PostMapping(value="/shield/downloadcontent", produces="application/json")
    public String downloadContent(@RequestParam("userName") String userName, @RequestParam("userContentId") String userContentId)throws IOException {

        return uploadService.downloadContent(userName, userContentId);
    }

        @PostMapping(value="/shield/getplist", produces = "application/json")
        public @ResponseBody String getAllPasswordList(@RequestBody String userName){

            String response="";
            List<PasswordNotification> List= uploadService.getAllPasswordList(userName);
            Gson gson =new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'").create();
            response=gson.toJson(List);
            return response;
        }

    }


