package com.p2p.listing.controller;

import com.p2p.listing.dto.CreateRequest;
import com.p2p.listing.dto.ResponseDto;
import com.p2p.listing.service.CoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/listings")
public class CoreController {
    private final CoreService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto create(@Valid @RequestBody CreateRequest request) { return service.create(request); }

    @GetMapping("/{id}")
    public ResponseDto get(@PathVariable UUID id) { return service.get(id); }

    @GetMapping
    public String list() { return "[]"; }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String city,
                         @RequestParam(required = false) Integer minPrice,
                         @RequestParam(required = false) Integer maxPrice,
                         @RequestParam(required = false) String vehicleType) {
        return "[]";
    }
}
