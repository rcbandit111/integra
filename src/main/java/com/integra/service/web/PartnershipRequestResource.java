package com.integra.service.web;

import com.integra.service.domain.PartnershipRequest;
import com.integra.service.domain.PartnershipRequestRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/web/partnership-requests")
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

    @GET
    @Path("/{id}")
    public PartnershipRequest get(@PathParam("id") Long id) {
        return partnershipRequestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
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

    @PUT
    @Path("/{id}")
    public PartnershipRequest update(@PathParam("id") Long id, PartnershipRequestCreateRequest body) {
        if (body == null) {
            throw new IllegalArgumentException("request body is required");
        }
        requireNonBlank(body.partnerType(), "partnerType");
        requireNonBlank(body.firstName(), "firstName");
        requireNonBlank(body.lastName(), "lastName");
        requireNonBlank(body.email(), "email");
        requireNonBlank(body.companyName(), "companyName");

        return partnershipRequestRepository.update(
                        id,
                        body.partnerType().trim(),
                        blankToNull(body.websiteUrl()),
                        body.firstName().trim(),
                        body.lastName().trim(),
                        blankToNull(body.phone()),
                        body.email().trim(),
                        body.companyName().trim(),
                        blankToNull(body.country()),
                        blankToNull(body.message()))
                .orElseThrow(NotFoundException::new);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        if (!partnershipRequestRepository.deleteById(id)) {
            throw new NotFoundException();
        }
        return Response.noContent().build();
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
