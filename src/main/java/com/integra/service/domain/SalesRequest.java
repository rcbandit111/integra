package com.integra.service.domain;

import java.time.Instant;

public record SalesRequest(
        Long id,
        String firstName,
        String lastName,
        String phone,
        String email,
        String companyName,
        String country,
        String projectedMonthlyVolume,
        String industry,
        String companyWebsite,
        String isEcommercePlatform,
        String businessDescription,
        Instant createdAt) {
}
