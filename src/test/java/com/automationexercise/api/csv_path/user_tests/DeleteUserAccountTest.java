package com.automationexercise.api.csv_path.user_tests;

import com.automationexercise.api.config.Config;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * <h1>DeleteUserAccountTest</h1>
 *
 * <p>This test class verifies the functionality of the DELETE method for user accounts on AutomationExercise.</p>
 *
 * <p><strong>API Under Test:</strong></p>
 * <ul>
 *   <li><strong>Endpoint:</strong> <code>{Config.BASE_URL}{Config.DELETE_ACCOUNT_ENDPOINT}</code></li>
 *   <li><strong>Method:</strong> DELETE</li>
 *   <li><strong>Request Parameters:</strong> email, password</li>
 *   <li><strong>Expected Response Code (in JSON):</strong> 200</li>
 *   <li><strong>Expected Response Message:</strong> "Account deleted!"</li>
 *   <li><strong>Expected HTTP Status Code:</strong> 200</li>
 *   <li><strong>Expected Content-Type:</strong> Defined in <code>Config.EXPECTED_CONTENT_TYPE</code></li>
 * </ul>
 *
 * <p>The tests in this class perform the following validations:</p>
 * <ol>
 *   <li><strong>Pre-test Setup:</strong> The <code>@BeforeAll</code> method creates test users using data from a CSV file
 *       (<code>Config.CREATE_USER_CSV_PATH</code>). This ensures that accounts exist before deletion. If creation fails,
 *       the tests fail early.</li>
 *   <li><strong>Delete Operation:</strong> A parameterized test sends a DELETE request for each user provided in the CSV file
 *       (<code>Config.DELETE_USER_CSV_PATH</code>).</li>
 *   <li><strong>Response Validations:</strong> Separate tests verify that the response message, JSON response code, and
 *       HTTP status code are as expected.</li>
 *   <li><strong>Additional Validations:</strong> Checks such as ensuring the Content-Type header and JSON structure are correct.</li>
 * </ol>
 *
 * <p>This comprehensive testing approach ensures that user account deletion behaves as expected and the API's
 * response adheres to the defined contract.</p>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteUserAccountTest {

    private Response response;

    /**
     * <p><strong>Pre-test Setup:</strong> Create test users using data from the creation CSV file
     * (<code>Config.CREATE_USER_CSV_PATH</code>) to ensure that accounts exist prior to deletion.</p>
     *
     * <p>If the deletion of any pre-existing user fails with an unexpected message (i.e. not "Account not found!"),
     * the test fails early.</p>
     */
    @BeforeAll
    @DisplayName("Pre-test Setup: Create test users")
    void createTestUsers() throws Exception {
        // Read the CSV file.
        List<String> lines = Files.readAllLines(Paths.get(Config.CREATE_USER_CSV_PATH));
        // Skip header (index 0) and iterate through the rest of the lines
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] fields = line.split(",");
            String name = fields[0].trim();
            String email = fields[1].trim();
            String password = fields[2].trim();
            String title = fields[3].trim();
            String birthDay = fields[4].trim();
            String birthMonth = fields[5].trim();
            String birthYear = fields[6].trim();
            String firstName = fields[7].trim();
            String lastName = fields[8].trim();
            String company = fields[9].trim();
            String address1 = fields[10].trim();
            String address2 = fields[11].trim();
            String country = fields[12].trim();
            String zipcode = fields[13].trim();
            String state = fields[14].trim();
            String city = fields[15].trim();
            String mobileNumber = fields[16].trim();
            Response createResponse = given()
                    .log().all()
                    .contentType("application/x-www-form-urlencoded")
                    .formParams(
                            "name", name,
                            "email", email,
                            "password", password,
                            "title", title,
                            "birth_date", birthDay,
                            "birth_month", birthMonth,
                            "birth_year", birthYear,
                            "firstname", firstName,
                            "lastname", lastName,
                            "company", company,
                            "address1", address1,
                            "address2", address2,
                            "country", country,
                            "zipcode", zipcode,
                            "state", state,
                            "city", city,
                            "mobile_number", mobileNumber
                    )
                    .post(Config.BASE_URL + Config.CREATE_ACCOUNT_ENDPOINT)
                    .then()
                    .extract().response();
            String createMessage = createResponse.jsonPath().getString("message");
            System.out.println("Create response for " + email + ": " + createResponse.getBody().asString());

            // Fail early if the creation response is not as expected.
            if (!("User created!".equals(createMessage) || "User already exists!".equals(createMessage))) {
                fail(String.format("User creation failed for %s. Received response: %s", email, createMessage));
            }
        }
    }

    /**
     * <p><strong>Delete Test:</strong> This parameterized test reads user data from the deletion CSV file
     * (<code>Config.DELETE_USER_CSV_PATH</code>) and sends a DELETE request for each account.</p>
     *
     * <p>CSV Format:</p>
     * <pre>
     * Email,Password
     * katie@gmail.com,123456789
     * </pre>
     */
    @Order(1)
    @ParameterizedTest(name = "{index} - Delete account for Email: {0}")
    @CsvFileSource(files = Config.DELETE_USER_CSV_PATH, numLinesToSkip = 1)
    @DisplayName("Send DELETE request to delete user account")
    void sendDeleteRequestToDeleteAccount(String email, String password) {
        response = given()
                .log().all()
                .contentType("application/x-www-form-urlencoded")
                .formParams("email", email, "password", password)
                .delete(Config.BASE_URL + Config.DELETE_ACCOUNT_ENDPOINT)
                .then()
                .extract().response();
        System.out.println("Delete response for " + email + ":\n" + response.getBody().asString());
    }

    /**
     * Validates that the response message is "Account deleted!".
     */
    @Order(2)
    @Test
    @DisplayName("Validate response message: Account deleted!")
    void validateResponseMessage() {
        assertThat("Expected response message: 'Account deleted!'",
                response.jsonPath().getString("message"), equalTo(Config.EXPECTED_ACCOUNT_DELETED_MESSAGE));
    }

    /**
     * Validates that the JSON response contains the expected response code "200".
     */
    @Order(3)
    @Test
    @DisplayName("Validate JSON response code is 200")
    void validateResponseCode200() {
        assertThat("Expected JSON response code 200!",
                response.jsonPath().getString("responseCode"), equalTo(Config.EXPECTED_RESPONSE_CODE_200));
    }

    /**
     * Validates that the HTTP status code of the response is 200.
     */
    @Order(4)
    @Test
    @DisplayName("Validate HTTP status code is 200")
    void validateStatusCode200() {
        assertThat("Expected HTTP status code 200!",
                response.getStatusCode(), equalTo(Config.EXPECTED_STATUS_CODE));
    }

    /**
     * Additional test: Validates that the Content-Type header in the response matches the expected value.
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
     * Additional test: Validates that the JSON response structure contains only the keys "responseCode" and "message".
     */
    @Order(6)
    @Test
    @DisplayName("Validate JSON response structure")
    void validateResponseJsonStructure() {
        // Convert the JSON response into a Map
        java.util.Map<String, Object> responseMap = response.jsonPath().getMap("");
        Set<String> expectedKeys = new HashSet<>();
        expectedKeys.add("responseCode");
        expectedKeys.add("message");
        assertThat("JSON response structure does not match expected keys!",
                responseMap.keySet(), equalTo(expectedKeys));
    }

    /**
     * <p><strong>Cleanup:</strong> After all tests have run, this method deletes all test users using data
     * from the creation CSV file (<code>Config.CREATE_USER_CSV_PATH</code>).</p>
     *
     * <p>If a deletion does not return the expected "User deleted!" message, a warning is logged.</p>
     */
    @AfterAll
    @DisplayName("Cleanup: Delete test users")
    void cleanUpTestUsers() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Config.CREATE_USER_CSV_PATH));
        // Skip header (assumed at index 0)
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] fields = line.split(",");
            String email = fields[1].trim();
            String password = fields[2].trim();
            Response deleteResponse = given()
                    .log().all()
                    .contentType("application/x-www-form-urlencoded")
                    .formParams("email", email)
                    .formParam("password", password)
                    .delete(Config.BASE_URL + Config.DELETE_ACCOUNT_ENDPOINT)
                    .then()
                    .extract().response();
            System.out.println("Cleanup - Delete response for " + email + ":\n" + deleteResponse.getBody().asString());
            String deleteMessage = deleteResponse.jsonPath().getString("message");
            if (!"User deleted!".equals(deleteMessage)) {
                System.err.println(String.format("Warning: Deletion of user %s did not succeed as expected. Response: %s", email, deleteMessage));
            }
        }
    }
}