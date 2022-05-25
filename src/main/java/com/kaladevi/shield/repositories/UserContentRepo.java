package com.kaladevi.shield.repositories;

import com.kaladevi.shield.entity.UserContentEntity;
import com.kaladevi.shield.model.UserContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserContentRepo extends JpaRepository<UserContentEntity, Long> {

    public List<UserContentEntity> findAllByUserDetailsIdUserDetailsId(UUID userDetailsID);

    @Query(value ="select user_content_id,user_details_id,files, document_name,document_content,upload_file_name,file_size, created_date, updated_date from user_content u where u.user_details_id= :userDetailsId and u.document_name=:documentName or u.upload_file_name=:documentName", nativeQuery = true)
    public UserContentEntity findAllByUserDetailsIdAndDocumentName(@Param("userDetailsId")UUID userDetailsId, @Param("documentName")String documentName);

    @Query(value = "select u from UserContentEntity  u where u.userContentId=:userContentId")
    public UserContentEntity findAllByUserContentId(UUID userContentId);


}
