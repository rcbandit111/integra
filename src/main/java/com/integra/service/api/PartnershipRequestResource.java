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

import com.integra.service.domain.PartnershipRequest;
import com.integra.service.domain.PartnershipRequestRepository;

@Path("/api/partnership-requests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnershipRequestResource {

    private final PartnershipRequestRepository partnershipRequestRepository;

    @Inject
    public PartnershipRequestResource(PartnershipRequestRepository partnershipRequestRepository) {
        this.partnershipRequestRepository = partnershipRequestRepository;
    }

    @GET
    public List<PartnershipRequest> list() {
        return partnershipRequestRepository.findAll();
    }

    @POST
    public Response create(PartnershipRequestCreateRequest body) {
        if (body == null) {
            throw new IllegalArgumentException("request body is required");
        }
        requireNonBlank(body.partnerType(), "partnerType");
        requireNonBlank(body.firstName(), "firstName");
        requireNonBlank(body.lastName(), "lastName");
        requireNonBlank(body.email(), "email");
        requireNonBlank(body.companyName(), "companyName");

        PartnershipRequest created = partnershipRequestRepository.save(
                body.partnerType().trim(),
                blankToNull(body.websiteUrl()),
                body.firstName().trim(),
                body.lastName().trim(),
                blankToNull(body.phone()),
                body.email().trim(),
                body.companyName().trim(),
                blankToNull(body.country()),
                blankToNull(body.message()));
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
