package com.automationexercise.api.csv_path.user_tests;

import com.automationexercise.api.config.Config;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * <h1>ValidLoginTest</h1>
 *
 * <p>This test class verifies the functionality of the POST method on the Verify Login API endpoint
 * for valid user credentials on AutomationExercise.</p>
 *
 * <p><strong>API Under Test:</strong></p>
 * <ul>
 *   <li><strong>Endpoint:</strong> <code>{Config.BASE_URL}{Config.LOGIN_ENDPOINT}</code></li>
 *   <li><strong>Method:</strong> POST</li>
 *   <li><strong>Request Parameters:</strong> email, password</li>
 *   <li><strong>Expected JSON Response:</strong>
 *       - Response Code: "200"
 *       - Response Message: "User exists!"</li>
 *   <li><strong>Expected HTTP Status Code:</strong> 200</li>
 * </ul>
 *
 * <p>The test class includes the following steps:</p>
 * <ol>
 *   <li><strong>Setup:</strong> Create test users using data from a CSV file (<code>Config.CREATE_USER_CSV_PATH</code>)
 *       to ensure that user accounts exist before testing valid logins. If creation fails, tests will fail early.</li>
 *   <li><strong>Valid Login Test:</strong> A parameterized test reads valid login credentials from a CSV file
 *       (<code>Config.VALID_LOGIN_CSV_PATH</code>) and sends a POST request to the Verify Login endpoint.</li>
 *   <li><strong>Response Validations:</strong> The test verifies that the API returns:
 *     <ul>
 *       <li>The expected response message ("User exists!")</li>
 *       <li>The expected JSON response code ("200")</li>
 *       <li>The expected HTTP status code (200)</li>
 *     </ul>
 *   </li>
 *   <li><strong>Cleanup:</strong> After all tests, the created test users are deleted using the data from the creation CSV.</li>
 * </ol>
 *
 * <p>This comprehensive test suite demonstrates robust, data-driven API testing practices and is designed to be
 * clear and maintainable.</p>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ValidLoginTest {

    private Response response;

    /**
     * Create test users from the creation CSV.
     * Fail early if user creation fails.
     */
    @BeforeAll
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
     * Parameterized test that verifies valid login using credentials from the CSV file
     * (<code>Config.VALID_LOGIN_CSV_PATH</code>).
     * Each row represents a distinct test case with potential variations in email formatting.
     *
     * CSV Format:
     * <pre>
     * Email,Password
     * katie@gmail.com,123456789
     * KATIE@GMAIL.COM,123456789
     *  katie@gmail.com ,123456789
     * katie@gmail.com, 123456789
     * </pre>
     */
    @Order(1)
    @ParameterizedTest(name = "{index} - Verify valid login for email: {0}")
    @CsvFileSource(files = Config.VALID_LOGIN_CSV_PATH, numLinesToSkip = 1)
    @DisplayName("Verify login with valid credentials")
    void verifyLoginWithValidCredentials(String email, String password) {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams("email", email, "password", password)
                .post(Config.BASE_URL + Config.LOGIN_ENDPOINT)
                .then()
                .extract().response();
        System.out.println("Response Body for " + email + ":\n" + response.getBody().asString());

        // Validate expected response for valid login
        assertThat("Expected response message: 'User exists!'",
                response.jsonPath().getString("message"), equalTo(Config.USER_EXISTS_MESSAGE));
        assertThat("Expected JSON response code 200!",
                response.jsonPath().getString("responseCode"), equalTo(Config.EXPECTED_RESPONSE_CODE_200));
        assertThat("Expected HTTP status code 200!",
                response.getStatusCode(), equalTo(Config.EXPECTED_STATUS_CODE));
    }

    /**
     * <p><strong>Cleanup:</strong> After all tests, delete the test users using the creation CSV file
     * (<code>Config.CREATE_USER_CSV_PATH</code>).</p>
     *
     * <p>This ensures that the environment is reset for future test runs.</p>
     */
    @AfterAll
    @DisplayName("Cleanup: Delete test users")
    void cleanUpTestUsers() throws Exception {
        // Read the CSV file with user creation data.
        List<String> lines = Files.readAllLines(Paths.get(Config.CREATE_USER_CSV_PATH));
        for (int i = 1; i < lines.size(); i++) {
            String[] fields = lines.get(i).split(",");
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
            System.out.println("Delete response for " + email + ": " + deleteResponse.getBody().asString());
            String deleteMsg = deleteResponse.jsonPath().getString("message");
            if (!"Account deleted!".equals(deleteMsg)) {
                System.err.println(String.format("Warning: Deletion of user %s did not succeed. Response: %s", email, deleteMsg));
            }
        }
    }
}
