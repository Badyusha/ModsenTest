package com.modsen.booktrackerservice.controllers;

import com.modsen.booktrackerservice.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final TokenService tokenService;

    @GetMapping("/generate-token")
    public String getToken(){
        String token = tokenService.generateToken();
        LOGGER.debug("Token granted {}", token);
        return token;
    }
}
