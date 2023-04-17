package com.automationexercise.api.tests.brandsListTests;

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
    @DisplayName("Putting to brands list")
    public void init() {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .when()
                .put(Routes.putBrands_url)
                .then()
                .extract().response();
        System.out.println(response.getBody().asString());

    }

    @Test
    @Order(2)
    @DisplayName("Test response message should be method not supported")
    void testResponseMessageShouldBeNotSupported() {
        assertThat(response.jsonPath().getString("message"), equalTo("This request method is not supported."));
    }

    @Test
    @Order(3)
    @DisplayName("Test response code should be 405")
    void testResponseCodeShouldBe405() {
        assertThat(response.jsonPath().getString("responseCode"), equalTo("405"));
    }

    @Test
    @Order(4)
    @DisplayName("Test status code should be 200")
    void testStatusCodeShouldBe200() {
        assertThat(response.getStatusCode(), equalTo(200));
    }

}
