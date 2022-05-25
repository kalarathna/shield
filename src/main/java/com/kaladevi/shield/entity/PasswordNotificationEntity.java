package com.kaladevi.shield.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name="password_notification")
@Getter
@Setter
public class PasswordNotificationEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="password_notifictaion_id")
    public UUID passwordNotificationId;

    @Column(name="application_name")
    public String applicationName;

    @Column(name="creation_date")
    public Date creationDate;

    @Column(name="expiry_date")
    public Date expriryDate;

    @Column(name="email_status")
    public String emailStatus;

    @CreatedDate
    @Column(name="created_date")
    public Date createdDate;

    @LastModifiedDate
    @Column(name="updated_date")
    public Date updatedDate;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserDetailsEntity.class)
    @JoinColumn(name="user_details_id", referencedColumnName = "user_details_id", nullable = false)
    private UserDetailsEntity userDetailsId;
}
