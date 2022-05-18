package com.kaladevi.shield.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class PasswordNotification {

    public String username;
    public UUID userID;
    public String applicationName;
    public LocalDate creationDate;
    public LocalDate expiryDate;
    public Boolean notificationStatus;

}
