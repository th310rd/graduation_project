package com.p2p.user.controller;

import com.p2p.user.dto.*;
import com.p2p.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody CreateUserRequest request) {
        return userService.register(request);
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @PostMapping("/{id}/license")
    @ResponseStatus(HttpStatus.CREATED)
    public void addLicense(@PathVariable UUID id, @Valid @RequestBody AddDriverLicenseRequest request) {
        userService.addLicense(id, request);
    }

    @GetMapping("/{id}/eligibility")
    public EligibilityResponse eligibility(@PathVariable UUID id) {
        return userService.eligibility(id);
    }
}
