package com.integra.service.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class SignupResourceTest {

    @Test
    void createAndListSignups() {
        String email = "ada-" + System.nanoTime() + "@example.com";
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "email": "%s",
                          "password": "secret123",
                          "confirmPassword": "secret123",
                          "firstName": "Ada",
                          "lastName": "Lovelace",
                          "company": "Analytical Engines",
                          "registrationNumber": "REG-1",
                          "industry": "fintech",
                          "country": "UK"
                        }
                        """.formatted(email))
                .when().post("/api/signups")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("email", is(email))
                .body("company", is("Analytical Engines"))
                .body("password", nullValue())
                .body("passwordHash", nullValue())
                .body("createdAt", notNullValue());

        given()
                .when().get("/api/signups")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    void createRequiresMatchingPasswords() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "email": "mismatch@example.com",
                          "password": "secret123",
                          "confirmPassword": "other",
                          "firstName": "Ada",
                          "lastName": "Lovelace",
                          "company": "Analytical Engines"
                        }
                        """)
                .when().post("/api/signups")
                .then()
                .statusCode(400)
                .body("error", is("password and confirmPassword must match"));
    }

    @Test
    void createRequiresEmail() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "password": "secret123",
                          "confirmPassword": "secret123",
                          "firstName": "Ada",
                          "lastName": "Lovelace",
                          "company": "Analytical Engines"
                        }
                        """)
                .when().post("/api/signups")
                .then()
                .statusCode(400)
                .body("error", is("email is required"));
    }
}
