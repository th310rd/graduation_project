package com.p2p.rental.dto;

import java.util.List;

public record EligibilityResponse(boolean eligible, List<String> reasons) {}
