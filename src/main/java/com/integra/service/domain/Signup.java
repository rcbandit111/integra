package com.integra.service.domain;

import java.time.Instant;

public record Signup(
        Long id,
        String email,
        String firstName,
        String lastName,
        String company,
        String registrationNumber,
        String industry,
        String country,
        Instant createdAt) {
}
