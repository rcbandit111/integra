package com.integra.service.api;

public record SalesRequestCreateRequest(
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
        String businessDescription) {
}
