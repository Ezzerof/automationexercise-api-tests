package com.automationexercise.api.tests2;

import com.automationexercise.api.endpoints.Routes;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class ProductsBrandsLists {

    static Response response;
    static Map<String, String> items;
    static String stringResponse;
    static String pathToJson = "src/test/resources/ProductsList.json";


    @Order(1)
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GetAllProductsList {
        @Order(1)
        @Test
        @DisplayName("Get the products list")
        void getTheProductsList() {

            response = given()
                    .when()
                    .get(Routes.getProducts_url)
                    .then()
                    .extract().response();

            stringResponse = response.asString();
            JsonParser.writeResponseIntoJson(stringResponse, pathToJson);

        }


    }

}
