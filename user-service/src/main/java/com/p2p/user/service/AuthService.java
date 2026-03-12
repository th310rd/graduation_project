package com.p2p.user.service;

import com.p2p.user.dto.*;
import com.p2p.user.entity.User;
import com.p2p.user.entity.UserRole;
import com.p2p.user.entity.UserStatus;
import com.p2p.user.exception.BadRequestException;
import com.p2p.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecretKey secretKey;

    @Value("${security.jwt.expiration-seconds:3600}")
    private long expiration;

    public UserResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BadRequestException("Email already in use");
        }
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .dateOfBirth(request.dateOfBirth())
                .passwordHash(passwordEncoder.encode(request.password()))
                .status(UserStatus.ACTIVE)
                .roles(request.roles() == null || request.roles().isEmpty() ? Set.of(UserRole.ROLE_RENTER) : request.roles())
                .build();
        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getFirstName(), saved.getLastName(), saved.getEmail(), saved.getPhoneNumber(), saved.getDateOfBirth(), saved.getStatus(), saved.getCreatedAt(), saved.getUpdatedAt());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new BadRequestException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid credentials");
        }
        Instant now = Instant.now();
        String token = Jwts.builder()
                .subject(user.getId().toString())
                .claim("userId", user.getId().toString())
                .claim("roles", user.getRoles().stream().map(Enum::name).toList())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration, ChronoUnit.SECONDS)))
                .signWith(secretKey)
                .compact();
        return new AuthResponse(token, "Bearer", expiration);
    }
}
