package com.automationexercise.api.tests.productsListTests;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchProduct {

    static Response response;
    static int actualStatusCode = 0;
    public final static int expectedStatusCode = 200; // Entered by tester
    static String searchProduct = "tshirt";
    final static String queryParam = "search_product";


    // NOT WORKING API AT ALL EVEN IN POSTMAN


//    @BeforeAll
//    static void init() {
//        response = given()
//                .contentType("application/json")
//                .body("{ \"search_product\": \"" + searchProduct + "\" }")
//                .when()
//                .post(Routes.postSearchProduct_url)
//                .then()
//                .log().all()
//                .statusCode(expectedStatusCode)
//                .body("$", hasSize(5))
//                .body("[0].name", equalTo("Blue T-Shirt"))
//                .extract().response();
//        actualStatusCode = response.getStatusCode();
//    }

//    @TestTemplate
//    @DisplayName("Testing search product")
//    @ParameterizedTest(name = "{index} - Product name: {name}")
//    @CsvFileSource(files = "src\\test\\resources\\SearchProducts.csv", numLinesToSkip = 1)
//    public void testBrandList(String name) {
//
//        //try {
//        assertThat(response.jsonPath().getString("products.find { it.name == " + name + "}.name"), equalTo(name));
//
////        } catch (AssertionError e) {
////            System.out.println("AssertionError: " + e.getMessage());
////        }
//    }

//    @Test
//    @DisplayName("Test")
//    void test2() {
//        assertThat(response.jsonPath().getString("products.name"), equalTo("tshirt"));
//    }
//
//
//
//    @Test
//    @DisplayName("Test")
//    void test() {
//        assertThat(actualStatusCode, equalTo(expectedStatusCode));
//    }

}
