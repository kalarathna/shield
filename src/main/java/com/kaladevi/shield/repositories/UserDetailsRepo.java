package com.kaladevi.shield.repositories;

import com.kaladevi.shield.entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetailsEntity, Long >{

    @Query("select u from UserDetailsEntity u where u.email= :email")
    public UserDetailsEntity findUserDetailsEntityIdByEmail(@Param("email") String email);



}
