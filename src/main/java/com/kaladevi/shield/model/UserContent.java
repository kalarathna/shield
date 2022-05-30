package com.kaladevi.shield.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class UserContent {


    private UUID userDetailsId;
    private String userName;
    private MultipartFile  file;
    private String documentName;
    private MultipartFile documentContent;
    private String documentSize;
    private String downloadFile;
    private Long fileSize;
    private Boolean errorFlg;
    private UUID userContentID;
    private String expiryDate;
    private List<Files> fileDetails;
   // private List<Long> fileSizes;
   // private Map<String,Long> FileDetails;

}
