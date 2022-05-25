package com.kaladevi.shield.service;

import com.kaladevi.shield.entity.PasswordNotificationEntity;
import com.kaladevi.shield.entity.UserContentEntity;
import com.kaladevi.shield.entity.UserDetailsEntity;
import com.kaladevi.shield.model.PasswordNotification;
import com.kaladevi.shield.model.UserContent;
import com.kaladevi.shield.repositories.PasswordNotificationRepo;
import com.kaladevi.shield.repositories.UserContentRepo;
import com.kaladevi.shield.repositories.UserDetailsRepo;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.jni.Local;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.charset.StandardCharsets;
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
            userContentEntity.setUploadFileName(userContent.getUploadFileName());
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
    public UserContent saveText(UserContent userContent){
        UserContent userContent1= new UserContent();
        try {
            byte[] document = userContent.getDocumentContent().getBytes();

            UserContentEntity userContentEntity = new UserContentEntity();
            userContentEntity.setUserDetailsId(userDetailsRepo.findUserDetailsEntityIdByEmail(userContent.getUsername()));
            userContentEntity.setDocumentName(userContent.getDocumentName());
            userContentEntity.setDocumentContent(document);
            userContentRepo.save(userContentEntity);
            userContent1.setDocumentName(userContent.getDocumentName());
            userContent1.setDocumentSize(userContent.getDocumentSize());
            userContent1.setDocumentContent(userContent.getDocumentContent());
            userContent1.setErrorFlg(false);

        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return userContent1;
    }

    public String deleteFile(UserContent userContent){
            return null;
    }

    public List<UserContent> getUserContent(String username){

        List<String> fileNameList= new ArrayList<>();
        List<Long> fileSizeList = new ArrayList<>();
        List<UserContent> userContentList= new ArrayList<>();
        UserContent userContent= new UserContent();
        UserDetailsEntity userDetailsEntity= userDetailsRepo.findUserDetailsEntityIdByEmail(username);
        if(Objects.nonNull(userDetailsEntity)) {
            List<UserContentEntity> userContentEntity = userContentRepo.findAllByUserDetailsIdUserDetailsId(userDetailsEntity.getUserDetailsId());
            if (Objects.nonNull(userContentEntity)) {
                for (UserContentEntity userContents : userContentEntity) {
                    userContent.setUsername(username);
                    userContent.setUserContentID(userContents.getUserContentId());
                    if(Objects.nonNull(userContents.getUploadFileName())){
                        userContent.setUploadFileName(userContents.getUploadFileName());
                        userContent.setFileSize(userContents.getFileSize());
                        fileNameList.add(userContents.getUploadFileName());
                        fileSizeList.add(userContents.getFileSize());
                    }
                    if(Objects.nonNull(userContents.getDocumentName())){
                        userContent.setDocumentName(userContents.getDocumentName());
                        fileNameList.add(userContents.getDocumentName());
                        fileSizeList.add(userContents.getDocumentSize());
                        }
                    userContent.setFileNames(fileNameList);
                    userContent.setFileSizes(fileSizeList);
//                   String p = ServletUriComponentsBuilder.fromCurrentContextPath()
//                            .path("/files/")
//                            .path(userContents.getFiles().toString())
//                            .toUriString();
//                    userContent.setDownloadFile(p);
                }

                userContentList.add(userContent);
            }
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

    @Transactional
    public String shareContent(String userName,String shareEmail,String documentName){
        UserDetailsEntity userDetailsEntity=userDetailsRepo.findUserDetailsEntityIdByEmail(userName);
        UserContentEntity userContentEntity= new UserContentEntity();
        UserContentEntity saveUserContentEntity= new UserContentEntity();
        if(Objects.nonNull(userDetailsEntity)){
            userContentEntity= userContentRepo.findAllByUserDetailsIdAndDocumentName(userDetailsEntity.getUserDetailsId(),documentName);
            if(Objects.nonNull(userContentEntity)){
                UserDetailsEntity userDetailsEntity1= userDetailsRepo.findUserDetailsEntityIdByEmail(shareEmail);
                if(Objects.nonNull(userDetailsEntity1)){
                    saveUserContentEntity.setUserDetailsId(userDetailsRepo.findUserDetailsEntityIdByEmail(shareEmail));
                    saveUserContentEntity.setDocumentName(userContentEntity.getDocumentName());
                    saveUserContentEntity.setDocumentContent(userContentEntity.getDocumentContent());
                    saveUserContentEntity.setFiles(userContentEntity.getFiles());
                    saveUserContentEntity.setUploadFileName(userContentEntity.getUploadFileName());
                    saveUserContentEntity.setFileSize(userContentEntity.getFileSize());
                    userContentRepo.save(saveUserContentEntity);
                    return "success";
                }
            }

        }
        return "success";
    }
    public String downloadContent(String userName, String userContentId) throws IOException{
       UserDetailsEntity userDetailsEntity=userDetailsRepo.findUserDetailsEntityIdByEmail(userName);
       UserContentEntity userContentEntity= new UserContentEntity();
        UUID userContentsId=UUID.fromString(userContentId);
        if(Objects.nonNull(userDetailsEntity)){
            userContentEntity=userContentRepo.findAllByUserContentId(userContentsId);
            if(Objects.nonNull(userContentEntity)){
                if(Objects.nonNull(userContentEntity.getFiles())){

                    InputStream inputStream= new ByteArrayInputStream(userContentEntity.getFiles());
                            //Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
                    if(inputStream==null){
                        throw  new FileNotFoundException("file unavailable");
                    }
                    return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                }
                if(Objects.nonNull(userContentEntity.getDocumentContent())){
                    InputStream inputStream= new ByteArrayInputStream(userContentEntity.getDocumentContent());
                    if(inputStream==null){
                        throw  new FileNotFoundException("file unavailable");
                    }
                    return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                }
            }
        }
        return null;

    }
}
