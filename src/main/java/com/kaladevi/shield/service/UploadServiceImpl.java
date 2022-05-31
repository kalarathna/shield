package com.kaladevi.shield.service;

import com.kaladevi.shield.entity.PasswordNotificationEntity;
import com.kaladevi.shield.entity.UserContentEntity;
import com.kaladevi.shield.entity.UserDetailsEntity;
import com.kaladevi.shield.model.Files;
import com.kaladevi.shield.model.PasswordNotification;
import com.kaladevi.shield.model.UserContent;
import com.kaladevi.shield.repositories.PasswordNotificationRepo;
import com.kaladevi.shield.repositories.UserContentRepo;
import com.kaladevi.shield.repositories.UserDetailsRepo;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public String uploadFile(MultipartFile  file, String expiryDate, String userName, String documentName) {
        Date d;
        try {
            UserContentEntity userContentEntity = new UserContentEntity();
            byte[] uploadFile = file.getBytes();
            long documentSize=file.getSize();
            userContentEntity.setUserDetailsId(userDetailsRepo.findUserDetailsEntityIdByEmail(userName));
            userContentEntity.setDocumentName(documentName);
            userContentEntity.setDocumentContent(uploadFile);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
            LocalDate localCreateDate = LocalDate.parse(expiryDate.toString(), formatter);
            d=Date.valueOf(localCreateDate);
            userContentEntity.setExpiryDate(d);
            userContentEntity.setDocumentSize(documentSize);
            userContentEntity.setIsUploaded(true);
            userContentRepo.save(userContentEntity);
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return "success";
    }

//    @Override
//    @Transactional
//    public UserContent saveText(UserContent userContent){
//        UserContent userContent1= new UserContent();
//        try {
//            byte[] document = userContent.getDocumentContent().getBytes();
//            long documentSize=userContent.getDocumentContent().getSize();
//
//            UserContentEntity userContentEntity = new UserContentEntity();
//            userContentEntity.setUserDetailsId(userDetailsRepo.findUserDetailsEntityIdByEmail(userContent.getUserName()));
//            userContentEntity.setDocumentName(userContent.getDocumentContent().getName());
//            userContentEntity.setDocumentContent(document);
//            userContentEntity.setExpiryDate(Date.valueOf(userContent.getExpiryDate()));
//            userContentEntity.setDocumentSize(documentSize);
////            if(Objects.nonNull(userContent.getFileExpiryDate())) {
////                Date date = Date.valueOf(userContent.getFileExpiryDate());
////                userContentEntity.setExpiryDate(date);
////            }
//            userContentEntity.setIsUploaded(false);
//            userContentRepo.save(userContentEntity);
//            userContent1.setDocumentName(userContent.getDocumentName());
//            userContent1.setDocumentSize(userContent.getDocumentSize());
//            userContent1.setDocumentContent(userContent.getDocumentContent());
//            userContent1.setErrorFlg(false);
//
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//        }
//        return userContent1;
//    }

    @Transactional
    public String deleteFile(String userContentId){

        UUID userContentID1=UUID.fromString(userContentId);
        if(userContentId!=null){
            UserContentEntity userContentEntity=userContentRepo.findAllByUserContentId(userContentID1);
            if(Objects.nonNull(userContentEntity)){
                userContentRepo.deleteContent(userContentID1);
            }
            return "success";
        }
        else{
            return "failure";
        }
        }

    public UserContent getUserContent(String userName){

        List<Files> fileDetailList= new ArrayList<Files>();

        LocalDate date = null;
        List<UserContent> userContentList= new ArrayList<>();
        Map<String,Long> fileDetailsMap = new HashMap<>();
        UserContent userContent= new UserContent();
        UserDetailsEntity userDetailsEntity= userDetailsRepo.findUserDetailsEntityIdByEmail(userName);
        if(Objects.nonNull(userDetailsEntity)) {
            List<UserContentEntity> userContentEntity = userContentRepo.findAllByUserDetailsIdUserDetailsId(userDetailsEntity.getUserDetailsId());
            if (Objects.nonNull(userContentEntity)) {
                for (UserContentEntity userContents : userContentEntity) {
                    Files files= new Files();
                    userContent.setUserName(userName);
                    if(Objects.nonNull(userContents.getDocumentName())){
                        files.setFileName(userContents.getDocumentName());
                        Long fileSize=userContents.getDocumentSize()/1024;
                        files.setFileSizes(fileSize+" KB");
                        if(Objects.nonNull(userContents.getExpiryDate())){
                            files.setFileExpiryDate(String.format(userContents.getExpiryDate().toString(), "dd-MM-yyyy"));
                        }

                        files.setUserContentId(userContents.getUserContentId().toString());
                    }
                    fileDetailList.add(files);
                }
                userContent.setFileDetails(fileDetailList);

            }
        }
        return userContent;
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
    public String savePasswordExpiryNotification(PasswordNotification passwordNotification ) {
         int daysToAdd = 90;
         Date cd;
         Date d;
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

         LocalDateTime localCreateDate = LocalDateTime.parse(passwordNotification.getCreationDate().toString(), formatter);

         LocalDateTime expiryDate = localCreateDate.plusDays(daysToAdd);
         PasswordNotificationEntity passwordNotificationEntity = new PasswordNotificationEntity();
         UserDetailsEntity userDetailsEntity = userDetailsRepo.findUserDetailsEntityIdByEmail(passwordNotification.getUserName());

         if (Objects.nonNull(userDetailsEntity)) {
            List< PasswordNotificationEntity> existingPasswordNotificationEntity = passwordNotificationRepo.findAllByUserDetailsIdUserDetailsId(userDetailsEntity.getUserDetailsId());
             if (Objects.nonNull(existingPasswordNotificationEntity)) {
                 for(PasswordNotificationEntity passwordNotificationlist:existingPasswordNotificationEntity)

                 if (passwordNotificationlist.getApplicationName().equalsIgnoreCase(passwordNotification.getApplicationName())) {
                     cd = Date.valueOf(localCreateDate.toLocalDate());
                     passwordNotificationEntity.setCreationDate(cd);
                     d = Date.valueOf(expiryDate.toLocalDate());
                     passwordNotificationEntity.setExpriryDate(d);
                     passwordNotificationEntity.setUserDetailsId(passwordNotificationlist.getUserDetailsId());
                     passwordNotificationRepo.save(passwordNotificationEntity);
                 } else {
                     passwordNotificationEntity.setApplicationName(passwordNotification.getApplicationName());
                     cd = Date.valueOf(localCreateDate.toLocalDate());
                     passwordNotificationEntity.setCreationDate(cd);
                     d = Date.valueOf(expiryDate.toLocalDate());
                     passwordNotificationEntity.setExpriryDate(d);
                     passwordNotificationEntity.setUserDetailsId(passwordNotificationlist.getUserDetailsId());
                     passwordNotificationRepo.save(passwordNotificationEntity);
                 }

             }
             passwordNotificationEntity.setApplicationName(passwordNotification.getApplicationName());
             cd = Date.valueOf(localCreateDate.toLocalDate());
             passwordNotificationEntity.setCreationDate(cd);
             d = Date.valueOf(expiryDate.toLocalDate());
             passwordNotificationEntity.setExpriryDate(d);
             passwordNotificationEntity.setUserDetailsId(userDetailsEntity);
             passwordNotificationRepo.save(passwordNotificationEntity);

             return "success";
         } else {
             return "failure";
         }
     }

    @Transactional
    public String shareContent(String userName,String shareEmail,String documentId){
        UserDetailsEntity userDetailsEntity=userDetailsRepo.findUserDetailsEntityIdByEmail(userName);
        UserContentEntity userContentEntity= new UserContentEntity();
        UserContentEntity saveUserContentEntity= new UserContentEntity();
        UUID userContentID=UUID.fromString(documentId);
        if(Objects.nonNull(userDetailsEntity)){
            userContentEntity= userContentRepo.findAllByUserContentId(userContentID);
            if(Objects.nonNull(userContentEntity)){
                UserDetailsEntity userDetailsEntity1= userDetailsRepo.findUserDetailsEntityIdByEmail(shareEmail);
                if(Objects.nonNull(userDetailsEntity1)){
                    saveUserContentEntity.setUserDetailsId(userDetailsRepo.findUserDetailsEntityIdByEmail(shareEmail));
                    saveUserContentEntity.setDocumentName(userContentEntity.getDocumentName());
                    saveUserContentEntity.setDocumentContent(userContentEntity.getDocumentContent());
                    saveUserContentEntity.setDocumentContent(userContentEntity.getDocumentContent());
                    saveUserContentEntity.setDocumentName(userContentEntity.getDocumentName());
                    saveUserContentEntity.setDocumentSize(userContentEntity.getDocumentSize());
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
                if(Objects.nonNull(userContentEntity.getDocumentContent())){


                    InputStream inputStream= new ByteArrayInputStream(userContentEntity.getDocumentContent());
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


    public List<PasswordNotification> getAllPasswordList(String userName){

        List<PasswordNotification> passwordNotificationList= new ArrayList<>();


        UserDetailsEntity userDetailsEntity= userDetailsRepo.findUserDetailsEntityIdByEmail(userName);
        if(Objects.nonNull(userDetailsEntity)) {
            List<PasswordNotificationEntity> passwordNotificationEntityList= passwordNotificationRepo.findAllByUserDetailsIdUserDetailsId(userDetailsEntity.getUserDetailsId());
            if(Objects.nonNull(passwordNotificationEntityList)){

                for(PasswordNotificationEntity passwordNotification :passwordNotificationEntityList){
                    PasswordNotification passwordNotificationData= new PasswordNotification();
                    passwordNotificationData.setApplicationName(passwordNotification.getApplicationName());
                    passwordNotificationData.setCreationDate(passwordNotification.getCreationDate().toString());
                    passwordNotificationData.setExpiryDate(passwordNotification.getExpriryDate().toString());
                    passwordNotificationList.add(passwordNotificationData);
                }

                return passwordNotificationList;
            }
        }

        return passwordNotificationList;


    }
}
