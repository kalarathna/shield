package com.kaladevi.shield.service;

import com.kaladevi.shield.entity.UserDetailsEntity;
import com.kaladevi.shield.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;

@Service
public class LoginServiceImpl implements LoginService{

    @Autowired
    EntityManager entityManager;

    @Override
    public String validateLoginCredentials(String username, String password) {
        UserDetailsEntity  userDetailsEntity=  new UserDetailsEntity();
        String success="success";
        String failure="failure";
        if(userDetailsEntity.getEmail().equals(username) && userDetailsEntity.getPassword().equals(password)){
            return success;
        }
        else
            return failure;
            }

    @Override
    @Transactional
    public String createUser(UserDetails userDetails) {
        try{

            UserDetailsEntity  userDetailsEntity=  new UserDetailsEntity();
            userDetailsEntity.setFirstName(userDetails.getFirstName());
            userDetailsEntity.setLastName(userDetails.getLastName());
            userDetailsEntity.setEmail(userDetails.getEmail());
            userDetailsEntity.setPassword(userDetails.getPassword());
            userDetailsEntity.setDateOfBirth(userDetails.getDateOfBirth());
            entityManager.persist(userDetailsEntity);
        }
        catch(Exception e){

        }

        return "null";
    }
}
