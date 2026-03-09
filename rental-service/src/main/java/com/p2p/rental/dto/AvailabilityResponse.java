package com.p2p.rental.dto;

import java.util.List;

public record AvailabilityResponse(boolean available, List<String> reasons) {}
