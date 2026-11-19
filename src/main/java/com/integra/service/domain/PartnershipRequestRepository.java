package com.integra.service.domain;

import static com.integra.service.jooq.tables.PartnershipRequests.PARTNERSHIP_REQUESTS;

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
public class PartnershipRequestRepository {

    private final DSLContext dsl;

    @Inject
    public PartnershipRequestRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<PartnershipRequest> findAll() {
        return dsl.selectFrom(PARTNERSHIP_REQUESTS)
                .orderBy(PARTNERSHIP_REQUESTS.ID.asc())
                .fetch(this::toPartnershipRequest);
    }

    public Optional<PartnershipRequest> findById(Long id) {
        return dsl.selectFrom(PARTNERSHIP_REQUESTS)
                .where(PARTNERSHIP_REQUESTS.ID.eq(id))
                .fetchOptional(this::toPartnershipRequest);
    }

    @Transactional
    public PartnershipRequest save(
            String partnerType,
            String websiteUrl,
            String firstName,
            String lastName,
            String phone,
            String email,
            String companyName,
            String country,
            String message) {
        OffsetDateTime createdAt = Instant.now().atOffset(ZoneOffset.UTC);
        return dsl.insertInto(PARTNERSHIP_REQUESTS)
                .set(PARTNERSHIP_REQUESTS.PARTNER_TYPE, partnerType)
                .set(PARTNERSHIP_REQUESTS.WEBSITE_URL, websiteUrl)
                .set(PARTNERSHIP_REQUESTS.FIRST_NAME, firstName)
                .set(PARTNERSHIP_REQUESTS.LAST_NAME, lastName)
                .set(PARTNERSHIP_REQUESTS.PHONE, phone)
                .set(PARTNERSHIP_REQUESTS.EMAIL, email)
                .set(PARTNERSHIP_REQUESTS.COMPANY_NAME, companyName)
                .set(PARTNERSHIP_REQUESTS.COUNTRY, country)
                .set(PARTNERSHIP_REQUESTS.MESSAGE, message)
                .set(PARTNERSHIP_REQUESTS.CREATED_AT, createdAt)
                .returningResult(
                        PARTNERSHIP_REQUESTS.ID,
                        PARTNERSHIP_REQUESTS.PARTNER_TYPE,
                        PARTNERSHIP_REQUESTS.WEBSITE_URL,
                        PARTNERSHIP_REQUESTS.FIRST_NAME,
                        PARTNERSHIP_REQUESTS.LAST_NAME,
                        PARTNERSHIP_REQUESTS.PHONE,
                        PARTNERSHIP_REQUESTS.EMAIL,
                        PARTNERSHIP_REQUESTS.COMPANY_NAME,
                        PARTNERSHIP_REQUESTS.COUNTRY,
                        PARTNERSHIP_REQUESTS.MESSAGE,
                        PARTNERSHIP_REQUESTS.CREATED_AT)
                .fetchSingle(this::toPartnershipRequest);
    }

    @Transactional
    public Optional<PartnershipRequest> update(
            Long id,
            String partnerType,
            String websiteUrl,
            String firstName,
            String lastName,
            String phone,
            String email,
            String companyName,
            String country,
            String message) {
        return dsl.update(PARTNERSHIP_REQUESTS)
                .set(PARTNERSHIP_REQUESTS.PARTNER_TYPE, partnerType)
                .set(PARTNERSHIP_REQUESTS.WEBSITE_URL, websiteUrl)
                .set(PARTNERSHIP_REQUESTS.FIRST_NAME, firstName)
                .set(PARTNERSHIP_REQUESTS.LAST_NAME, lastName)
                .set(PARTNERSHIP_REQUESTS.PHONE, phone)
                .set(PARTNERSHIP_REQUESTS.EMAIL, email)
                .set(PARTNERSHIP_REQUESTS.COMPANY_NAME, companyName)
                .set(PARTNERSHIP_REQUESTS.COUNTRY, country)
                .set(PARTNERSHIP_REQUESTS.MESSAGE, message)
                .where(PARTNERSHIP_REQUESTS.ID.eq(id))
                .returningResult(
                        PARTNERSHIP_REQUESTS.ID,
                        PARTNERSHIP_REQUESTS.PARTNER_TYPE,
                        PARTNERSHIP_REQUESTS.WEBSITE_URL,
                        PARTNERSHIP_REQUESTS.FIRST_NAME,
                        PARTNERSHIP_REQUESTS.LAST_NAME,
                        PARTNERSHIP_REQUESTS.PHONE,
                        PARTNERSHIP_REQUESTS.EMAIL,
                        PARTNERSHIP_REQUESTS.COMPANY_NAME,
                        PARTNERSHIP_REQUESTS.COUNTRY,
                        PARTNERSHIP_REQUESTS.MESSAGE,
                        PARTNERSHIP_REQUESTS.CREATED_AT)
                .fetchOptional(this::toPartnershipRequest);
    }

    @Transactional
    public boolean deleteById(Long id) {
        return dsl.deleteFrom(PARTNERSHIP_REQUESTS).where(PARTNERSHIP_REQUESTS.ID.eq(id)).execute() > 0;
    }

    private PartnershipRequest toPartnershipRequest(org.jooq.Record record) {
        return new PartnershipRequest(
                record.get(PARTNERSHIP_REQUESTS.ID),
                record.get(PARTNERSHIP_REQUESTS.PARTNER_TYPE),
                record.get(PARTNERSHIP_REQUESTS.WEBSITE_URL),
                record.get(PARTNERSHIP_REQUESTS.FIRST_NAME),
                record.get(PARTNERSHIP_REQUESTS.LAST_NAME),
                record.get(PARTNERSHIP_REQUESTS.PHONE),
                record.get(PARTNERSHIP_REQUESTS.EMAIL),
                record.get(PARTNERSHIP_REQUESTS.COMPANY_NAME),
                record.get(PARTNERSHIP_REQUESTS.COUNTRY),
                record.get(PARTNERSHIP_REQUESTS.MESSAGE),
                toInstant(record.get(PARTNERSHIP_REQUESTS.CREATED_AT)));
    }

    private static Instant toInstant(OffsetDateTime value) {
        return value == null ? null : value.toInstant();
    }
}
