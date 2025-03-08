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
 * <h1>GetUserAccountTest</h1>
 *
 * <p>This test class verifies the functionality of the GET <code>/getUserDetailByEmail</code> API endpoint
 * for retrieving user account details on AutomationExercise.</p>
 *
 * <p><strong>API Under Test:</strong></p>
 * <ul>
 *   <li><strong>Endpoint:</strong> <code>{Config.BASE_URL}{Config.GET_USER_DETAILS_BY_EMAIL_ENDPOINT}</code></li>
 *   <li><strong>Method:</strong> GET</li>
 *   <li><strong>Request Parameter:</strong> email</li>
 *   <li><strong>Expected HTTP Status Code:</strong> 200</li>
 *   <li><strong>Expected Response for Valid Email:</strong> JSON object with user details</li>
 *   <li><strong>Expected Response for Invalid Email:</strong> Response code 404 and message "Account not found"</li>
 *   <li><strong>Expected Response for Missing Email Parameter:</strong> Response code 400 and an appropriate error message</li>
 *   <li><strong>Security:</strong> SQL injection attempts should be rejected with a 404 and "Account not found" message</li>
 * </ul>
 *
 * <p>The test class includes the following steps:</p>
 * <ol>
 *   <li><strong>Setup:</strong> Create test users using data from a CSV file (<code>Config.CREATE_USER_CSV_PATH</code>)
 *       and retrieve valid and invalid user details.</li>
 *   <li><strong>Validation:</strong> Use parameterized tests (with CSV data from <code>Config.EMAIL_DETAILS_CSV_PATH</code>)
 *       to verify that the user details returned by the API match the expected data.</li>
 *   <li>Verify additional scenarios:
 *     <ul>
 *       <li>Invalid email address returns response code 404 and "Account not found" message.</li>
 *       <li>Missing email parameter returns response code 400 and an appropriate error message.</li>
 *       <li>SQL injection attempts are handled properly (response code 404 and "Account not found").</li>
 *       <li>Case-insensitive search is supported.</li>
 *     </ul>
 *   </li>
 * </ol>
 *
 * <p>This documentation is intended for developers and testers who need to understand the testing approach for
 * retrieving user account details by email. The HTML formatting can be viewed in generated Javadoc or via IDE hover.</p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetUserAccountTest {

    private Response responseValidEmail;
    private Response responseInvalidEmail;
    private String VALID_EMAIL;

    /**
     * <p><strong>Setup:</strong> Create test users from the CSV file (<code>Config.CREATE_USER_CSV_PATH</code>)
     * and retrieve user details for a valid and an invalid email.</p>
     *
     * <p>If user creation fails, the tests will fail early.</p>
     */
    @BeforeAll
    void createTestUsers() throws Exception {
        // Read the creation CSV file.
        List<String> lines = Files.readAllLines(Paths.get(Config.CREATE_USER_CSV_PATH));
        // Skip header (assumed at index 0) and iterate through each line.
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] fields = line.split(",");
            String name = fields[0].trim();
            String email = fields[1].trim();
            VALID_EMAIL = email; // store valid email for later reference
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
            System.out.println("Create response for " + email + ":\n" + createResponse.getBody().asString());

            // Fail early if user creation does not return the expected message.
            if (!("User created!".equals(createMessage) || "User already exists!".equals(createMessage))) {
                fail(String.format("User creation failed for %s. Received response: %s", email, createMessage));
            }
        }

        // Retrieve user details for a valid email.
        responseValidEmail = given()
                .param("email", VALID_EMAIL)
                .when()
                .get(Config.BASE_URL + Config.GET_USER_DETAILS_BY_EMAIL_ENDPOINT)
                .then()
                .statusCode(Config.EXPECTED_STATUS_CODE)
                .extract().response();

        // Retrieve details for an invalid email.
        responseInvalidEmail = given()
                .param("email", Config.INVALID_EMAIL)
                .when()
                .get(Config.BASE_URL + Config.GET_USER_DETAILS_BY_EMAIL_ENDPOINT)
                .then()
                .extract().response();
    }

    /**
     * Parameterized test that validates the user account details for a valid email
     * using data from a CSV file (<code>Config.EMAIL_DETAILS_CSV_PATH</code>).
     *
     * CSV Format:
     * <pre>
     * Name,Email,Title,Birth day,Birth month,Birth year,Firstname,Lastname,Company,Address 1,Address 2,Country,Zipcode,State,City
     * createTest,katie@gmail.com,Mrs,9,10,1996,Katie,Hulme,Sparta,in Uk 23,,Uk,19868,aa,London
     * </pre>
     */
    @TestTemplate
    @DisplayName("Validate user account details by email")
    @ParameterizedTest(name = "{index} - Validating account for user: {0}")
    @CsvFileSource(files = Config.EMAIL_DETAILS_CSV_PATH, numLinesToSkip = 1)
    void validateUserAccountDetails(String name, String email, String title, String birthDay,
                                    String birthMonth, String birthYear, String firstName, String lastName,
                                    String company, String address1, String address2, String country,
                                    String zipCode, String state, String city) {

        assertThat("Username mismatch!", responseValidEmail.jsonPath().getString("user.name"), equalTo(name));
        assertThat("Email mismatch!", responseValidEmail.jsonPath().getString("user.email"), equalTo(email));
        assertThat("Title mismatch!", responseValidEmail.jsonPath().getString("user.title"), equalTo(title));
        assertThat("Birth day mismatch!", responseValidEmail.jsonPath().getString("user.birth_day"), equalTo(birthDay));
        assertThat("Birth month mismatch!", responseValidEmail.jsonPath().getString("user.birth_month"), equalTo(birthMonth));
        assertThat("Birth year mismatch!", responseValidEmail.jsonPath().getString("user.birth_year"), equalTo(birthYear));
        assertThat("First name mismatch!", responseValidEmail.jsonPath().getString("user.first_name"), equalTo(firstName));
        assertThat("Last name mismatch!", responseValidEmail.jsonPath().getString("user.last_name"), equalTo(lastName));
        assertThat("Company mismatch!", responseValidEmail.jsonPath().getString("user.company"), equalTo(company == null ? "" : company));
        assertThat("Address1 mismatch!", responseValidEmail.jsonPath().getString("user.address1"), equalTo(address1));
        assertThat("Address2 mismatch!", responseValidEmail.jsonPath().getString("user.address2"), equalTo(address2 == null ? "" : address2));
        assertThat("Country mismatch!", responseValidEmail.jsonPath().getString("user.country"), equalTo(country));
        assertThat("Zip code mismatch!", responseValidEmail.jsonPath().getString("user.zipcode"), equalTo(zipCode));
        assertThat("State mismatch!", responseValidEmail.jsonPath().getString("user.state"), equalTo(state));
        assertThat("City mismatch!", responseValidEmail.jsonPath().getString("user.city"), equalTo(city));
    }

    /**
     * Validates that an invalid email returns a JSON response code of 404.
     */
    @Test
    @DisplayName("Validate response code for invalid email: 404")
    void validateResponseCodeForInvalidEmail() {
        assertThat("Expected response code 404 for invalid email!",
                responseInvalidEmail.jsonPath().getString("responseCode"), equalTo(Config.EXPECTED_RESPONSE_CODE_404));
    }

    /**
     * Validates that an invalid email returns the expected "Account not found" message.
     */
    @Test
    @DisplayName("Validate response message for invalid email")
    void validateResponseMessageForInvalidEmail() {
        assertThat("Expected 'Account not found' message!",
                responseInvalidEmail.jsonPath().getString("message"), equalTo(Config.ACCOUNT_NOT_FOUND_MESSAGE));
    }

    /**
     * Validates that omitting the email parameter results in a response code of 400.
     */
    @Test
    @DisplayName("Validate response code for missing email parameter: 400")
    void validateResponseCodeForMissingEmail() {
        Response missingEmailResponse = given()
                .when()
                .get(Config.BASE_URL + Config.GET_USER_DETAILS_BY_EMAIL_ENDPOINT)
                .then()
                .extract().response();

        assertThat("Expected response code 400 for missing email parameter!",
                missingEmailResponse.jsonPath().getString("responseCode"), equalTo(Config.EXPECTED_RESPONSE_CODE_400));
    }

    /**
     * Validates that omitting the email parameter returns the expected error message.
     */
    @Test
    @DisplayName("Validate response message for missing email parameter")
    void validateResponseMessageForMissingEmail() {
        Response missingEmailResponse = given()
                .when()
                .get(Config.BASE_URL + Config.GET_USER_DETAILS_BY_EMAIL_ENDPOINT)
                .then()
                .extract().response();

        assertThat("Expected 'Bad request' message!",
                missingEmailResponse.jsonPath().getString("message"), equalTo(Config.BAD_REQUEST_EMAIL_PARAMETER_IS_MISSING_MESSAGE));
    }

    /**
     * Validates that a SQL injection attempt (using "' OR '1'='1") results in a JSON response code of 404.
     */
    @Test
    @DisplayName("Validate security against SQL injection attempt")
    void validateSecurityAgainstSQLInjection() {
        Response sqlInjectionResponse = given()
                .param("email", "' OR '1'='1")
                .when()
                .get(Config.BASE_URL + Config.GET_USER_DETAILS_BY_EMAIL_ENDPOINT)
                .then()
                .extract().response();

        assertThat("Expected response code 404 for SQL injection attempt!",
                sqlInjectionResponse.jsonPath().getString("responseCode"), equalTo(Config.EXPECTED_RESPONSE_CODE_404));
    }

    /**
     * Validates that a SQL injection attempt returns the expected "Account not found" message.
     */
    @Test
    @DisplayName("Validate response message for SQL injection attempt")
    void validateResponseMessageForSQLInjection() {
        Response sqlInjectionResponse = given()
                .param("email", "' OR '1'='1")
                .when()
                .get(Config.BASE_URL + Config.GET_USER_DETAILS_BY_EMAIL_ENDPOINT)
                .then()
                .extract().response();

        assertThat("Expected 'Account not found' message for SQL injection attempt!",
                sqlInjectionResponse.jsonPath().getString("message"), equalTo(Config.ACCOUNT_NOT_FOUND_MESSAGE));
    }

    /**
     * Validates that a case-insensitive search for a valid email returns HTTP status code 200.
     */
    @Test
    @DisplayName("Validate case-insensitive email search")
    void validateCaseInsensitiveEmailSearch() {
        Response caseInsensitiveResponse = given()
                .param("email", VALID_EMAIL.toUpperCase())
                .when()
                .get(Config.BASE_URL + Config.GET_USER_DETAILS_BY_EMAIL_ENDPOINT)
                .then()
                .extract().response();

        assertThat("Expected status code 200 for case-insensitive search!",
                caseInsensitiveResponse.getStatusCode(), equalTo(Config.EXPECTED_STATUS_CODE));
    }

    /**
     * Cleanup: Delete test users after all tests have run.
     */
    @AfterAll
    void cleanUpTestUsers() throws Exception {
        // Read the CSV file.
        List<String> lines = Files.readAllLines(Paths.get(Config.CREATE_USER_CSV_PATH));
        // Skip header (index 0) and iterate through the rest of the lines
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] fields = line.split(",");
            // Fetching email and password
            String email = fields[1].trim();
            String password = fields[2].trim();
            Response deleteResponse = given()
                    .log().all()
                    .contentType("application/x-www-form-urlencoded")
                    .formParams("email", email, "password", password)
                    .delete(Config.BASE_URL + Config.DELETE_ACCOUNT_ENDPOINT)
                    .then()
                    .extract().response();

            System.out.println("Delete response for " + email + ": " + deleteResponse.getBody().asString());

            // Optionally, assert if deletion didn't return the expected response.
            String deleteMessage = deleteResponse.jsonPath().getString("message");
            if (!"User deleted!".equals(deleteMessage)) {
                System.err.println("Warning: Deletion of user " + email + " did not succeed as expected. Response: " + deleteMessage);
            }
        }

    }

}
