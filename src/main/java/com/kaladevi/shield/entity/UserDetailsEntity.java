package com.kaladevi.shield.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name="User_Details")
public class UserDetailsEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="user_details_id", nullable = false)
    public UUID userDetailsId;

    @Column(name="first_name", nullable = false)
    public String firstName;

    @Column(name="last_name", nullable = false)
    public String lastName;

    @Column(name="email",  nullable = false)
    public String email;

    @Column(name="password", nullable = false)
    public String password;

    @Column(name="organisation_name")
    public String organisationName ;

    @OneToMany(mappedBy = "userDetailsId")
    private List<UserContentEntity> userContentEntityList;

    @OneToMany(mappedBy = "userDetailsId")
    private List<PasswordNotificationEntity> passwordNotificationEntityList;




}
