package com.automationexercise.api.csv_path.products_list_tests;

import com.automationexercise.api.config.Config;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * <h1>PostProductListTest</h1>
 *
 * <p>This test class verifies the behavior of the POST method on the Products List API endpoint.</p>
 *
 * <p><strong>API Under Test:</strong></p>
 * <ul>
 *   <li><strong>Endpoint:</strong> <code>{Config.BASE_URL}{Config.PRODUCTS_ENDPOINT}</code></li>
 *   <li><strong>Method:</strong> POST</li>
 *   <li><strong>Expected Response Code:</strong> 405</li>
 *   <li><strong>Expected Response Message:</strong> "This request method is not supported."</li>
 *   <li><strong>HTTP Status Code:</strong> 200</li>
 *   <li><strong>Expected Content-Type:</strong> Defined in <code>Config.EXPECTED_CONTENT_TYPE</code></li>
 * </ul>
 *
 * <p>The tests in this class perform the following verifications:</p>
 * <ol>
 *   <li>Send a POST request to the <code>/productsList</code> endpoint.</li>
 *   <li>Validate that the JSON response contains the expected error message indicating the method is not supported.</li>
 *   <li>Validate that the JSON response field <code>responseCode</code> is "405".</li>
 *   <li>Assert that the HTTP status code is 200.</li>
 *   <li>Verify that the Content-Type header in the response matches the expected value.</li>
 * </ol>
 *
 * <p>This comprehensive set of tests ensures that the products list endpoint properly rejects POST requests
 * and adheres to the expected API contract.</p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostProductListTest {

    private Response response;

    /**
     * Setup: Send a POST request to the /productsList endpoint.
     * Since the endpoint does not support POST, we expect an error response.
     */
    @BeforeAll
    @DisplayName("Send POST request to /productsList endpoint")
    void init() {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .post(Config.BASE_URL + Config.PRODUCTS_ENDPOINT)
                .then()
                .extract().response();

        System.out.println("POST Response:\n" + response.getBody().asString());
    }

    /**
     * Validate that the response message indicates the POST method is not supported.
     */
    @Test
    @Order(1)
    @DisplayName("Validate response message: 'This request method is not supported.'")
    void validateResponseMessage() {
        String actualMessage = response.jsonPath().getString("message");
        assertThat("Expected response message 'This request method is not supported.'",
                actualMessage, equalTo(Config.EXPECTED_METHOD_NOT_SUPPORTED_MESSAGE));
    }

    /**
     * Validate that the JSON response contains the expected response code "405".
     */
    @Test
    @Order(2)
    @DisplayName("Validate JSON response code is '405'")
    void validateResponseCode() {
        String actualResponseCode = response.jsonPath().getString("responseCode");
        assertThat("Expected JSON response code '405'!",
                actualResponseCode, equalTo(Config.EXPECTED_RESPONSE_CODE_405));
    }

    /**
     * Validate that the HTTP status code of the response is 200.
     */
    @Test
    @Order(3)
    @DisplayName("Validate HTTP status code is 200")
    void validateStatusCode() {
        int actualStatusCode = response.getStatusCode();
        assertThat("Expected HTTP status code 200!", actualStatusCode, equalTo(Config.EXPECTED_STATUS_CODE));
    }

    /**
     * Validate that the Content-Type header in the response matches the expected value.
     */
    @Test
    @Order(4)
    @DisplayName("Validate Content-Type header")
    void validateContentTypeHeader() {
        String actualContentType = response.header("content-type");
        assertThat("Expected Content-Type header to be " + Config.EXPECTED_CONTENT_TYPE,
                actualContentType, equalTo(Config.EXPECTED_CONTENT_TYPE));
    }
}
