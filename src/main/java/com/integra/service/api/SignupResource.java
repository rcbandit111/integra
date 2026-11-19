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

import io.quarkus.elytron.security.common.BcryptUtil;

import com.integra.service.domain.Signup;
import com.integra.service.domain.SignupRepository;

@Path("/api/signups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SignupResource {

    private final SignupRepository signupRepository;

    @Inject
    public SignupResource(SignupRepository signupRepository) {
        this.signupRepository = signupRepository;
    }

    @GET
    public List<Signup> list() {
        return signupRepository.findAll();
    }

    @POST
    public Response create(SignupCreateRequest body) {
        if (body == null) {
            throw new IllegalArgumentException("request body is required");
        }
        requireNonBlank(body.email(), "email");
        requireNonBlank(body.password(), "password");
        requireNonBlank(body.confirmPassword(), "confirmPassword");
        requireNonBlank(body.firstName(), "firstName");
        requireNonBlank(body.lastName(), "lastName");
        requireNonBlank(body.company(), "company");

        if (!body.password().equals(body.confirmPassword())) {
            throw new IllegalArgumentException("password and confirmPassword must match");
        }

        String email = body.email().trim();
        if (signupRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("email is already registered");
        }

        String passwordHash = BcryptUtil.bcryptHash(body.password());
        Signup created = signupRepository.save(
                email,
                passwordHash,
                body.firstName().trim(),
                body.lastName().trim(),
                body.company().trim(),
                blankToNull(body.registrationNumber()),
                blankToNull(body.industry()),
                blankToNull(body.country()));
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
