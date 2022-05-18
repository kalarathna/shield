package com.kaladevi.shield.service;

import com.kaladevi.shield.entity.UserDetailsEntity;
import com.kaladevi.shield.model.LoginModel;
import com.kaladevi.shield.model.UserContentDetails;
import com.kaladevi.shield.repositories.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserDetailsRepo userDetailsRepo;


    @Override
    public LoginModel validateLoginCredentials(LoginModel loginModel) {

        String success = "success";
        String failure = "failure";
        String userNotFond = "userNotfound";
        System.out.println(loginModel.getUsername());
        System.out.println(loginModel.getPassword());
        LoginModel returnLoginModel = new LoginModel();
        UserDetailsEntity userDetailsEntity = checkExistingUserByEmail(loginModel.getUsername());
        if (Objects.nonNull(userDetailsEntity)) {
            if (userDetailsEntity.getEmail().equalsIgnoreCase(loginModel.getUsername()) && userDetailsEntity.getPassword().equalsIgnoreCase(loginModel.getPassword())) {
                returnLoginModel.setFirstName(userDetailsEntity.getFirstName());
                returnLoginModel.setLoginUUID(userDetailsEntity.getUserDetailsId());
                returnLoginModel.setUsername(userDetailsEntity.getEmail());
                returnLoginModel.setStatus("Success");

                return returnLoginModel;

            } else {
                returnLoginModel.setStatus("Failure");
                return returnLoginModel;
            }
        }
        returnLoginModel.setStatus("userNotFound");
        return returnLoginModel;
    }

    @Override
    @Transactional
    public LoginModel createUser(UserContentDetails userDetails) {
        LoginModel loginModel = new LoginModel();
        try {
            UserDetailsEntity userDetailsEntity1 = checkExistingUserByEmail(userDetails.getEmail());
            if (Objects.nonNull(userDetailsEntity1)) {
                loginModel.setStatus("UserAvailable");
                return loginModel;
            } else {
                UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
                userDetailsEntity.setFirstName(userDetails.getFirstName());
                userDetailsEntity.setLastName(userDetails.getLastName());
                userDetailsEntity.setEmail(userDetails.getEmail());
                userDetailsEntity.setPassword(userDetails.getPassword());
                userDetailsEntity.setDateOfBirth(userDetails.getDateOfBirth());
                entityManager.persist(userDetailsEntity);
                UserDetailsEntity userDetailsEntity2 = checkExistingUserByEmail(userDetails.getEmail());
                loginModel.setLoginUUID(userDetailsEntity2.getUserDetailsId());
                loginModel.setFirstName(userDetailsEntity2.getFirstName());
                loginModel.setUsername(userDetailsEntity2.getEmail());
                loginModel.setStatus("saved");
                return loginModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        loginModel.setStatus("failure");
        return loginModel;
    }

    public UserDetailsEntity checkExistingUserByEmail(String email){

        UserDetailsEntity userDetailsEntity= userDetailsRepo.findUserDetailsEntityIdByEmail(email);
        return userDetailsEntity;
    }

}



