package com.integra.service.domain;

import java.time.Instant;

public record PartnershipRequest(
        Long id,
        String partnerType,
        String websiteUrl,
        String firstName,
        String lastName,
        String phone,
        String email,
        String companyName,
        String country,
        String message,
        Instant createdAt) {
}
