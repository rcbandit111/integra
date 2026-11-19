package com.integra.service.api;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.integra.service.domain.SalesRequest;
import com.integra.service.domain.SalesRequestRepository;

@Path("/api/sales-requests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SalesRequestResource {

    private final SalesRequestRepository salesRequestRepository;

    @Inject
    public SalesRequestResource(SalesRequestRepository salesRequestRepository) {
        this.salesRequestRepository = salesRequestRepository;
    }

    @GET
    public List<SalesRequest> list() {
        return salesRequestRepository.findAll();
    }

    @POST
    public Response create(SalesRequestCreateRequest body) {
        if (body == null) {
            throw new IllegalArgumentException("request body is required");
        }
        requireNonBlank(body.firstName(), "firstName");
        requireNonBlank(body.lastName(), "lastName");
        requireNonBlank(body.email(), "email");
        requireNonBlank(body.companyName(), "companyName");

        SalesRequest created = salesRequestRepository.save(
                body.firstName().trim(),
                body.lastName().trim(),
                blankToNull(body.phone()),
                body.email().trim(),
                body.companyName().trim(),
                blankToNull(body.country()),
                blankToNull(body.projectedMonthlyVolume()),
                blankToNull(body.industry()),
                blankToNull(body.companyWebsite()),
                blankToNull(body.isEcommercePlatform()),
                blankToNull(body.businessDescription()));
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    private static void requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
