package com.kaladevi.shield.repositories;

import com.kaladevi.shield.entity.PasswordNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PasswordNotificationRepo extends JpaRepository<PasswordNotificationEntity, Long > {

    public PasswordNotificationEntity findAllByUserDetailsId(UUID userDetailsId);
}
