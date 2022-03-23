package com.kaladevi.shield.entity;


import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONArray;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="User_Content")
public class UserContentEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="Login_Details_id")
    public UUID userContentId;

    @Column(name="files")
    public byte[] files;

}
