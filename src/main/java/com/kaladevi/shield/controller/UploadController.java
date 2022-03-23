package com.kaladevi.shield.controller;


import com.kaladevi.shield.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    UploadService uploadService;

    @PostMapping(value = "/uploadfile", produces="application/json")
    public String uploadFiles(@RequestParam("file")MultipartFile multipartFile){
        uploadService.uploadFile(multipartFile);
        return "success";
    }
}
