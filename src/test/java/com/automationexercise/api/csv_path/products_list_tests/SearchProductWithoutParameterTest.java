package com.automationexercise.api.csv_path.products_list_tests;

import com.automationexercise.api.config.Config;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
/**
 * <h1>SearchProductWithoutParameterTest</h1>
 *
 * <p>This test class verifies the behavior of the POST method on the Search Product API endpoint when the required
 * parameter <code>search_product</code> is missing.</p>
 *
 * <p><strong>API Under Test:</strong></p>
 * <ul>
 *   <li><strong>Endpoint:</strong> <code>{Config.BASE_URL}{Config.SEARCH_PRODUCT_ENDPOINT}</code></li>
 *   <li><strong>Method:</strong> POST</li>
 *   <li><strong>Expected Response Code (in JSON):</strong> 400</li>
 *   <li><strong>Expected Response Message:</strong> "Bad request, search_product parameter is missing in POST request."</li>
 *   <li><strong>Expected HTTP Status Code:</strong> 200</li>
 * </ul>
 *
 * <p>The tests in this class perform the following validations:</p>
 * <ol>
 *   <li>Send a POST request to the search product endpoint without providing the <code>search_product</code> parameter.</li>
 *   <li>Verify that the response message matches the expected error message.</li>
 *   <li>Verify that the JSON response field <code>responseCode</code> is "400".</li>
 *   <li>Assert that the HTTP status code is 200.</li>
 * </ol>
 *
 * <p>This documentation and the associated tests provide a clear, professional example of negative testing
 * for required parameters in an API.</p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchProductWithoutParameterTest {

    private Response response;

    /**
     * Sends a POST request to the search product endpoint without the required
     * <code>search_product</code> parameter.
     * This setup method is executed once before all tests in the class.
     */
    @BeforeAll
    @DisplayName("Send POST request without search_product parameter")
    void sendPostRequestToSearchProduct() {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .post(Config.BASE_URL + Config.SEARCH_PRODUCT_ENDPOINT)
                .then()
                .extract().response();

        System.out.println("Search Product (Missing Parameter) Response:\n" + response.getBody().asString());
    }

    /**
     * Validates that the response message indicates a bad request due to a missing
     * <code>search_product</code> parameter.
     */
    @Test
    @Order(1)
    @DisplayName("Validate response message for missing search_product parameter")
    void validateResponseMessage() {
        String actualMessage = response.jsonPath().getString("message");
        assertThat("Expected response message: Bad request, search_product parameter is missing in POST request.",
                actualMessage, equalTo(Config.BAD_REQUEST_PRODUCT_PARAMETER_IS_MISSING_MESSAGE));
    }

    /**
     * Validates that the JSON response contains the expected response code "400".
     */
    @Test
    @Order(2)
    @DisplayName("Validate JSON response code is 400")
    void validateResponseCode() {
        String actualResponseCode = response.jsonPath().getString("responseCode");
        assertThat("Expected JSON response code '400'!",
                actualResponseCode, equalTo(Config.EXPECTED_RESPONSE_CODE_400));
    }

    /**
     * Validates that the HTTP status code of the response is 200.
     * This ensures that even though the API returns a logical error in the body, the HTTP call itself succeeds.
     */
    @Test
    @Order(3)
    @DisplayName("Validate HTTP status code is 200")
    void validateStatusCode() {
        int actualStatusCode = response.getStatusCode();
        assertThat("Expected HTTP status code 200!", actualStatusCode, equalTo(Config.EXPECTED_STATUS_CODE));
    }
}
