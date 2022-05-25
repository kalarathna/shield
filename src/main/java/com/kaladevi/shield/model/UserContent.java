package com.kaladevi.shield.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserContent {


    private UUID userDetailsId;
    private String username;
    private MultipartFile  file;
    private String uploadFileName;
    private String documentName;
    private String documentContent;
    private String documentSize;
    private String downloadFile;
    private Long fileSize;
    private Boolean errorFlg;
    private UUID userContentID;
    private List<String> fileNames;
    private List<Long> fileSizes;

}
