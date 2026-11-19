package com.integra.service.web;

import com.integra.service.domain.Signup;
import com.integra.service.domain.SignupRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/web/signups")
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

    @GET
    @Path("/{id}")
    public Signup get(@PathParam("id") Long id) {
        return signupRepository.findById(id)
                .orElseThrow(NotFoundException::new);
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

    @PUT
    @Path("/{id}")
    public Signup update(@PathParam("id") Long id, SignupCreateRequest body) {
        if (body == null) {
            throw new IllegalArgumentException("request body is required");
        }
        requireNonBlank(body.email(), "email");
        requireNonBlank(body.firstName(), "firstName");
        requireNonBlank(body.lastName(), "lastName");
        requireNonBlank(body.company(), "company");

        if (!signupRepository.findById(id).isPresent()) {
            throw new NotFoundException();
        }

        String email = body.email().trim();
        if (signupRepository.existsByEmailExcludingId(email, id)) {
            throw new IllegalArgumentException("email is already registered");
        }

        String passwordHash;
        if (body.password() != null && !body.password().isBlank()) {
            requireNonBlank(body.confirmPassword(), "confirmPassword");
            if (!body.password().equals(body.confirmPassword())) {
                throw new IllegalArgumentException("password and confirmPassword must match");
            }
            passwordHash = BcryptUtil.bcryptHash(body.password());
        } else {
            passwordHash = signupRepository.findPasswordHashById(id);
            if (passwordHash == null) {
                throw new NotFoundException();
            }
        }

        return signupRepository.update(
                        id,
                        email,
                        passwordHash,
                        body.firstName().trim(),
                        body.lastName().trim(),
                        body.company().trim(),
                        blankToNull(body.registrationNumber()),
                        blankToNull(body.industry()),
                        blankToNull(body.country()))
                .orElseThrow(NotFoundException::new);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        if (!signupRepository.deleteById(id)) {
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
