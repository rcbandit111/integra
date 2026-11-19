package com.integra.service.web;

import com.integra.service.domain.SalesRequest;
import com.integra.service.domain.SalesRequestRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/web/sales-requests")
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

    @GET
    @Path("/{id}")
    public SalesRequest get(@PathParam("id") Long id) {
        return salesRequestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
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

    @PUT
    @Path("/{id}")
    public SalesRequest update(@PathParam("id") Long id, SalesRequestCreateRequest body) {
        if (body == null) {
            throw new IllegalArgumentException("request body is required");
        }
        requireNonBlank(body.firstName(), "firstName");
        requireNonBlank(body.lastName(), "lastName");
        requireNonBlank(body.email(), "email");
        requireNonBlank(body.companyName(), "companyName");

        return salesRequestRepository.update(
                        id,
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
                        blankToNull(body.businessDescription()))
                .orElseThrow(NotFoundException::new);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        if (!salesRequestRepository.deleteById(id)) {
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
