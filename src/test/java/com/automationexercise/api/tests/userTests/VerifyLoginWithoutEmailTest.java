package com.automationexercise.api.tests.userTests;

import com.automationexercise.api.endpoints.Routes;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VerifyLoginWithoutEmailTest {

    static Response response;
    static String password = "123456789";

    @Order(1)
    @TestTemplate
    @DisplayName("Verify user account without email parameter")
    @ParameterizedTest(name = "{index} - Name: {0}")
    @CsvFileSource(files = "src\\test\\resources\\DeleteUser.csv", numLinesToSkip = 3)
    public void init(String email, String password) {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams( "password", password)
                .post(Routes.postLoginDetails_url);
    }
    @Order(2)
    @Test
    @DisplayName("Test response message should be Bad request")
    public void testVerifyLoginWithValidDetails() {
        assertThat(response.jsonPath().getString("message"), equalTo("Bad request, email or password parameter is missing in POST request."));
    }

    @Order(3)
    @Test
    @DisplayName("Test response code should be 400")
    void testResultCodeShouldBe400() {
        assertThat(response.jsonPath().getString("responseCode"), equalTo("400"));
    }

    @Order(4)
    @Test
    @DisplayName("Test the status code should be 200")
    void testTheResponseCodeShouldBe200() {
        assertThat(response.getStatusCode(), equalTo(200));

    }

}
