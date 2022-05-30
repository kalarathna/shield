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
    public String uploadFile(MultipartFile  file, String expiryDate, String userName) {
        try {
            UserContentEntity userContentEntity = new UserContentEntity();
            byte[] uploadFile = file.getBytes();
            long documentSize=file.getSize();
            userContentEntity.setUserDetailsId(userDetailsRepo.findUserDetailsEntityIdByEmail(userName));
            userContentEntity.setDocumentName(file.getName());
            userContentEntity.setDocumentContent(uploadFile);
            SimpleDateFormat format= new SimpleDateFormat("dd-MM-yyyy");
            DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("dd-MM-yyyy");
//            if(Objects.nonNull(userContent.getFileExpiryDate())) {
//                Date date = Date.valueOf(dateTimeFormatter.parse(userContent.getFileExpiryDate()).toString());
//                userContentEntity.setExpiryDate(date);
//            }

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

    @Override
    @Transactional
    public UserContent saveText(UserContent userContent){
        UserContent userContent1= new UserContent();
        try {
            byte[] document = userContent.getDocumentContent().getBytes();
            long documentSize=userContent.getDocumentContent().getSize();

            UserContentEntity userContentEntity = new UserContentEntity();
            userContentEntity.setUserDetailsId(userDetailsRepo.findUserDetailsEntityIdByEmail(userContent.getUserName()));
            userContentEntity.setDocumentName(userContent.getDocumentContent().getName());
            userContentEntity.setDocumentContent(document);
            userContentEntity.setExpiryDate(Date.valueOf(userContent.getExpiryDate()));
            userContentEntity.setDocumentSize(documentSize);
//            if(Objects.nonNull(userContent.getFileExpiryDate())) {
//                Date date = Date.valueOf(userContent.getFileExpiryDate());
//                userContentEntity.setExpiryDate(date);
//            }
            userContentEntity.setIsUploaded(false);
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
                        if(Objects.nonNull(userContents.getExpiryDate())) {
                            date = userContents.getExpiryDate().toLocalDate();
                        }

                        files.setFileName(userContents.getDocumentName());
                        files.setFileSizes(userContents.getDocumentSize());
                        files.setFileExpiryDate(String.format(date.toString(), "dd-MM-yyyy"));
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
}
