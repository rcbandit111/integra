package com.integra.service.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class SalesRequestResourceTest {

    @Test
    void createAndListSalesRequests() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "firstName": "Ada",
                          "lastName": "Lovelace",
                          "phone": "+123456789",
                          "email": "ada@example.com",
                          "companyName": "Analytical Engines",
                          "country": "UK",
                          "projectedMonthlyVolume": "100k",
                          "industry": "fintech",
                          "companyWebsite": "https://example.com",
                          "isEcommercePlatform": "yes",
                          "businessDescription": "Payment processing"
                        }
                        """)
                .when().post("/api/sales-requests")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("email", is("ada@example.com"))
                .body("companyName", is("Analytical Engines"))
                .body("industry", is("fintech"))
                .body("createdAt", notNullValue());

        given()
                .when().get("/api/sales-requests")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    void createRequiresEmail() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "firstName": "Ada",
                          "lastName": "Lovelace",
                          "companyName": "Analytical Engines"
                        }
                        """)
                .when().post("/api/sales-requests")
                .then()
                .statusCode(400)
                .body("error", is("email is required"));
    }
}
