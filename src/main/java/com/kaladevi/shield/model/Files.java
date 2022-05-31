package com.kaladevi.shield.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class Files {

    private String fileName;
    private String fileSizes;
    private String fileExpiryDate;
    private String userContentId;
}
