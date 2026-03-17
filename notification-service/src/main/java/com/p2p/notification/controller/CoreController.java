package com.p2p.notification.controller;

import com.p2p.notification.dto.CreateRequest;
import com.p2p.notification.dto.ResponseDto;
import com.p2p.notification.service.CoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class CoreController {
    private final CoreService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto notify(@Valid @RequestBody CreateRequest request) { return service.create(request); }
}
