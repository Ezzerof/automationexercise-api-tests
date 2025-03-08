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
 * <h1>CreateUserAccountTest</h1>
 *
 * <p>This test class verifies the functionality of the POST endpoint for creating (registering)
 * a user account on AutomationExercise.</p>
 *
 * <p><strong>API Under Test:</strong></p>
 * <ul>
 *   <li><strong>Endpoint:</strong> <code>{Config.BASE_URL}{Config.CREATE_ACCOUNT_ENDPOINT}</code></li>
 *   <li><strong>Method:</strong> POST</li>
 *   <li><strong>Request Parameters:</strong> name, email, password, title, birth_date, birth_month,
 *       birth_year, firstname, lastname, company, address1, address2, country, zipcode, state, city,
 *       mobile_number</li>
 *   <li><strong>Expected Response Code (JSON):</strong> 201</li>
 *   <li><strong>Expected Response Message:</strong> "User created!"</li>
 *   <li><strong>Expected HTTP Status Code:</strong> 200</li>
 * </ul>
 *
 * <p>The test class includes the following steps:</p>
 * <ol>
 *   <li><strong>Pre-test Cleanup:</strong> In the <code>@BeforeAll</code> method, it attempts to delete
 *       any existing test user accounts (using the creation CSV) to ensure a clean state. If the deletion
 *       does not return the expected "Account not found!" message, the test fails early.</li>
 *   <li><strong>Create User Account:</strong> A parameterized test reads user data from a CSV file
 *       (<code>Config.CREATE_USER_CSV_PATH</code>) and sends a POST request to create the user. The test
 *       fails if the response indicates that the user already exists or any unexpected response is received.</li>
 *   <li><strong>Validations:</strong> Separate tests validate that the response message, response code,
 *       and HTTP status code match the expected values.</li>
 *   <li><strong>Post-test Cleanup:</strong> In the <code>@AfterAll</code> method, the test class deletes
 *       the created test users to restore the system to its initial state, logging a warning if any deletion
 *       does not succeed as expected.</li>
 * </ol>
 *
 * <p>This comprehensive test suite demonstrates robust API testing practices and is designed to be clear
 * and maintainable.</p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateUserAccountTest {
    private Response response;

    /**
     * <p><strong>Pre-test Cleanup:</strong> Delete any existing test user accounts using data from the
     * creation CSV to ensure a clean state.</p>
     *
     * <p>The deletion is performed by reading the CSV file (<code>Config.CREATE_USER_CSV_PATH</code>),
     * and for each record, sending a DELETE request. The test fails early if the deletion response is not
     * as expected (i.e. "Account not found!" indicating no pre-existing account).</p>
     */
    @BeforeAll
    @DisplayName("Pre-test Cleanup: Delete existing test users")
    void deleteTestUsers() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Config.CREATE_USER_CSV_PATH));
        // Skip header (assumed at index 0) and iterate through each line
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] fields = line.split(",");
            // Extract email and password from the CSV
            String email = fields[1].trim();
            String password = fields[2].trim();
            Response deleteResponse = given()
                    .log().all()
                    .contentType("application/x-www-form-urlencoded")
                    .formParams("email", email, "password", password)
                    .delete(Config.BASE_URL + Config.DELETE_ACCOUNT_ENDPOINT)
                    .then()
                    .extract().response();
            String deleteMessage = deleteResponse.jsonPath().getString("message");
            System.out.println("Delete response for " + email + ": " + deleteResponse.getBody().asString());

            // If the deletion does not return "Account not found!", fail early
            if (!("Account not found!".equals(deleteMessage))) {
                fail(String.format("Pre-test cleanup failed for %s. Received response: %s", email, deleteMessage));
            }
        }
    }

    /**
     * <p><strong>Create User Account:</strong> This parameterized test sends a POST request to create
     * a user account using data from the CSV file (<code>Config.CREATE_USER_CSV_PATH</code>).</p>
     *
     * <p>If the response message indicates "Email already exists!", the test fails; if it receives an
     * unexpected response, it fails with a detailed message. Otherwise, it logs a success message.</p>
     *
     * <p>CSV format should be as follows:</p>
     * <pre>
     * Name,Email,Password,Title,Birth day,Birth month,Birth year,Firstname,Lastname,Company,Address 1,Address 2,Country,Zipcode,State,City,Mobile number
     * createTest,katie@gmail.com,123456789,Mrs,9,10,1996,Katie,Hulme,Sparta,in Uk 23,,Uk,19868,aa,London,89798416
     * </pre>
     */
    @Order(1)
    @ParameterizedTest(name = "{index} - Create account for Email: {1}")
    @CsvFileSource(files = Config.CREATE_USER_CSV_PATH, numLinesToSkip = 1)
    @DisplayName("Send POST request to create test accounts")
    void createTestAccounts(String name, String email, String password, String title, String birthDay,
                            String birthMonth, String birthYear, String firstName, String lastName,
                            String company, String address1, String address2, String country,
                            String zipcode, String state, String city, String mobileNumber) {

        response = given()
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

        // Retrieve the response message for logging and assertions.
        String message = response.jsonPath().getString("message");
        if ("Email already exists!".equals(message)) {
            fail("Test failed: Received 'User already exists!' when attempting to create user " + email);
        } else if ("User created!".equals(message)) {
            System.out.println("User created successfully: " + email);
        } else {
            fail(String.format("Test failed: Unexpected response: '%s' when attempting to create user %s", message, email));
        }

        System.out.println("Response Body:\n" + response.getBody().asString());
    }

    /**
     * Validates that the response message from the create account request is "User created!".
     */
    @Order(2)
    @Test
    @DisplayName("Validate response message: User created!")
    void validateResponseMessage() {
        assertThat("Expected response message 'User created!'",
                response.jsonPath().getString("message"), equalTo("User created!"));
    }

    /**
     * Validates that the JSON response contains the expected response code "201".
     */
    @Order(3)
    @Test
    @DisplayName("Validate response code: 201")
    void validateResponseCode201() {
        assertThat("Expected response code 201!",
                response.jsonPath().getString("responseCode"), equalTo("201"));
    }

    /**
     * Validates that the HTTP status code is 200.
     */
    @Order(4)
    @Test
    @DisplayName("Validate HTTP status code: 200")
    void validateStatusCode200() {
        assertThat("Expected HTTP status code 200!",
                response.getStatusCode(), equalTo(Config.EXPECTED_STATUS_CODE));
    }

    /**
     * <p><strong>Cleanup:</strong> Delete the test users created during the tests. This method is executed
     * after all tests have completed and reads the same CSV used for creation.</p>
     *
     * <p>It sends a DELETE request for each user and logs the response. If a deletion does not return the
     * expected "User deleted!" message, it logs a warning.</p>
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

            System.out.println("Delete response for " + email + ":\n" + deleteResponse.getBody().asString());

            String deleteMessage = deleteResponse.jsonPath().getString("message");
            if (!"User deleted!".equals(deleteMessage)) {
                System.err.println(String.format("Warning: Deletion of user %s did not succeed as expected. Response: %s", email, deleteMessage));
            }
        }
    }
}
