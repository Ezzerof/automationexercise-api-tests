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
 * <h1>InvalidLoginTest</h1>
 *
 * <p>This test class verifies the behavior of the POST method on the Verify Login API endpoint when invalid credentials are provided.</p>
 *
 * <p><strong>API Under Test:</strong></p>
 * <ul>
 *   <li><strong>Endpoint:</strong> <code>{Config.BASE_URL}{Config.LOGIN_ENDPOINT}</code></li>
 *   <li><strong>Method:</strong> POST</li>
 *   <li><strong>Request Parameters:</strong> email, password</li>
 *   <li><strong>Expected JSON Response:</strong>
 *       - Response Code: "404"
 *       - Response Message: "User not found!"</li>
 *   <li><strong>Expected HTTP Status Code:</strong> 200</li>
 * </ul>
 *
 * <p>The CSV file used (<code>{Config.INVALID_LOGIN_CSV_PATH}</code>) should have the following format:</p>
 * <pre>
 * Email,Password
 * invalid.email@gmail.com,123456789
 * katie@gmail.com,invalidPassword
 * </pre>
 *
 * <p>Each row in the CSV represents a test case for invalid login credentials. The tests ensure that the API returns the correct
 * error message and response code when invalid credentials are submitted.</p>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InvalidLoginTest {

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
     * Parameterized test for invalid login credentials.
     * This test sends a POST request to the Verify Login endpoint using each row from the CSV file and verifies that the API responds with the expected error message and response code.
     *
     * @param email the email provided in the CSV file
     * @param password the password provided in the CSV file
     */
    @Order(1)
    @ParameterizedTest(name = "{index} - Verify invalid login for email: {0}")
    @CsvFileSource(files = Config.INVALID_LOGIN_CSV_PATH, numLinesToSkip = 1)
    @DisplayName("Verify login with invalid credentials")
    void verifyLoginWithInvalidCredentials(String email, String password) {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams("email", email, "password", password)
                .post(Config.BASE_URL + Config.LOGIN_ENDPOINT)
                .then()
                .extract().response();
        System.out.println("Response Body (Invalid Credentials): " + response.getBody().asString());
        assertThat("Expected response message: 'User not found!'",
                response.jsonPath().getString("message"), equalTo(Config.USER_NOT_FOUND_MESSAGE));
        assertThat("Expected response code 404!",
                response.jsonPath().getString("responseCode"), equalTo(Config.EXPECTED_RESPONSE_CODE_404));
        assertThat("Expected HTTP status code 200!",
                response.getStatusCode(), equalTo(Config.EXPECTED_STATUS_CODE));
    }

    /**
     * Verify login without email parameter.
     * This test uses a valid password from the CSV and sends a request without the email parameter.
     * It expects a response indicating that a required parameter is missing.
     *
     * @param password the password provided in the CSV file
     */
    @Order(2)
    @ParameterizedTest(name = "{index} - Verify login missing email with password: {0}")
    @CsvFileSource(files = Config.INVALID_LOGIN_CSV_PATH, numLinesToSkip = 1)
    @DisplayName("Verify login without email parameter")
    void verifyLoginWithoutEmail(String password) {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("password", password)
                .post(Config.BASE_URL + Config.LOGIN_ENDPOINT)
                .then()
                .extract().response();
        System.out.println("Response Body (Missing Email): " + response.getBody().asString());
        assertThat("Expected response message for missing email",
                response.jsonPath().getString("message"), equalTo(Config.MISSING_EMAIL_OR_PASSWORD_MESSAGE));
        assertThat("Expected response code 400!",
                response.jsonPath().getString("responseCode"), equalTo(Config.EXPECTED_RESPONSE_CODE_400));
        assertThat("Expected HTTP status code 200!",
                response.getStatusCode(), equalTo(Config.EXPECTED_STATUS_CODE));
    }

    /**
     * Verify login without password parameter.
     * This test uses a valid email from the CSV and sends a request without the password parameter.
     * It expects a response indicating that a required parameter is missing.
     *
     * @param email the email provided in the CSV file
     */
    @Order(3)
    @ParameterizedTest(name = "{index} - Verify login missing email with password: {0}")
    @CsvFileSource(files = Config.INVALID_LOGIN_CSV_PATH, numLinesToSkip = 1)
    @DisplayName("Verify login without password parameter")
    void verifyLoginWithoutPassword(String email) {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("email", email)
                .post(Config.BASE_URL + Config.LOGIN_ENDPOINT)
                .then()
                .extract().response();
        System.out.println("Response Body (Missing Password): " + response.getBody().asString());
        assertThat("Expected response message for missing password",
                response.jsonPath().getString("message"), equalTo(Config.MISSING_EMAIL_OR_PASSWORD_MESSAGE));
        assertThat("Expected response code 400!",
                response.jsonPath().getString("responseCode"), equalTo(Config.EXPECTED_RESPONSE_CODE_400));
        assertThat("Expected HTTP status code 200!",
                response.getStatusCode(), equalTo(Config.EXPECTED_STATUS_CODE));
    }

    /**
     * Cleanup: Delete test users after all tests have run.
     * This method reads the creation CSV (Config.CREATE_USER_CSV_PATH) and sends a DELETE request for each user.
     * If deletion does not succeed as expected, a warning is logged.
     */
    @AfterAll
    void cleanUpTestUsers() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Config.CREATE_USER_CSV_PATH));
        // Skip header (assumed at index 0) and iterate through the remaining lines
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
            String deleteMessage = deleteResponse.jsonPath().getString("message");
            if (!"Account deleted!".equals(deleteMessage)) {
                System.err.println(String.format("Warning: Deletion of user %s did not succeed as expected. Response: %s", email, deleteMessage));
            }
        }
    }
}