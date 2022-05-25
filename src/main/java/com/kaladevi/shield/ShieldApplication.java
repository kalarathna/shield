package com.kaladevi.shield;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class }, scanBasePackages = {"com.kaladevi.shield"})
@EntityScan
@EnableJpaAuditing

public class ShieldApplication {



    public static void main(String[] args) {

        SpringApplication.run(ShieldApplication.class, args);
        System.out.println("SHIELD");


    }

}
