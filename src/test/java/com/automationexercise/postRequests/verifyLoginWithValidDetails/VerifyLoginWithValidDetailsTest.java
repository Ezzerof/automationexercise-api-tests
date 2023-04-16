package com.automationexercise.postRequests.verifyLoginWithValidDetails;

import com.automationexercise.api.endpoints.Routes;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class VerifyLoginWithValidDetailsTest {

    static Response response;
    static int actualStatusCode = 0;
    public final static int statusCode = 200; // Entered by tester

    @BeforeAll
    static void init() {
        response = given()
                .when()
                .post(Routes.postLoginDetails_url)
                .then()
                .statusCode(statusCode)
                .extract().response();

    }




}
