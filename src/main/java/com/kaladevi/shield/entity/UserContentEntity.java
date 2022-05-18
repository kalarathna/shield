package com.kaladevi.shield.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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
    @Column(name="user_content_id")
    public UUID userContentId;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserDetailsEntity.class)
    @JoinColumn(name="user_details_id", referencedColumnName = "user_details_id", nullable = false)
    private UserDetailsEntity userDetailsId;

    @Column(name="files")
    public byte[] files;

    @Column(name="document_name")
    public String documentName;

    @Column(name="document_content")
    public String documentContent;

    @Column(name="upload_file_name")
    public String uploadFileName;

    public UserContentEntity() {
        super();
    }

    public UserContentEntity(UUID userContentId, byte[] files) {
        super();
        this.userContentId = userContentId;
        this.files = files;
    }



}
