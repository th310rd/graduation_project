package com.p2p.rental.controller;

import com.p2p.rental.dto.*;
import com.p2p.rental.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rentals")
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentalResponse create(@Valid @RequestBody CreateRentalRequest request) {
        return rentalService.create(request);
    }

    @PutMapping("/{id}/confirm")
    public RentalResponse confirm(@PathVariable UUID id) {
        return rentalService.confirm(id);
    }

    @PutMapping("/{id}/activate")
    public RentalResponse activate(@PathVariable UUID id, @Valid @RequestBody ActivateRentalRequest request) {
        return rentalService.activate(id, request);
    }

    @PutMapping("/{id}/complete")
    public RentalResponse complete(@PathVariable UUID id, @Valid @RequestBody CompleteRentalRequest request) {
        return rentalService.complete(id, request);
    }

    @GetMapping("/{id}")
    public RentalResponse get(@PathVariable UUID id) {
        return rentalService.get(id);
    }
}
