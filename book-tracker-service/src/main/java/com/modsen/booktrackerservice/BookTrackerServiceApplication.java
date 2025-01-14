package com.modsen.booktrackerservice;

import com.modsen.booktrackerservice.configs.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class BookTrackerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookTrackerServiceApplication.class, args);
    }

}
