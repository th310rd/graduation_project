package com.p2p.user.dto;

public record AuthResponse(String accessToken, String tokenType, long expiresIn) {}
