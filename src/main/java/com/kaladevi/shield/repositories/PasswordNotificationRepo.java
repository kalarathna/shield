package com.kaladevi.shield.repositories;

import com.kaladevi.shield.entity.PasswordNotificationEntity;
import com.kaladevi.shield.entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PasswordNotificationRepo extends JpaRepository<PasswordNotificationEntity, Long > {


    public PasswordNotificationEntity findAllByUserDetailsId(UUID userDetailsId);

    public List<PasswordNotificationEntity> findAllByUserDetailsIdUserDetailsId(UUID userDetailsId);
//    @Query("select p from PasswordNotificationEntity p where p.userDetailsId= :userDetailsId")
//    public PasswordNotificationEntity findAllByUserDetailsId(UserDetailsEntity userDetailsEntity);
}
