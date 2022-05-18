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
import org.springframework.mail.javamail.JavaMailSender;
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
import java.time.format.DateTimeFormatter;
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
    EmailServiceImpl emailService;


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

     public String sendPasswordExpiryNotification(String username){
         String firstName="";
         LocalDate expiryDate;

         UserDetailsEntity userDetailsEntity= userDetailsRepo.findUserDetailsEntityIdByEmail(username);
         if(Objects.nonNull(userDetailsEntity)){
              firstName= userDetailsEntity.getFirstName();
              List<PasswordNotificationEntity> passwordNotificationEntity=passwordNotificationRepo.findAllByUserDetailsIdUserDetailsId(userDetailsEntity.getUserDetailsId());
              if(Objects.nonNull(passwordNotificationEntity)){
                  if(passwordNotificationEntity.size()>0) {
                      for (PasswordNotificationEntity passwordNotification1 : passwordNotificationEntity) {
                    expiryDate=passwordNotification1.getExpriryDate().toLocalDate();
                    LocalDate today = LocalDate.now();
                    Period emailSendDate=Period.between(expiryDate, today);
                    if(emailSendDate.getDays()<5){
                              emailService.sendEmail(username,firstName,passwordNotification1.getExpriryDate().toLocalDate());

                          }
                      }
                  }
                  return "success";
              }
         }

        return "success";
     }


     @Transactional
    public String savePasswordExpiryNotification(PasswordNotification passwordNotification ){
        int daysToAdd=90;
        Date cd;
        Date d;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localCreateDate = LocalDate.parse(passwordNotification.getCreationDate(), formatter);

        LocalDate  expiryDate = localCreateDate.plusDays(daysToAdd);
        PasswordNotificationEntity passwordNotificationEntity= new PasswordNotificationEntity();
        PasswordNotificationEntity existingPasswordNotificationEntity=passwordNotificationRepo.findAllByUserDetailsId(passwordNotification.getUserID());
        if(Objects.nonNull(existingPasswordNotificationEntity)){
            if(existingPasswordNotificationEntity.getApplicationName().equalsIgnoreCase(passwordNotification.getApplicationName())) {
                cd = Date.valueOf(localCreateDate);
                passwordNotificationEntity.setCreationDate(cd);
                d = Date.valueOf(expiryDate);
                passwordNotificationEntity.setExpriryDate(d);
                passwordNotificationEntity.setUserDetailsId(existingPasswordNotificationEntity.getUserDetailsId());
                passwordNotificationRepo.save(passwordNotificationEntity);
            }
            else{
                passwordNotificationEntity.setApplicationName(passwordNotification.getApplicationName());
                cd=Date.valueOf(localCreateDate);
                passwordNotificationEntity.setCreationDate(cd);
                d= Date.valueOf(expiryDate);
                passwordNotificationEntity.setExpriryDate(d);
                passwordNotificationEntity.setUserDetailsId(existingPasswordNotificationEntity.getUserDetailsId());
                passwordNotificationRepo.save(passwordNotificationEntity);
            }
        }
        UserDetailsEntity userDetailsEntity=userDetailsRepo.findUserDetailsEntityIdByEmail(passwordNotification.getUserName());
        passwordNotificationEntity.setApplicationName(passwordNotification.getApplicationName());
        cd=Date.valueOf(localCreateDate);
        passwordNotificationEntity.setCreationDate(cd);
        d= Date.valueOf(expiryDate);
        passwordNotificationEntity.setExpriryDate(d);
        passwordNotificationEntity.setUserDetailsId(userDetailsEntity);
        passwordNotificationRepo.save(passwordNotificationEntity);

        return "success";
    }
}
