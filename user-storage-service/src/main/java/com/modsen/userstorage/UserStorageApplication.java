package com.modsen.userstorage;

import com.modsen.userstorage.configs.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
//@EntityScan(basePackages = "com.modsen.commonmodels.models.entities")
public class UserStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserStorageApplication.class, args);
	}

}
