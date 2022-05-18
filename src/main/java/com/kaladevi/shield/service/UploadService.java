package com.kaladevi.shield.service;

import com.kaladevi.shield.model.PasswordNotification;
import com.kaladevi.shield.model.UserContent;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface UploadService {

    public String uploadFile(UserContent userContent);
    public String saveText(UserContent userContent);
    public String deleteFile(UserContent userContent);
    public List<UserContent> getUserContent(String username);
    public String sendPasswordExpiryNotifictaion(String username);
    public String sendPasswordExpiryNotification(PasswordNotification passwordNotification );

}
