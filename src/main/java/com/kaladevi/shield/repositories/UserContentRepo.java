package com.kaladevi.shield.repositories;

import com.kaladevi.shield.entity.UserContentEntity;
import com.kaladevi.shield.model.UserContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserContentRepo extends JpaRepository<UserContentEntity, Long> {

    public List<UserContentEntity> findAllByUserDetailsId(UUID userDetailsID);


}
