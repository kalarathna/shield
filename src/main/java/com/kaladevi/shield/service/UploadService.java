package com.kaladevi.shield.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    public String uploadFile(MultipartFile multipartFile);
}
