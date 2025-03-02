package com.automationexercise.api.csv_path.brands_list_tests;

import com.automationexercise.api.config.Config;
import com.automationexercise.api.endpoints.Routes;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PutToBrandsListTest {

    public static Response response;

    @Test
    @Order(1)
    @DisplayName("Sending PUT request to brands list")
    void sendPutRequestToBrandsList() {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .when()
                .put(Config.BASE_URL + Config.BRANDS_ENDPOINT)
                .then()
                .extract().response();
        System.out.println(response.getBody().asString());

    }

    @Test
    @Order(2)
    @DisplayName("Validate response message: Method not supported")
    void validateResponseMessage() {
        assertThat("Expected 'method not supported' message!",
                response.jsonPath().getString("message"), equalTo(Config.EXPECTED_METHOD_NOT_SUPPORTED_MESSAGE));
    }

    @Test
    @Order(3)
    @DisplayName("Validate response code: 405")
    void validateResponseCode() {
        assertThat("Expected response code 405!",
                response.jsonPath().getString("responseCode"), equalTo(Config.EXPECTED_RESPONSE_CODE_405));
    }

    @Test
    @Order(4)
    @DisplayName("Validate HTTP status code: 200")
    void validateStatusCode() {
        assertThat("Expected HTTP status code 200!",
                response.getStatusCode(), equalTo(Config.EXPECTED_STATUS_CODE));
    }

}
