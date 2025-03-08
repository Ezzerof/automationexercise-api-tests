package com.automationexercise.api.csv_path.products_list_tests;

import com.automationexercise.api.config.Config;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * <h1>PostToSearchProductTest</h1>
 *
 * <p>This test class verifies the behavior of the POST method on the Search Product API endpoint.</p>
 *
 * <p><strong>API Under Test:</strong></p>
 * <ul>
 *   <li><strong>Endpoint:</strong> <code>{Config.BASE_URL}{Config.SEARCH_PRODUCT_ENDPOINT}</code></li>
 *   <li><strong>Method:</strong> POST</li>
 *   <li><strong>Request Parameter:</strong> search_product (e.g., "top", "tshirt", "jean")</li>
 *   <li><strong>Expected HTTP Status Code:</strong> 200</li>
 *   <li><strong>Expected Response JSON:</strong> A list of searched products</li>
 * </ul>
 *
 * <p>The tests in this class perform the following verifications:</p>
 * <ol>
 *   <li>Send a POST request to the search product endpoint with a product search term from a CSV file.</li>
 *   <li>Validate that the returned list of products contains at least one entry whose category matches the search term.</li>
 *   <li>Validate that the JSON response includes the expected response code (200) as a string.</li>
 *   <li>Assert that the HTTP status code is 200.</li>
 *   <li>Optional: Additional tests can be added to check for empty result sets, proper header values, etc.</li>
 * </ol>
 *
 * <p><strong>CSV Data:</strong></p>
 * <p>The CSV file (<code>{Config.SEARCH_PRODUCT_CSV_PATH}</code>) should contain a header with "Product name" and one or more rows. For example:</p>
 * <pre>
 * Product name
 * tops
 * tshirt
 * jean
 * dress
 * </pre>
 *
 * <p>This allows testing various search scenarios by simply adding or modifying CSV rows.</p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostToSearchProductTest {

    private Response response;
    private String searchedProduct;

    /**
     * Parameterized test that sends a POST request to search for a product.
     * Each CSV row provides a different search term.
     *
     * @param product the search term from the CSV file
     */
    @Order(1)
    @ParameterizedTest(name = "{index} - Search for product: {0}")
    @CsvFileSource(files = Config.SEARCH_PRODUCT_CSV_PATH, numLinesToSkip = 1)
    @DisplayName("Send POST request to search for product")
    void sendPostRequestToSearchProduct(String product) {
        searchedProduct = product;
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams("search_product", product)
                .post(Config.BASE_URL + Config.SEARCH_PRODUCT_ENDPOINT)
                .then()
                .extract().response();
        System.out.println("Search Response for \"" + product + "\":\n" + response.getBody().asString());
    }

    /**
     * Validates that the returned list contains at least one product where the category
     * matches the searched product (case-insensitive).
     */
    @Order(2)
    @Test
    @DisplayName("Validate returned list contains searched product")
    void validateReturnedListContainsSearchedProduct() {
        // Extract list of categories from the response.
        List<String> productCategoryList = response.jsonPath().getList("products.category.category");
        // Assert that at least one category (case-insensitive) contains the searched product.
        boolean found = productCategoryList != null && productCategoryList.stream()
                .anyMatch(category -> category.toLowerCase().contains(searchedProduct.toLowerCase()));
        if (!found) {
            fail("Expected search term \"" + searchedProduct + "\" not found in any product categories.");
        }
    }

    /**
     * Validates that the JSON response includes the expected response code.
     */
    @Order(3)
    @Test
    @DisplayName("Validate JSON response code is 200")
    void validateResponseCode() {
        String responseCode = response.jsonPath().getString("responseCode");
        assertThat("Expected JSON response code '200'!", responseCode, equalTo(String.valueOf(Config.EXPECTED_STATUS_CODE)));
    }

    /**
     * Validates that the HTTP status code is 200.
     */
    @Order(4)
    @Test
    @DisplayName("Validate HTTP status code is 200")
    void validateStatusCode() {
        int statusCode = response.getStatusCode();
        assertThat("Expected HTTP status code 200!", statusCode, equalTo(Config.EXPECTED_STATUS_CODE));
    }

    /**
     * Additional Test: Validate that the response Content-Type header is as expected.
     */
    @Order(5)
    @Test
    @DisplayName("Validate Content-Type header")
    void validateContentTypeHeader() {
        String contentType = response.header("content-type");
        assertThat("Expected Content-Type header to be " + Config.EXPECTED_CONTENT_TYPE,
                contentType, equalTo(Config.EXPECTED_CONTENT_TYPE));
    }

    /**
     * Additional Test: Validate that the list of products is not empty.
     */
    @Order(6)
    @Test
    @DisplayName("Validate that the search returns at least one product")
    void validateNonEmptyProductList() {
        List<?> products = response.jsonPath().getList("products");
        assertThat("Expected at least one product in the search results", products.size(), is(notNullValue()));
        assertFalse(products.isEmpty(), "Product list is empty!");
    }
}
