package com.automationexercise.api.tests.productsListTests;

import com.automationexercise.api.endpoints.Routes;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SearchProductWithoutSearchParamTest {

    static Response response;
    static int actualStatusCode = 0;
    public final static int statusCode = 200; // Entered by tester
    public final static String expectedStatusCode = "400"; // Entered by tester
    final static String searchProduct = "jeans"; // Entered by tester

    @BeforeAll
    static void init() {
        response = given()
                .queryParam(searchProduct)
                .when()
                .post(Routes.postSearchProduct_url)
                .then()
                .statusCode(statusCode)
                .extract().response();

    }

    @Test
    @DisplayName("Testing response code of a search without parameter 400")
    void testingResponseCodeOfASearchWithoutParameter400() {
        assertThat(response.jsonPath().getString("responseCode"), equalTo(expectedStatusCode));
    }

    @Test
    @DisplayName("Testing response message of a search without parameter")
    void testingResponseMessageOfASearchWithoutParameter() {
        assertThat(response.jsonPath().getString("message"), equalTo("Bad request, search_product parameter is missing in POST request."));
    }



}
