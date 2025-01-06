package com.modsen.bookstorageservice.controllers;

import com.modsen.bookstorageservice.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "Token Generator", description = "APIs for creating JWT token")
@RestController
public class TokenController {

    private final Logger LOGGER = LoggerFactory.getLogger(TokenController.class);
    private final TokenService tokenService;

    @Operation(summary = "Creates JWT token")
    @GetMapping("/token/generate")
    public String getToken(){
        String token = tokenService.generateToken();
        LOGGER.debug("Token granted {}", token);
        return token;
    }
}
