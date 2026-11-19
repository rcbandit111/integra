package com.integra.service.domain;

import static com.integra.service.jooq.tables.SalesRequests.SALES_REQUESTS;

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
public class SalesRequestRepository {

    private final DSLContext dsl;

    @Inject
    public SalesRequestRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<SalesRequest> findAll() {
        return dsl.selectFrom(SALES_REQUESTS)
                .orderBy(SALES_REQUESTS.ID.asc())
                .fetch(this::toSalesRequest);
    }

    public Optional<SalesRequest> findById(Long id) {
        return dsl.selectFrom(SALES_REQUESTS)
                .where(SALES_REQUESTS.ID.eq(id))
                .fetchOptional(this::toSalesRequest);
    }

    @Transactional
    public SalesRequest save(
            String firstName,
            String lastName,
            String phone,
            String email,
            String companyName,
            String country,
            String projectedMonthlyVolume,
            String industry,
            String companyWebsite,
            String isEcommercePlatform,
            String businessDescription) {
        OffsetDateTime createdAt = Instant.now().atOffset(ZoneOffset.UTC);
        return dsl.insertInto(SALES_REQUESTS)
                .set(SALES_REQUESTS.FIRST_NAME, firstName)
                .set(SALES_REQUESTS.LAST_NAME, lastName)
                .set(SALES_REQUESTS.PHONE, phone)
                .set(SALES_REQUESTS.EMAIL, email)
                .set(SALES_REQUESTS.COMPANY_NAME, companyName)
                .set(SALES_REQUESTS.COUNTRY, country)
                .set(SALES_REQUESTS.PROJECTED_MONTHLY_VOLUME, projectedMonthlyVolume)
                .set(SALES_REQUESTS.INDUSTRY, industry)
                .set(SALES_REQUESTS.COMPANY_WEBSITE, companyWebsite)
                .set(SALES_REQUESTS.IS_ECOMMERCE_PLATFORM, isEcommercePlatform)
                .set(SALES_REQUESTS.BUSINESS_DESCRIPTION, businessDescription)
                .set(SALES_REQUESTS.CREATED_AT, createdAt)
                .returningResult(
                        SALES_REQUESTS.ID,
                        SALES_REQUESTS.FIRST_NAME,
                        SALES_REQUESTS.LAST_NAME,
                        SALES_REQUESTS.PHONE,
                        SALES_REQUESTS.EMAIL,
                        SALES_REQUESTS.COMPANY_NAME,
                        SALES_REQUESTS.COUNTRY,
                        SALES_REQUESTS.PROJECTED_MONTHLY_VOLUME,
                        SALES_REQUESTS.INDUSTRY,
                        SALES_REQUESTS.COMPANY_WEBSITE,
                        SALES_REQUESTS.IS_ECOMMERCE_PLATFORM,
                        SALES_REQUESTS.BUSINESS_DESCRIPTION,
                        SALES_REQUESTS.CREATED_AT)
                .fetchSingle(this::toSalesRequest);
    }

    @Transactional
    public Optional<SalesRequest> update(
            Long id,
            String firstName,
            String lastName,
            String phone,
            String email,
            String companyName,
            String country,
            String projectedMonthlyVolume,
            String industry,
            String companyWebsite,
            String isEcommercePlatform,
            String businessDescription) {
        return dsl.update(SALES_REQUESTS)
                .set(SALES_REQUESTS.FIRST_NAME, firstName)
                .set(SALES_REQUESTS.LAST_NAME, lastName)
                .set(SALES_REQUESTS.PHONE, phone)
                .set(SALES_REQUESTS.EMAIL, email)
                .set(SALES_REQUESTS.COMPANY_NAME, companyName)
                .set(SALES_REQUESTS.COUNTRY, country)
                .set(SALES_REQUESTS.PROJECTED_MONTHLY_VOLUME, projectedMonthlyVolume)
                .set(SALES_REQUESTS.INDUSTRY, industry)
                .set(SALES_REQUESTS.COMPANY_WEBSITE, companyWebsite)
                .set(SALES_REQUESTS.IS_ECOMMERCE_PLATFORM, isEcommercePlatform)
                .set(SALES_REQUESTS.BUSINESS_DESCRIPTION, businessDescription)
                .where(SALES_REQUESTS.ID.eq(id))
                .returningResult(
                        SALES_REQUESTS.ID,
                        SALES_REQUESTS.FIRST_NAME,
                        SALES_REQUESTS.LAST_NAME,
                        SALES_REQUESTS.PHONE,
                        SALES_REQUESTS.EMAIL,
                        SALES_REQUESTS.COMPANY_NAME,
                        SALES_REQUESTS.COUNTRY,
                        SALES_REQUESTS.PROJECTED_MONTHLY_VOLUME,
                        SALES_REQUESTS.INDUSTRY,
                        SALES_REQUESTS.COMPANY_WEBSITE,
                        SALES_REQUESTS.IS_ECOMMERCE_PLATFORM,
                        SALES_REQUESTS.BUSINESS_DESCRIPTION,
                        SALES_REQUESTS.CREATED_AT)
                .fetchOptional(this::toSalesRequest);
    }

    @Transactional
    public boolean deleteById(Long id) {
        return dsl.deleteFrom(SALES_REQUESTS).where(SALES_REQUESTS.ID.eq(id)).execute() > 0;
    }

    private SalesRequest toSalesRequest(org.jooq.Record record) {
        return new SalesRequest(
                record.get(SALES_REQUESTS.ID),
                record.get(SALES_REQUESTS.FIRST_NAME),
                record.get(SALES_REQUESTS.LAST_NAME),
                record.get(SALES_REQUESTS.PHONE),
                record.get(SALES_REQUESTS.EMAIL),
                record.get(SALES_REQUESTS.COMPANY_NAME),
                record.get(SALES_REQUESTS.COUNTRY),
                record.get(SALES_REQUESTS.PROJECTED_MONTHLY_VOLUME),
                record.get(SALES_REQUESTS.INDUSTRY),
                record.get(SALES_REQUESTS.COMPANY_WEBSITE),
                record.get(SALES_REQUESTS.IS_ECOMMERCE_PLATFORM),
                record.get(SALES_REQUESTS.BUSINESS_DESCRIPTION),
                toInstant(record.get(SALES_REQUESTS.CREATED_AT)));
    }

    private static Instant toInstant(OffsetDateTime value) {
        return value == null ? null : value.toInstant();
    }
}
