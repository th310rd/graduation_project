package com.p2p.vehicle.dto;

import java.util.List;

public record AvailabilityResponse(boolean available, List<String> reasons) {}
