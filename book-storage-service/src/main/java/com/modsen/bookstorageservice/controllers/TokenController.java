package com.modsen.bookstorageservice.controllers;

import com.modsen.bookstorageservice.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final Logger LOGGER = LoggerFactory.getLogger(TokenController.class);
    private final TokenService tokenService;

    @GetMapping("/token/generate")
    public String getToken(){
        String token = tokenService.generateToken();
        LOGGER.debug("Token granted {}", token);
        return token;
    }
}
