package com.modsen.bookstorageservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtDecoder jwtDecoder;

    private final String TOKEN_PREFIX = "Bearer ";
    private final String USER_ID_CLAIM = "userId";

    public Long extractUserId(String token) {
        String tokenWithoutPrefix = extractTokenPrefix(token);
        Jwt jwt = jwtDecoder.decode(tokenWithoutPrefix);
        return jwt.getClaim(USER_ID_CLAIM);
    }

    private String extractTokenPrefix(String token) {
        return token.substring(TOKEN_PREFIX.length());
    }
}
