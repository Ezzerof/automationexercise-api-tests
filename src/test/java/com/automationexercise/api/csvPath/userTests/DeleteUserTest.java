package com.automationexercise.api.csvPath.userTests;

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
public class DeleteUserTest {

    public static Response response;

    @TestTemplate
    @Order(1)
    @DisplayName("Deleting existing account")
    @ParameterizedTest(name = "{index} - Name: {0}")
    @CsvFileSource(files = "src\\test\\resources\\DeleteUser.csv", numLinesToSkip = 1)
    public void init(String email, String password) {
        response = given()
                .log().all()
                .contentType("application/x-www-form-urlencoded")
                .formParams("email", email, "password", password)
                .delete(Routes.deleteUserAccount_url);

    }


    @Test
    @Order(2)
    @DisplayName("Test response message should be Account deleted!")
    void testResponseMessageShouldBeAccountDeleted() {
        assertThat(response.jsonPath().getString("message"), equalTo("Account deleted!"));
    }

    @Test
    @Order(3)
    @DisplayName("Test response code should be 200")
    void testResponseCodeShouldBe200() {

        assertThat(response.jsonPath().getString("responseCode"), equalTo("200"));
    }

    @Test
    @Order(4)
    @DisplayName("Test status code should be 200")
    void testStatusCodeShouldBe200() {
        assertThat(response.getStatusCode(), equalTo(200));
    }

}
