package com.kaladevi.shield.service;

import com.kaladevi.shield.entity.PasswordNotificationEntity;
import com.kaladevi.shield.entity.UserContentEntity;
import com.kaladevi.shield.entity.UserDetailsEntity;
import com.kaladevi.shield.model.PasswordNotification;
import com.kaladevi.shield.model.UserContent;
import com.kaladevi.shield.repositories.PasswordNotificationRepo;
import com.kaladevi.shield.repositories.UserContentRepo;
import com.kaladevi.shield.repositories.UserDetailsRepo;
import org.apache.tomcat.jni.Local;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class UploadServiceImpl implements UploadService{

    //@Autowired
   // EntityManager entityManager;

    @Autowired
    UserDetailsRepo userDetailsRepo;

    @Autowired
    UserContentRepo userContentRepo;



    @Autowired
    PasswordNotificationRepo passwordNotificationRepo;


    @Override
    @Transactional
    public String uploadFile(UserContent userContent) {
        try {
            byte[] file = userContent.getFile().getBytes();
            UserContentEntity userContentEntity = new UserContentEntity();
            userContentEntity.setUserDetailsId(userDetailsRepo.findUserDetailsEntityIdByEmail(userContent.getUsername()));
            userContentEntity.setFiles(file);
            userContentRepo.save(userContentEntity);
        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return "success";
    }

    @Override
    @Transactional
    public String saveText(UserContent userContent){
        try {
            UserContentEntity userContentEntity = new UserContentEntity();
            userContentEntity.setUserDetailsId(userDetailsRepo.findUserDetailsEntityIdByEmail(userContent.getUsername()));
            userContentEntity.setDocumentName(userContent.getDocumentName());
            userContentEntity.setDocumentContent(userContent.getDocumentContent());
            userContentRepo.save(userContentEntity);
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return "success";
    }

    public String deleteFile(UserContent userContent){
            return null;
    }

    public List<UserContent> getUserContent(String username){

        List<UserContent> userContentList= new ArrayList<>();
        UserContent userContent= new UserContent();
        UserDetailsEntity userDetailsEntity= userDetailsRepo.findUserDetailsEntityIdByEmail(username);

            List<UserContentEntity> userContentEntity= userContentRepo.findAllByUserDetailsId(userDetailsEntity.getUserDetailsId());
            if(Objects.nonNull(userContentEntity)) {
                for (UserContentEntity userContents:userContentEntity) {
                    userContent.setDocumentName(userContents.getDocumentName());
                    userContent.setDocumentContent(userContents.getDocumentContent());
                    userContent.setUploadFileName(userContents.getUploadFileName());
                    userContent.setFileSize(userContents.getFileSize());
                    String p= ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/files/")
                            .path(userContents.getFiles().toString())
                            .toUriString();
                    userContent.setDownloadFile(p);
                }  userContentList.add(userContent);
              //  String path = this.getClass().getClassLoader().getResource("/file").toString();
//                try {
////                    FileOutputStream fileOutputStream= new FileOutputStream(path);
////                    fileOutputStream.write(userContentEntity.getFiles());
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
            }
        return userContentList;
    }

     public String sendPasswordExpiryNotifictaion(String username){
         String firstName="";
         LocalDate expiryDate;
         EmailServiceImpl emailService= new EmailServiceImpl();
         UserDetailsEntity userDetailsEntity= userDetailsRepo.findUserDetailsEntityIdByEmail(username);
         if(Objects.nonNull(userDetailsEntity)){
              firstName= userDetailsEntity.getFirstName();
              PasswordNotificationEntity passwordNotificationEntity=passwordNotificationRepo.findAllByUserDetailsId(userDetailsEntity.getUserDetailsId());
              if(Objects.nonNull(passwordNotificationEntity)){
                  expiryDate= passwordNotificationEntity.getExpriryDate().toLocalDate();
                  LocalDate today= LocalDate.now();
                  Period emailSendDate=Period.between(expiryDate, today);
                  if(emailSendDate.getDays()<5){
                      emailService.sendEmail(username,firstName,passwordNotificationEntity.getExpriryDate());
                  }

              }
         }

        return "success";
     }


    public String sendPasswordExpiryNotification(PasswordNotification passwordNotification ){
        int daysToAdd=90;
        Date cd;
        Date d;
        LocalDate expiryDate;
        PasswordNotificationEntity passwordNotificationEntity= new PasswordNotificationEntity();
        PasswordNotificationEntity existingPasswordNotificationEntity=passwordNotificationRepo.findAllByUserDetailsId(passwordNotification.getUserID());
        if(Objects.nonNull(existingPasswordNotificationEntity)){
            cd=Date.valueOf(passwordNotification.getCreationDate());
            passwordNotificationEntity.setCreationDate(cd);
            expiryDate=passwordNotificationEntity.getCreationDate().toLocalDate().plusDays(daysToAdd);
            d= Date.valueOf(expiryDate);
            passwordNotificationEntity.setExpriryDate(d);
            passwordNotificationRepo.save(passwordNotificationEntity);
        }
        passwordNotificationEntity.setApplicationName(passwordNotification.getApplicationName());
        cd=Date.valueOf(passwordNotification.getCreationDate());
        passwordNotificationEntity.setCreationDate(cd);
        expiryDate=passwordNotificationEntity.getCreationDate().toLocalDate().plusDays(daysToAdd);
        d= Date.valueOf(expiryDate);
        passwordNotificationEntity.setExpriryDate(d);
        passwordNotificationRepo.save(passwordNotificationEntity);

        return "success";
    }
}
