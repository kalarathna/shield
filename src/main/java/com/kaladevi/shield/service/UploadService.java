package com.kaladevi.shield.service;

import com.kaladevi.shield.model.PasswordNotification;
import com.kaladevi.shield.model.UserContent;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface UploadService {

    public String uploadFile(MultipartFile file, String expiryDate, String userName, String documentName);
   // public UserContent saveText(UserContent userContent);
    public String deleteFile(String userContentId);
    public UserContent getUserContent(String username);
    public String sendPasswordExpiryNotification(String username);
    public String savePasswordExpiryNotification(PasswordNotification passwordNotification );

    public String shareContent(String userName,String shareEmail,String documentId);
    public List<PasswordNotification> getAllPasswordList(String userName);

    public String downloadContent(String userName, String userContentId) throws IOException;

}
