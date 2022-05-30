package com.kaladevi.shield.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Date;
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

    @Column(name="document_size")
    public Long documentSize;

    @Column(name="document_name")
    public String documentName;

    @Column(name="document_content")
    public byte[] documentContent;

    @Column(name="is_uploaded")
    public Boolean isUploaded;

    @CreatedDate
    @Column(name="expiry_date")
    public Date expiryDate;

    @CreatedDate
    @Column(name="created_date")
    public Date createdDate;

    @LastModifiedDate
    @Column(name="updated_date")
    public Date updatedDate;

    public UserContentEntity() {
        super();
    }

    public UserContentEntity(UUID userContentId, byte[] documentContent) {
        super();
        this.userContentId = userContentId;
        this.documentContent=documentContent;

    }



}
