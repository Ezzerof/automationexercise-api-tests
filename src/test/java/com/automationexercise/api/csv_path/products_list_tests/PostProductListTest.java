package com.automationexercise.api.csv_path.products_list_tests;

import com.automationexercise.api.config.Config;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostProductListTest {

    public static Response response;

    @Test
    @Order(1)
    @DisplayName("Post all products")
    public void init() {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .post(Config.BASE_URL + Config.PRODUCTS_ENDPOINT);

    }

    @Test
    @Order(2)
    @DisplayName("Validate response message: Method not supported")
    void testResponseMessageShouldBeAccountDeleted() {
        assertThat(response.jsonPath().getString("message"), equalTo(Config.EXPECTED_METHOD_NOT_SUPPORTED_MESSAGE));
    }

    @Test
    @Order(3)
    @DisplayName("Validate response code: 405")
    void testResponseCodeShouldBe405() {
        assertThat(response.jsonPath().getString("responseCode"), equalTo(Config.EXPECTED_RESPONSE_CODE_405));
    }

    @Test
    @Order(4)
    @DisplayName("Validate status code: 200")
    void testStatusCodeShouldBe200() {
        assertThat(response.getStatusCode(), equalTo(Config.EXPECTED_STATUS_CODE));
    }
}
