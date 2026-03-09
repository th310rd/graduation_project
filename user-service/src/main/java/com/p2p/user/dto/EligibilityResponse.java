package com.p2p.user.dto;

import java.util.List;

public record EligibilityResponse(boolean eligible, List<String> reasons) {}
