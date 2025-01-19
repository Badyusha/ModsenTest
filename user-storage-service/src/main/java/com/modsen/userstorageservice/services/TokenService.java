package com.modsen.userstorageservice.services;

import com.modsen.commonmodels.Constants;
import com.modsen.userstorageservice.models.dtos.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public String generateToken(UserDTO userDTO) {
        String role = userDTO.getRole().name();
        if(!isRoleExist(role)) {
            return "Incorrect role name! Available: " + Arrays.toString(Constants.ROLES);
        }

        Long userId = userDTO.getId();
        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(3, ChronoUnit.HOURS))
                .claim("roles", role)
                .claim("userId", userId)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    private boolean isRoleExist(String role) {
        return Arrays.asList(Constants.ROLES).contains(role);
    }
}
