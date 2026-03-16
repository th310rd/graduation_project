package com.p2p.signature.controller;

import com.p2p.signature.dto.CreateRequest;
import com.p2p.signature.dto.ResponseDto;
import com.p2p.signature.service.CoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contracts")
public class CoreController {
    private final CoreService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto generate(@Valid @RequestBody CreateRequest request) { return service.create(request); }

    @PostMapping("/{id}/sign")
    public String sign(@PathVariable String id) { return "SIGNED:" + id; }

    @PostMapping("/{id}/verify")
    public String verify(@PathVariable String id) { return "VERIFIED:" + id; }
}
