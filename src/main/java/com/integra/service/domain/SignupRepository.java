package com.integra.service.domain;

import static com.integra.service.jooq.tables.Signups.SIGNUPS;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.jooq.DSLContext;

@ApplicationScoped
public class SignupRepository {

    private final DSLContext dsl;

    @Inject
    public SignupRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<Signup> findAll() {
        return dsl.selectFrom(SIGNUPS)
                .orderBy(SIGNUPS.ID.asc())
                .fetch(this::toSignup);
    }

    public Optional<Signup> findById(Long id) {
        return dsl.select(
                        SIGNUPS.ID,
                        SIGNUPS.EMAIL,
                        SIGNUPS.FIRST_NAME,
                        SIGNUPS.LAST_NAME,
                        SIGNUPS.COMPANY,
                        SIGNUPS.REGISTRATION_NUMBER,
                        SIGNUPS.INDUSTRY,
                        SIGNUPS.COUNTRY,
                        SIGNUPS.CREATED_AT)
                .from(SIGNUPS)
                .where(SIGNUPS.ID.eq(id))
                .fetchOptional(this::toSignup);
    }

    public String findPasswordHashById(Long id) {
        return dsl.select(SIGNUPS.PASSWORD_HASH)
                .from(SIGNUPS)
                .where(SIGNUPS.ID.eq(id))
                .fetchOne(SIGNUPS.PASSWORD_HASH);
    }

    public boolean existsByEmail(String email) {
        return dsl.fetchExists(
                dsl.selectFrom(SIGNUPS).where(SIGNUPS.EMAIL.eq(email)));
    }

    public boolean existsByEmailExcludingId(String email, Long id) {
        return dsl.fetchExists(
                dsl.selectFrom(SIGNUPS)
                        .where(SIGNUPS.EMAIL.eq(email).and(SIGNUPS.ID.ne(id))));
    }

    @Transactional
    public Signup save(
            String email,
            String passwordHash,
            String firstName,
            String lastName,
            String company,
            String registrationNumber,
            String industry,
            String country) {
        OffsetDateTime createdAt = Instant.now().atOffset(ZoneOffset.UTC);
        return dsl.insertInto(SIGNUPS)
                .set(SIGNUPS.EMAIL, email)
                .set(SIGNUPS.PASSWORD_HASH, passwordHash)
                .set(SIGNUPS.FIRST_NAME, firstName)
                .set(SIGNUPS.LAST_NAME, lastName)
                .set(SIGNUPS.COMPANY, company)
                .set(SIGNUPS.REGISTRATION_NUMBER, registrationNumber)
                .set(SIGNUPS.INDUSTRY, industry)
                .set(SIGNUPS.COUNTRY, country)
                .set(SIGNUPS.CREATED_AT, createdAt)
                .returningResult(
                        SIGNUPS.ID,
                        SIGNUPS.EMAIL,
                        SIGNUPS.FIRST_NAME,
                        SIGNUPS.LAST_NAME,
                        SIGNUPS.COMPANY,
                        SIGNUPS.REGISTRATION_NUMBER,
                        SIGNUPS.INDUSTRY,
                        SIGNUPS.COUNTRY,
                        SIGNUPS.CREATED_AT)
                .fetchSingle(this::toSignup);
    }

    @Transactional
    public Optional<Signup> update(
            Long id,
            String email,
            String passwordHash,
            String firstName,
            String lastName,
            String company,
            String registrationNumber,
            String industry,
            String country) {
        return dsl.update(SIGNUPS)
                .set(SIGNUPS.EMAIL, email)
                .set(SIGNUPS.PASSWORD_HASH, passwordHash)
                .set(SIGNUPS.FIRST_NAME, firstName)
                .set(SIGNUPS.LAST_NAME, lastName)
                .set(SIGNUPS.COMPANY, company)
                .set(SIGNUPS.REGISTRATION_NUMBER, registrationNumber)
                .set(SIGNUPS.INDUSTRY, industry)
                .set(SIGNUPS.COUNTRY, country)
                .where(SIGNUPS.ID.eq(id))
                .returningResult(
                        SIGNUPS.ID,
                        SIGNUPS.EMAIL,
                        SIGNUPS.FIRST_NAME,
                        SIGNUPS.LAST_NAME,
                        SIGNUPS.COMPANY,
                        SIGNUPS.REGISTRATION_NUMBER,
                        SIGNUPS.INDUSTRY,
                        SIGNUPS.COUNTRY,
                        SIGNUPS.CREATED_AT)
                .fetchOptional(this::toSignup);
    }

    @Transactional
    public boolean deleteById(Long id) {
        return dsl.deleteFrom(SIGNUPS).where(SIGNUPS.ID.eq(id)).execute() > 0;
    }

    private Signup toSignup(org.jooq.Record record) {
        return new Signup(
                record.get(SIGNUPS.ID),
                record.get(SIGNUPS.EMAIL),
                record.get(SIGNUPS.FIRST_NAME),
                record.get(SIGNUPS.LAST_NAME),
                record.get(SIGNUPS.COMPANY),
                record.get(SIGNUPS.REGISTRATION_NUMBER),
                record.get(SIGNUPS.INDUSTRY),
                record.get(SIGNUPS.COUNTRY),
                toInstant(record.get(SIGNUPS.CREATED_AT)));
    }

    private static Instant toInstant(OffsetDateTime value) {
        return value == null ? null : value.toInstant();
    }
}
