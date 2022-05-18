package com.kaladevi.shield.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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

    @Column(name="expiry_date")
    public Date expriryDate;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserDetailsEntity.class)
    @JoinColumn(name="user_details_id", referencedColumnName = "user_details_id", nullable = false)
    private UserDetailsEntity userDetailsId;
}
