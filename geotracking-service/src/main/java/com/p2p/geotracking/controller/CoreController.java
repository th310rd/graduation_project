package com.p2p.geotracking.controller;

import com.p2p.geotracking.dto.CreateRequest;
import com.p2p.geotracking.dto.ResponseDto;
import com.p2p.geotracking.service.CoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tracking")
public class CoreController {
    private final CoreService service;

    @PostMapping("/telemetry")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto telemetry(@Valid @RequestBody CreateRequest request) { return service.create(request); }

    @GetMapping("/vehicles/{vehicleId}")
    public String locations(@PathVariable UUID vehicleId) { return "[]"; }
}
