package com.modsen.userstorage.controllers;

import com.modsen.commonmodels.exceptions.ValidationException;
import com.modsen.userstorage.models.dtos.UserDTO;
import com.modsen.userstorage.services.AuthorizationService;
import com.modsen.userstorage.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "User Authorization", description = "APIs for authorizing user")
@RestController
@RequestMapping("/user")
public class AuthorizationController {

    private final TokenService tokenService;
    private final AuthorizationService authorizationService;

    @Operation(summary = "Authorize user")
    @PostMapping("/authorization")
    public String authorize(@RequestBody UserDTO userDTO) {
        UserDTO requestedUserDTO = authorizationService.authorizeUser(userDTO);
        return tokenService.generateToken(requestedUserDTO);
    }

    @Operation(summary = "Register user")
    @PostMapping("/registration")
    public String register(@RequestBody UserDTO userDTO) {
        try {
            UserDTO requestedUserDTO = authorizationService.registerUser(userDTO);
            return tokenService.generateToken(requestedUserDTO);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
