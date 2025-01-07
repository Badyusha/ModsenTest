package com.modsen.booktrackerservice.controllers;

import com.modsen.booktrackerservice.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/token/generate")
    public String getToken(){
        return tokenService.generateToken();
    }
}
