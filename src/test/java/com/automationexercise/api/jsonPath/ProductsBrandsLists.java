package com.automationexercise.api.jsonPath;

import com.automationexercise.api.endpoints.Routes;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class ProductsBrandsLists {
    static String searchedProduct = "tops";
    static Response response;
    JsonPath expectedJsonPath;
    static String pathToJson = "src/test/resources/ExpectedProductsList.json";

    static void getResponse(String url) {
        response = given()
                .when()
                .get(url)
                .then()
                .extract().response();
    }

    static void postResponse(String url) {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .when()
                .post(url)
                .then()
                .extract().response();
    }

    static void postResponse(String url, String product) {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams("search_product", product)
                .when()
                .post(url)
                .then()
                .extract().response();
    }

    static void putResponse(String url) {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .when()
                .put(url)
                .then()
                .extract().response();
    }


    @Order(1)
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GetAllProductsList {
        @TestFactory
        @DisplayName("Validate each product")
        List<DynamicTest> validateEachProduct() {
            getResponse(Routes.getProducts_url);

            String expectedJson;
            try {
                expectedJson = new String(Files.readAllBytes(Paths.get(pathToJson)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            expectedJsonPath = new JsonPath(expectedJson);

            List<String> productIds = response.jsonPath().getList("products.id", String.class);
            List<Integer> productIdInts = productIds.stream().map(Integer::parseInt).toList();

            List<DynamicTest> dynamicTests = new ArrayList<>();
            for (Integer id : productIdInts) {
                dynamicTests.add(DynamicTest.dynamicTest("Validate product with ID " + id, () -> {
                    assertThat(response.jsonPath().getString("products.find { it.id == " + id + "}.id"), equalTo(expectedJsonPath.getString("products.find { it.id == " + id + "}.id")));
                    assertThat(response.jsonPath().getString("products.find { it.id == " + id + "}.name"), equalTo(expectedJsonPath.getString("products.find { it.id == " + id + "}.name")));
                    assertThat(response.jsonPath().getString("products.find { it.id == " + id + "}.price"), equalTo(expectedJsonPath.getString("products.find { it.id == " + id + "}.price")));
                    assertThat(response.jsonPath().getString("products.find { it.id == " + id + "}.brand"), equalTo(expectedJsonPath.getString("products.find { it.id == " + id + "}.brand")));
                    assertThat(response.jsonPath().getString("products.find { it.id == " + id + "}.category.usertype.usertype"), equalTo(expectedJsonPath.getString("products.find { it.id == " + id + "}.category.usertype.usertype")));
                    assertThat(response.jsonPath().getString("products.find { it.id == " + id + "}.category.category"), equalTo(expectedJsonPath.getString("products.find { it.id == " + id + "}.category.category")));
                }));
            }
            return dynamicTests;
        }

        @Order(2)
        @Test
        @DisplayName("Test response code")
        void testRespondMessage() {
            getResponse(Routes.getProducts_url);
            assertThat(response.jsonPath().getString("responseCode"), equalTo("200"));
        }

    }

    @Order(3)
    @Nested
    class PostToAllProductsList {
        @Order(1)
        @Test
        @DisplayName("Test Post to all products response message")
        void testResponseMessage() {
            postResponse(Routes.postProducts_url);
            assertThat(response.jsonPath().getString("message"), equalTo("This request method is not supported."));
        }

        @Order(2)
        @Test
        @DisplayName("Test Post to all products response code")
        void testResponseCode() {
            postResponse(Routes.postProducts_url);
            assertThat(response.jsonPath().getString("responseCode"), equalTo("405"));
        }
    }

    @Order(4)
    @Nested
    class PostToSearchProduct {

        @Order(1)
        @Test
        @DisplayName("Test Post to search product response message")
        void testResponseMessage() {
            postResponse(Routes.postSearchProduct_url, searchedProduct); // Will save into a List response and check category with expected
            List<String> productTypeList = response.jsonPath().getList("products.category.category");
            assertTrue(productTypeList.stream().anyMatch(element -> element.toLowerCase().contains(searchedProduct)));
        }

        @Order(2)
        @Test
        @DisplayName("Test Post to search product response code")
        void testResponseCode() {
            postResponse(Routes.postSearchProduct_url, searchedProduct);
            assertThat(response.jsonPath().getString("responseCode"), equalTo("200"));
        }

        @Order(3)
        @Test
        @DisplayName("Test Post to search product without parameter response code")
        void testSearchProductWithoutParameterResponseCode() {
            postResponse(Routes.postSearchProduct_url);
            assertThat(response.jsonPath().getString("responseCode"), equalTo("400"));
        }

        @Order(4)
        @Test
        @DisplayName("Test Post to search product without parameter response message")
        void testSearchProductWithoutParameterResponseMessage() {
            postResponse(Routes.postSearchProduct_url);
            assertThat(response.jsonPath().getString("message"), equalTo("Bad request, search_product parameter is missing in POST request."));
        }

    }

    @Order(6)
    @Nested
    class GetAllBrandsList {

        @Order(1)
        @Test
        @DisplayName("Get the brands list")
        void getTheProductsList() {
            getResponse(Routes.getBrands_url);

            String expectedJson;
            try {
                expectedJson = new String(Files.readAllBytes(Paths.get("src/test/resources/ExpectedBrandsList.json")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JsonPath expectedJsonPath = new JsonPath(expectedJson);

            List<String> brandIds = response.jsonPath().getList("brands.id", String.class);
            List<Integer> brandIdInts = brandIds.stream().map(Integer::parseInt).toList();
            for (Integer id : brandIdInts) {
                System.out.println("Validating brand with ID " + id);

                assertThat(response.jsonPath().getString("brands.find { it.id == " + id + "}.id"), equalTo(expectedJsonPath.getString("brands.find { it.id == " + id + "}.id")));
                assertThat(response.jsonPath().getString("brands.find { it.id == " + id + "}.brand"), equalTo(expectedJsonPath.getString("brands.find { it.id == " + id + "}.brand")));
            }
        }

        @Order(2)
        @Test
        @DisplayName("Test response code")
        void testRespondMessage() {
            assertThat(response.jsonPath().getString("responseCode"), equalTo("200"));
        }

    }

    @Order(7)
    @Nested
    class PutToAllBrandsList {
        @Order(1)
        @Test
        @DisplayName("Test response message should be method not supported")
        void testResponseMessageShouldBeNotSupported() {
            putResponse(Routes.putBrands_url);
            assertThat(response.jsonPath().getString("message"), equalTo("This request method is not supported."));
        }
        @Order(2)
        @Test
        @DisplayName("Test response code should be 405")
        void testResponseCodeShouldBe405() {
            putResponse(Routes.putBrands_url);
            assertThat(response.jsonPath().getString("responseCode"), equalTo("405"));
        }




    }
}


