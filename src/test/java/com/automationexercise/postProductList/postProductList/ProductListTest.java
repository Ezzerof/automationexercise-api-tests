package com.automationexercise.postProductList.postProductList;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ProductListTest {

    static Response response;
    static int actualStatusCode = 0;
    public final static int statusCode = 200; // Entered by tester
    public final static String expectedStatusCode = "405";
    final static String URL = "https://automationexercise.com/api/productsList"; // Entered by tester


    @BeforeAll
    static void init() {
        response = given()
                .when()
                .post(URL)
                .then()
                .statusCode(statusCode)
                .extract().response();

    }

//    @Test
//    @DisplayName("Test Status Code: " + expectedStatusCode)
//    void testStatusCode() {
//        actualStatusCode = response.getStatusCode();
//        assertThat(actualStatusCode, equalTo(expectedStatusCode));
//    }

    @Test
    @DisplayName("Test Status Code: 405")
    void testStatusCode() {
        assertThat(response.jsonPath().getString("responseCode"), equalTo(expectedStatusCode));
    }

    @Test
    @DisplayName("Test message")
    void testMessage() {
        assertThat(response.jsonPath().getString("message"), equalTo("This request method is not supported."));
    }
}
