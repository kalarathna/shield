package com.kaladevi.shield.entity;

import com.kaladevi.shield.model.Role;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Set;
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

    @Column(name="secret_key")
    public String secretKey ;

    @Column(name="enable_MFA")
    public Boolean enableMFA ;

    @CreatedDate
    @Column(name="created_date")
    public Date createdDate;

    @LastModifiedDate
    @Column(name="updated_date")
    public Date updatedDate;


    @OneToMany(mappedBy = "userDetailsId")
    private List<UserContentEntity> userContentEntityList;

    @OneToMany(mappedBy = "userDetailsId")
    private List<PasswordNotificationEntity> passwordNotificationEntityList;

















}
