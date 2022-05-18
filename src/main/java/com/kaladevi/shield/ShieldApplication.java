package com.kaladevi.shield;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EntityScan
public class ShieldApplication {



    public static void main(String[] args) {

        SpringApplication.run(ShieldApplication.class, args);
        System.out.println("SHIELD");


    }

}
