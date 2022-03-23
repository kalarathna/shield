package com.kaladevi.shield.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "Login_Details")
public class LoginEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="Login_Details_id")
    public UUID loginDetailsId;

    @Column(name="Username", nullable = false)
    public String userName;

    @Column(name="password", nullable = false)
    public String password;
}
