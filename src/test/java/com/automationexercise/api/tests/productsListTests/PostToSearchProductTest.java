package com.automationexercise.api.tests.productsListTests;

import com.automationexercise.api.endpoints.Routes;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostToSearchProductTest {

    public static Response response;
    static String searchedProduct;

    @TestTemplate
    @Order(1)
    @DisplayName("Post to search product")
    @ParameterizedTest(name = "{index} - Brand id: {0}")
    @CsvFileSource(files = "src\\test\\resources\\SearchProduct.csv", numLinesToSkip = 1)
    public void init(String product) {
        searchedProduct = product;
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams("search_product", product)
                .post(Routes.postSearchProduct_url);

    }

    @Test
    @Order(2)
    @DisplayName("Test returned list that contains searched product")
    void testReturnedList() {
        List<String> productTypeList = response.jsonPath().getList("products.category.category");
        assertTrue(productTypeList.stream().anyMatch(element -> element.toLowerCase().contains(searchedProduct)));
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
