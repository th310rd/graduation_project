package com.p2p.listing.service;

import com.p2p.listing.exception.ForbiddenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthContextService {
    private final SecretKey secretKey;

    public UUID extractUserIdFromAuthorization(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ForbiddenException("Missing bearer token");
        }
        String token = authorizationHeader.substring(7);
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        Object userId = claims.get("userId");
        if (userId == null) {
            throw new ForbiddenException("Token does not contain userId");
        }
        return UUID.fromString(userId.toString());
    }
}
