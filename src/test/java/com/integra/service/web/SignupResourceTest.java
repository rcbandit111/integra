package com.integra.service.web;

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
    void createGetUpdateDeleteSignup() {
        String email = "web-ada-" + System.nanoTime() + "@example.com";
        int id = given()
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
                .when().post("/web/signups")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("email", is(email))
                .body("password", nullValue())
                .body("passwordHash", nullValue())
                .extract().path("id");

        given()
                .when().get("/web/signups/{id}", id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("email", is(email));

        String updatedEmail = "web-updated-" + System.nanoTime() + "@example.com";
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "email": "%s",
                          "firstName": "Augusta",
                          "lastName": "King",
                          "company": "Analytical Engines Ltd",
                          "industry": "banking",
                          "country": "FR"
                        }
                        """.formatted(updatedEmail))
                .when().put("/web/signups/{id}", id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("email", is(updatedEmail))
                .body("firstName", is("Augusta"))
                .body("company", is("Analytical Engines Ltd"));

        given()
                .when().get("/web/signups")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));

        given()
                .when().delete("/web/signups/{id}", id)
                .then()
                .statusCode(204);

        given()
                .when().get("/web/signups/{id}", id)
                .then()
                .statusCode(404);
    }

    @Test
    void getUpdateDeleteUnknownSignupReturns404() {
        given().when().get("/web/signups/{id}", 999999L).then().statusCode(404);

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "email": "missing@example.com",
                          "firstName": "Ada",
                          "lastName": "Lovelace",
                          "company": "Analytical Engines"
                        }
                        """)
                .when().put("/web/signups/{id}", 999999L)
                .then()
                .statusCode(404);

        given().when().delete("/web/signups/{id}", 999999L).then().statusCode(404);
    }
}
