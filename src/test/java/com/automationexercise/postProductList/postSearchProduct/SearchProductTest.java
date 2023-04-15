package com.automationexercise.postProductList.postSearchProduct;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class SearchProductTest {

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

}
