package com.automationexercise.api.csv_path.products_list_tests;

import com.automationexercise.api.config.Config;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * <h1>GetProductsListTest</h1>
 *
 * <p>This test class verifies the integrity of the products list returned by the
 * GET <code>/productsList</code> API endpoint from AutomationExercise.</p>
 *
 * <p><strong>API Under Test:</strong></p>
 * <ul>
 *   <li><strong>Endpoint:</strong> <code>{Config.BASE_URL}{Config.PRODUCTS_ENDPOINT}</code></li>
 *   <li><strong>Method:</strong> GET</li>
 *   <li><strong>Expected HTTP Status Code:</strong> 200</li>
 *   <li><strong>Expected List Size:</strong> Defined in <code>Config.EXPECTED_LIST_SIZE</code></li>
 *   <li><strong>Expected Server:</strong> Defined in <code>Config.EXPECTED_SERVER_NAME</code></li>
 *   <li><strong>Expected Content-Type:</strong> Defined in <code>Config.EXPECTED_CONTENT_TYPE</code></li>
 * </ul>
 *
 * <p>The tests in this class perform the following validations:</p>
 * <ol>
 *   <li>Validate each product's details (ID, name, price, brand, user type, category)
 *       against expected values provided in a CSV file (<code>Config.EXPECTED_PRODUCTS_DETAILS_CSV_PATH</code>).</li>
 *   <li>Check that the HTTP status code is 200.</li>
 *   <li>Ensure the total number of products matches the expected size.</li>
 *   <li>Verify that the server header in the response matches the expected value.</li>
 *   <li>Verify that the Content-Type header is as expected.</li>
 *   <li>Ensure that none of the product names are empty.</li>
 *   <li>Ensure that all product IDs in the response are unique.</li>
 *   <li>Validate that the Content-Type header is correct.</li>
 * </ol>
 *
 * <p>This comprehensive set of tests ensures that the products list endpoint returns the correct data
 * and adheres to expected standards, thereby providing confidence in the API's reliability and data integrity.</p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetProductsListTest {
    private Response response;
    /**
     * Setup: Execute a GET request to the /productsList endpoint before all tests.
     * The response is stored for reuse in subsequent tests.
     */
    @BeforeAll
    void setup() {
        response = given()
                .when()
                .get(Config.BASE_URL + Config.PRODUCTS_ENDPOINT)
                .then()
                .extract().response();

        System.out.println("ProductsList Response Body:\n" + response.getBody().asString());
    }

    /**
     * Parameterized test that validates each product's details from the CSV file.
     * Each row in the CSV must contain the following columns (in order):
     * <pre>
     * Brand ID, Name, Price, Brand, Usertype, Category
     * </pre>
     */
    @Order(1)
    @ParameterizedTest(name = "{index} - Product id: {0}, Product name: {1}")
    @CsvFileSource(files = Config.EXPECTED_PRODUCTS_DETAILS_CSV_PATH, numLinesToSkip = 1)
    @DisplayName("Validate product details against CSV data")
    void testProductsDetails(String id, String name, String price, String brand, String usertype, String category) {
        // Validate product ID
        String actualId = response.jsonPath().getString("products.find { it.id == " + id + "}.id");
        assertThat("Brand ID mismatch!", actualId, equalTo(id));

        // Validate product name
        String actualName = response.jsonPath().getString("products.find { it.id == " + id + "}.name");
        assertThat("Product name mismatch for ID " + id, actualName, equalTo(name));

        // Validate product price
        String actualPrice = response.jsonPath().getString("products.find { it.id == " + id + "}.price");
        assertThat("Product price mismatch for ID " + id, actualPrice, equalTo(price));

        // Validate brand
        String actualBrand = response.jsonPath().getString("products.find { it.id == " + id + "}.brand");
        assertThat("Product brand mismatch for ID " + id, actualBrand, equalTo(brand));

        // Validate usertype (nested JSON)
        String actualUsertype = response.jsonPath().getString("products.find { it.id == " + id + "}.category.usertype.usertype");
        assertThat("Product usertype mismatch for ID " + id, actualUsertype, equalTo(usertype));

        // Validate category
        String actualCategory = response.jsonPath().getString("products.find { it.id == " + id + "}.category.category");
        assertThat("Product category mismatch for ID " + id, actualCategory, equalTo(category));
    }

    /**
     * Validate that the HTTP status code returned by the GET request is 200.
     */
    @Order(2)
    @Test
    @DisplayName("Validate HTTP status code is 200")
    void validateStatusCode() {
        assertThat("Expected status code 200!", response.getStatusCode(), equalTo(Config.EXPECTED_STATUS_CODE));
    }

    /**
     * Validate that the total number of products in the response matches the expected size.
     */
    @Order(3)
    @Test
    @DisplayName("Validate products list size")
    void validateListSize() {
        List<String> productNames = response.jsonPath().getList("products.name");
        assertThat("Expected list size of " + Config.EXPECTED_LIST_SIZE, productNames.size(), equalTo(Config.EXPECTED_LIST_SIZE));
    }

    /**
     * Validate that the 'server' header in the response matches the expected value.
     */
    @Order(4)
    @Test
    @DisplayName("Validate server header")
    void validateServerName() {
        String serverHeader = response.getHeaders().get("server").toString();
        assertThat("Expected server header to be 'Server=" + Config.EXPECTED_SERVER_NAME + "'",
                serverHeader, equalTo("Server=" + Config.EXPECTED_SERVER_NAME));
    }

    /**
     * Validate that the 'content-type' header in the response matches the expected value.
     */
    @Order(5)
    @Test
    @DisplayName("Validate content-type header")
    void validateContentType() {
        String contentType = response.header("content-type");
        assertThat("Expected Content-Type to be " + Config.EXPECTED_CONTENT_TYPE,
                contentType, equalTo(Config.EXPECTED_CONTENT_TYPE));
    }

    /**
     * Additional test: Ensure that all product names in the response are not empty.
     */
    @Order(6)
    @Test
    @DisplayName("Validate that all product names are non-empty")
    void validateNonEmptyProductNames() {
        List<String> allProductNames = response.jsonPath().getList("products.name");
        for (String productName : allProductNames) {
            assertFalse(productName == null || productName.trim().isEmpty(),
                    "Found an empty product name in the response!");
        }
    }

    /**
     * Additional test: Verify that all product IDs in the response are unique.
     */
    @Order(7)
    @Test
    @DisplayName("Validate uniqueness of product IDs")
    void validateUniqueProductIds() {
        List<Integer> productIds = response.jsonPath().getList("products.id", Integer.class);
        Set<Integer> uniqueIds = new HashSet<>(productIds);
        assertThat("Duplicate product IDs found!", uniqueIds.size(), is(productIds.size()));
    }

    /**
     * Additional test: Verify that the price field is not empty and is a valid number.
     * This can help catch data issues where the price might be missing or improperly formatted.
     */
    @Order(8)
    @Test
    @DisplayName("Validate that product prices are valid numbers and non-empty")
    void validateProductPrices() {
        List<String> prices = response.jsonPath().getList("products.price");
        for (String price : prices) {
            assertFalse(price == null || price.trim().isEmpty(), "Found an empty price for a product!");

            // Remove non-numeric characters (except the decimal point)
            String cleanedPrice = price.replaceAll("[^0-9.]", "");
            if (cleanedPrice.isEmpty()) {
                fail("Price does not contain numeric characters: " + price);
            }
            try {
                Double.parseDouble(cleanedPrice);
            } catch (NumberFormatException e) {
                fail("Price is not a valid number after cleaning: " + cleanedPrice);
            }
        }
    }

}
