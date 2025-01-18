package com.modsen.bookstorageservice.controllers;

import com.modsen.bookstorageservice.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "Token Generator", description = "APIs for creating JWT token")
@RestController("/user")
public class AuthorizationController {

    private final TokenService tokenService;

    @Operation(summary = "Creates JWT token")
    @GetMapping("/token/generate")
    public String getToken(@RequestParam String role) {
        return tokenService.generateToken(role);
    }
}
