package com.integra.service.web;

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
    void createGetUpdateDeleteSalesRequest() {
        int id = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "firstName": "Ada",
                          "lastName": "Lovelace",
                          "phone": "+123456789",
                          "email": "web-ada@example.com",
                          "companyName": "Analytical Engines",
                          "country": "UK",
                          "projectedMonthlyVolume": "100k",
                          "industry": "fintech",
                          "companyWebsite": "https://example.com",
                          "isEcommercePlatform": "yes",
                          "businessDescription": "Payment processing"
                        }
                        """)
                .when().post("/web/sales-requests")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("email", is("web-ada@example.com"))
                .extract().path("id");

        given()
                .when().get("/web/sales-requests/{id}", id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("companyName", is("Analytical Engines"));

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "firstName": "Augusta",
                          "lastName": "King",
                          "phone": "+987654321",
                          "email": "web-augusta@example.com",
                          "companyName": "Analytical Engines Ltd",
                          "country": "FR",
                          "projectedMonthlyVolume": "200k",
                          "industry": "banking",
                          "companyWebsite": "https://example.org",
                          "isEcommercePlatform": "no",
                          "businessDescription": "Updated description"
                        }
                        """)
                .when().put("/web/sales-requests/{id}", id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("email", is("web-augusta@example.com"))
                .body("firstName", is("Augusta"))
                .body("companyName", is("Analytical Engines Ltd"));

        given()
                .when().get("/web/sales-requests")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));

        given()
                .when().delete("/web/sales-requests/{id}", id)
                .then()
                .statusCode(204);

        given()
                .when().get("/web/sales-requests/{id}", id)
                .then()
                .statusCode(404);
    }

    @Test
    void getUpdateDeleteUnknownSalesRequestReturns404() {
        given().when().get("/web/sales-requests/{id}", 999999L).then().statusCode(404);

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "firstName": "Ada",
                          "lastName": "Lovelace",
                          "email": "missing@example.com",
                          "companyName": "Analytical Engines"
                        }
                        """)
                .when().put("/web/sales-requests/{id}", 999999L)
                .then()
                .statusCode(404);

        given().when().delete("/web/sales-requests/{id}", 999999L).then().statusCode(404);
    }
}
