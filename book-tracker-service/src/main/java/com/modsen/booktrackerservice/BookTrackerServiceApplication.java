package com.modsen.booktrackerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.modsen.commonmodels.models.entities")
public class BookTrackerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookTrackerServiceApplication.class, args);
    }

}
