package com.integra.service.web;

public record SignupCreateRequest(
        String email,
        String password,
        String confirmPassword,
        String firstName,
        String lastName,
        String company,
        String registrationNumber,
        String industry,
        String country) {
}
