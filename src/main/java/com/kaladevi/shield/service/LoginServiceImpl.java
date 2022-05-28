package com.kaladevi.shield.service;

import com.kaladevi.shield.entity.UserDetailsEntity;
import com.kaladevi.shield.model.LoginModel;
import com.kaladevi.shield.model.UserContentDetails;

import com.kaladevi.shield.repositories.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserDetailsRepo userDetailsRepo;

    @Autowired
    private TotpManager totpManager;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

      @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public LoginModel validateLoginCredentials(LoginModel loginModel) {

        String success = "success";
        String failure = "failure";
        String userNotFond = "userNotfound";
        loginModel.setUserName("kalarathna29@gmail.com");

        System.out.println(loginModel.getUserName());
        System.out.println(loginModel.getPassword());
        LoginModel returnLoginModel = new LoginModel();
        UserDetailsEntity userDetailsEntity = checkExistingUserByEmail(loginModel.getUserName().trim().toLowerCase(Locale.ROOT));
        if (Objects.nonNull(userDetailsEntity)) {
            if (userDetailsEntity.getEmail().equalsIgnoreCase(loginModel.getUserName().trim()) && userDetailsEntity.getPassword().equalsIgnoreCase(loginModel.getPassword().trim())) {
                returnLoginModel.setFirstName(userDetailsEntity.getFirstName());
                returnLoginModel.setLoginUUID(userDetailsEntity.getUserDetailsId());
                returnLoginModel.setUserName(userDetailsEntity.getEmail());
                returnLoginModel.setStatus("Success");
                if(userDetailsEntity.getEnableMFA()){

                    returnLoginModel.setSecretImageURI(totpManager.getUriForImage(totpManager.generateSecret()));
                    System.out.println(returnLoginModel.getSecretImageURI());

                }
                returnLoginModel.setErrorFlg(false);
                return returnLoginModel;

            } else {
                returnLoginModel.setStatus("Failure");
                returnLoginModel.setErrorFlg(true);
                return returnLoginModel;
            }
        }
        returnLoginModel.setStatus("userNotFound");
        returnLoginModel.setErrorFlg(true);
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
                userDetailsEntity.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                entityManager.persist(userDetailsEntity);
                UserDetailsEntity userDetailsEntity2 = checkExistingUserByEmail(userDetails.getEmail());
                loginModel.setLoginUUID(userDetailsEntity2.getUserDetailsId());
                loginModel.setFirstName(userDetailsEntity2.getFirstName());
                loginModel.setUserName(userDetailsEntity2.getEmail());
                loginModel.setStatus("saved");
                loginModel.setErrorFlg(false);
                userDetails.setEnableMFA(true);
                if (userDetails.getEnableMFA()) {
                    userDetailsEntity.setSecretKey(totpManager.generateSecret());
                    loginModel.setSecretImageURI(totpManager.getUriForImage(totpManager.generateSecret()));

                }
                return loginModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        loginModel.setStatus("failure");
        loginModel.setErrorFlg(true);
        return loginModel;
    }

    public UserDetailsEntity checkExistingUserByEmail(String email) {
        UserDetailsEntity userDetailsEntity = userDetailsRepo.findUserDetailsEntityIdByEmail(email);
        return userDetailsEntity;
    }

    public LoginModel verifyOTP(String userName, String code) {
        String token=verify(userName,code);
        LoginModel returnLoginModel = new LoginModel();
        UserDetailsEntity userDetailsEntity = checkExistingUserByEmail(userName.trim().toLowerCase(Locale.ROOT));
        if (Objects.nonNull(userDetailsEntity)) {
            returnLoginModel.setFirstName(userDetailsEntity.getFirstName());
            returnLoginModel.setLoginUUID(userDetailsEntity.getUserDetailsId());
            returnLoginModel.setUserName(userDetailsEntity.getEmail());
            returnLoginModel.setStatus("Success");
            returnLoginModel.setJwtToken(token);
            return returnLoginModel;
        }
        returnLoginModel.setStatus("userNotFound");
        return returnLoginModel;


    }
//
    public String verify(String userName, String code) {
        LoginModel loginModel = new LoginModel();
        UserDetailsEntity userDetailsEntity = checkExistingUserByEmail(userName.trim().toLowerCase(Locale.ROOT));
        try {

            if (Objects.nonNull(userDetailsEntity)) {

                if (!totpManager.verifyCode(code, userDetailsEntity.getSecretKey())) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());

        }
        Authentication authentication=  new UsernamePasswordAuthenticationToken(userName, null);
//        final UserDetails userDetails = jwtInMemoryUserDsetailsService
//                .loadUserByUsername(userName);

        String token = jwtTokenManager.generateToken(authentication);

        return token;

    }

}



