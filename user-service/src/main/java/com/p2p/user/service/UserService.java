package com.p2p.user.service;

import com.p2p.user.dto.*;
import com.p2p.user.entity.DriverLicense;
import com.p2p.user.entity.User;
import com.p2p.user.entity.UserStatus;
import com.p2p.user.exception.BadRequestException;
import com.p2p.user.exception.NotFoundException;
import com.p2p.user.repository.DriverLicenseRepository;
import com.p2p.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DriverLicenseRepository driverLicenseRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(CreateUserRequest request) {
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .dateOfBirth(request.dateOfBirth())
                .passwordHash(passwordEncoder.encode("changeme"))
                .status(request.status() == null ? UserStatus.ACTIVE : request.status())
                .roles(java.util.Set.of(com.p2p.user.entity.UserRole.ROLE_RENTER))
                .build();
        return map(userRepository.save(user));
    }

    public UserResponse getById(UUID userId) {
        return map(findUser(userId));
    }

    public void addLicense(UUID userId, AddDriverLicenseRequest request) {
        findUser(userId);
        if (driverLicenseRepository.findByUserId(userId).isPresent()) {
            throw new BadRequestException("Driver license already exists for user");
        }
        DriverLicense license = DriverLicense.builder()
                .userId(userId)
                .licenseNumber(request.licenseNumber())
                .category(request.category())
                .issuedDate(request.issuedDate())
                .expiryDate(request.expiryDate())
                .issuedBy(request.issuedBy())
                .countryCode(request.countryCode())
                .build();
        driverLicenseRepository.save(license);
    }

    public EligibilityResponse eligibility(UUID userId) {
        User user = findUser(userId);
        List<String> reasons = new ArrayList<>();
        LocalDate today = LocalDate.now();

        if (user.getStatus() != UserStatus.ACTIVE) reasons.add("User status is not ACTIVE");
        if (Period.between(user.getDateOfBirth(), today).getYears() < 21) reasons.add("User age must be at least 21");

        DriverLicense license = driverLicenseRepository.findByUserId(userId).orElse(null);
        if (license == null) {
            reasons.add("Driver license not found");
        } else {
            if (!license.getCategory().toUpperCase().contains("B")) reasons.add("Driver license category must include B");
            if (!license.getExpiryDate().isAfter(today)) reasons.add("Driver license is expired");
            if (license.getIssuedDate().isAfter(today.minusYears(1))) reasons.add("Driver license must be issued at least 1 year ago");
        }
        return new EligibilityResponse(reasons.isEmpty(), reasons);
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private UserResponse map(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(),
                user.getDateOfBirth(), user.getStatus(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
