package com.modsen.bookstorageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.modsen.commonmodels.models.entities")
public class BookStorageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookStorageServiceApplication.class, args);
    }

}
