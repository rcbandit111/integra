package com.integra.service.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class PartnershipRequestResourceTest {

    @Test
    void createGetUpdateDeletePartnershipRequest() {
        int id = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "partnerType": "reseller",
                          "websiteUrl": "https://example.com",
                          "firstName": "Ada",
                          "lastName": "Lovelace",
                          "phone": "+123456789",
                          "email": "web-ada@example.com",
                          "companyName": "Analytical Engines",
                          "country": "UK",
                          "message": "Interested in partnering"
                        }
                        """)
                .when().post("/web/partnership-requests")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("partnerType", is("reseller"))
                .extract().path("id");

        given()
                .when().get("/web/partnership-requests/{id}", id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("email", is("web-ada@example.com"));

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "partnerType": "integrator",
                          "websiteUrl": "https://example.org",
                          "firstName": "Augusta",
                          "lastName": "King",
                          "phone": "+987654321",
                          "email": "web-augusta@example.com",
                          "companyName": "Analytical Engines Ltd",
                          "country": "FR",
                          "message": "Updated partnership interest"
                        }
                        """)
                .when().put("/web/partnership-requests/{id}", id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("partnerType", is("integrator"))
                .body("email", is("web-augusta@example.com"))
                .body("firstName", is("Augusta"));

        given()
                .when().get("/web/partnership-requests")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));

        given()
                .when().delete("/web/partnership-requests/{id}", id)
                .then()
                .statusCode(204);

        given()
                .when().get("/web/partnership-requests/{id}", id)
                .then()
                .statusCode(404);
    }

    @Test
    void getUpdateDeleteUnknownPartnershipRequestReturns404() {
        given().when().get("/web/partnership-requests/{id}", 999999L).then().statusCode(404);

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "partnerType": "reseller",
                          "firstName": "Ada",
                          "lastName": "Lovelace",
                          "email": "missing@example.com",
                          "companyName": "Analytical Engines"
                        }
                        """)
                .when().put("/web/partnership-requests/{id}", 999999L)
                .then()
                .statusCode(404);

        given().when().delete("/web/partnership-requests/{id}", 999999L).then().statusCode(404);
    }
}
