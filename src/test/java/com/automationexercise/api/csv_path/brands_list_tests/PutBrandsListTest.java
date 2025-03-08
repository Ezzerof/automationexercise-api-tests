package com.automationexercise.api.csv_path.brands_list_tests;

import com.automationexercise.api.config.Config;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * <h1>PutToBrandsListTest</h1>
 *
 * <p>This test class verifies the behavior of the PUT method on the Brands List API endpoint.</p>
 *
 * <p><strong>API Under Test:</strong></p>
 * <ul>
 *   <li><strong>Endpoint:</strong> <code>{Config.BASE_URL}{Config.BRANDS_ENDPOINT}</code></li>
 *   <li><strong>Method:</strong> PUT</li>
 *   <li><strong>Expected Response Code:</strong> 405</li>
 *   <li><strong>Expected Response Message:</strong> "This request method is not supported."</li>
 *   <li><strong>HTTP Status Code:</strong> 200</li>
 *   <li><strong>Expected Content-Type:</strong> "text/html; charset=utf-8"</li>
 * </ul>
 *
 * <p>The tests in this class perform the following verifications:</p>
 * <ol>
 *   <li>Send a PUT request to the /brandsList endpoint and log the response.</li>
 *   <li>Validate that the response JSON contains the expected error message.</li>
 *   <li>Validate that the JSON response field "responseCode" is "405".</li>
 *   <li>Validate that the HTTP status code of the response is 200.</li>
 *   <li>Validate that the Content-Type header in the response matches the expected value.</li>
 *   <li>Validate that the JSON response structure contains only the expected keys ("responseCode" and "message").</li>
 * </ol>
 */


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PutBrandsListTest {

    public Response response;

    /**
     * Setup: Send a PUT request to the /brandsList endpoint.
     * The API is expected to return a JSON response indicating that the PUT method is not supported.
     */
    @BeforeAll
    @DisplayName("Send PUT request to /brandsList endpoint")
    void sendPutRequestToBrandsList() {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .when()
                .put(Config.BASE_URL + Config.BRANDS_ENDPOINT)
                .then()
                .extract().response();
        System.out.println("PUT Response:\n" + response.getBody().asString());
    }

    /**
     * Validate that the response message is exactly as expected.
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
     * Validate that the JSON response contains the expected response code '405'.
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
    void validateHttpStatusCode() {
        int actualStatusCode = response.getStatusCode();
        assertThat("Expected HTTP status code 200!", actualStatusCode, equalTo(Config.EXPECTED_STATUS_CODE));
    }

    /**
     * Validate that the Content-Type header matches the expected value.
     */
    @Test
    @Order(4)
    @DisplayName("Validate Content-Type header")
    void validateContentTypeHeader() {
        String actualContentType = response.header("content-type");
        assertThat("Expected Content-Type header to be " + Config.EXPECTED_CONTENT_TYPE,
                actualContentType, equalTo(Config.EXPECTED_CONTENT_TYPE));
    }

    /**
     * Validate that the JSON response structure contains exactly the keys 'responseCode' and 'message'.
     */
    @Test
    @Order(5)
    @DisplayName("Validate JSON response structure")
    void validateResponseJsonStructure() {
        Map<String, Object> responseMap = response.jsonPath().getMap("");
        Set<String> expectedKeys = new HashSet<>();
        expectedKeys.add("responseCode");
        expectedKeys.add("message");
        assertThat("JSON response structure does not match expected keys!",
                responseMap.keySet(), equalTo(expectedKeys));
    }
}
