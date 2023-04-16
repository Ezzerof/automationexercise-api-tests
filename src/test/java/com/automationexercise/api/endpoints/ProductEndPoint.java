package com.automationexercise.api.endpoints;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ProductEndPoint {

    public static Response readProduct(String name) {
        return given()
                .param("name", name)
                .when()
                .post(Routes.postProducts_url);
    }

}
