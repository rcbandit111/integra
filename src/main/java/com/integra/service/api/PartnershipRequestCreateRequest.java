package com.integra.service.api;

public record PartnershipRequestCreateRequest(
        String partnerType,
        String websiteUrl,
        String firstName,
        String lastName,
        String phone,
        String email,
        String companyName,
        String country,
        String message) {
}
