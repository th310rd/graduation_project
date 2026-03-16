package com.p2p.payment.controller;

import com.p2p.payment.dto.CreateRequest;
import com.p2p.payment.dto.ResponseDto;
import com.p2p.payment.service.CoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class CoreController {
    private final CoreService service;

    @PostMapping("/rental")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto rental(@Valid @RequestBody CreateRequest request) { return service.create(request); }

    @PostMapping("/refund")
    public ResponseDto refund(@Valid @RequestBody CreateRequest request) { return service.create(request); }

    @GetMapping("/{id}")
    public ResponseDto get(@PathVariable UUID id) { return service.get(id); }
}
