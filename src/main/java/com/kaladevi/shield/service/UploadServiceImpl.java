package com.kaladevi.shield.service;

import com.kaladevi.shield.entity.UserContentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;

@Service
public class UploadServiceImpl implements UploadService{

    @Autowired
    EntityManager entityManager;


    @Override
    public String uploadFile(MultipartFile multipartFile) {
        try {
            byte[] file = multipartFile.getBytes();
            UserContentEntity userContent = new UserContentEntity();
            userContent.setFiles(file);
            entityManager.persist(userContent);
        }
        catch(IOException e){

        }


        return null;
    }
}
