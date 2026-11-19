package com.integra.service.web;

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
