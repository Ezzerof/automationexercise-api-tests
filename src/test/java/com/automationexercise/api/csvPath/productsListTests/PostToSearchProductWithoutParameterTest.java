package com.automationexercise.api.csvPath.productsListTests;

import com.automationexercise.api.endpoints.Routes;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostToSearchProductWithoutParameterTest {

    public static Response response;

    @Test
    @Order(1)
    @DisplayName("Post to search product without parameter")
    public void init() {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .post(Routes.postSearchProduct_url);
        System.out.println(response.getBody().asString());

    }

    @Test
    @Order(2)
    @DisplayName("Test response message should be bad request")
    void testResponseMessageShouldBeBadRequest() {
        assertThat(response.jsonPath().getString("message"), equalTo("Bad request, search_product parameter is missing in POST request."));
    }


    @Test
    @Order(3)
    @DisplayName("Test response code should be 400")
    void testResponseCodeShouldBe200() {
        assertThat(response.jsonPath().getString("responseCode"), equalTo("400"));
    }

    @Test
    @Order(4)
    @DisplayName("Test status code should be 200")
    void testStatusCodeShouldBe200() {
        assertThat(response.getStatusCode(), equalTo(200));
    }

}
