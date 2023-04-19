package com.automationexercise.api.csvPath.userTests;

import com.automationexercise.api.endpoints.Routes;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserAccountByEmailTest {

    public final static int expectedStatusCode = 200; // Entered by tester
    public final static String serverName = "cloudflare"; // Entered by tester
    static String emailAddress = "ezzerof9999@gmail.com"; // Entered by tester
    static String wrongEmailAddress = "thisEmailNotExists@gmail.uk"; // Entered by tester
    final static String contentType = "Content-Type=text/html; charset=utf-8";
    final static String queryParam = "email";
    static Response response;
    static Response response1;
    static int actualStatusCode = 0;

    @BeforeAll
    static void init() {
        response = given()
                .param(queryParam, emailAddress)
                .when()
                .get(Routes.getUserAccountByEmail_url)
                .then()
                .statusCode(expectedStatusCode)
                .extract().response();
    }

    @TestTemplate
    @DisplayName("Testing Account details")
    @ParameterizedTest(name = "{index} - Account id: {0}")
    @CsvFileSource(files = "src\\test\\resources\\EmailDetails.csv", numLinesToSkip = 1)
    public void testAccountDetails(String id, String name, String email, String title, String birth_day, String birth_month, String birth_year, String first_name, String last_name, String company, String address1, String address2, String country, String state, String city, String zipcode) {
        //try {
        assertThat(response.jsonPath().getString("user.id"), equalTo(id));
        assertThat(response.jsonPath().getString("user.name"), equalTo(name));
        assertThat(response.jsonPath().getString("user.email"), equalTo(email));
        assertThat(response.jsonPath().getString("user.title"), equalTo(title));
        assertThat(response.jsonPath().getString("user.birth_day"), equalTo(birth_day));
        assertThat(response.jsonPath().getString("user.birth_month"), equalTo(birth_month));
        assertThat(response.jsonPath().getString("user.birth_year"), equalTo(birth_year));
        assertThat(response.jsonPath().getString("user.first_name"), equalTo(first_name));
        assertThat(response.jsonPath().getString("user.last_name"), equalTo(last_name));
        assertThat(response.jsonPath().getString("user.company"), equalTo(company == null ? "" : company));
        assertThat(response.jsonPath().getString("user.address1"), equalTo(address1));
        assertThat(response.jsonPath().getString("user.address2"), equalTo(address2 == null ? "" : company));
        assertThat(response.jsonPath().getString("user.country"), equalTo(country));
        assertThat(response.jsonPath().getString("user.state"), equalTo(state));
        assertThat(response.jsonPath().getString("user.city"), equalTo(city));
        assertThat(response.jsonPath().getString("user.zipcode"), equalTo(zipcode));

//        } catch (AssertionError e) {
//            System.out.println("AssertionError: " + e.getMessage());
//        }
    }

    @Test
    @DisplayName("Test Status Code: " + expectedStatusCode)
    void testStatusCode() {
        actualStatusCode = response.getStatusCode();
        assertThat(actualStatusCode, equalTo(expectedStatusCode));
    }

    @Test
    @DisplayName("Check the server name: " + serverName)
    void checkTheServerName() {
        assertThat(response.getHeaders().get("server").toString(), equalTo("Server=" + serverName));
    }

    @Test
    @DisplayName("Test the content-type")
    void testTheContentType() {
        assertThat(response.getHeaders().get("content-type").toString(), equalTo(contentType));
    }

    @BeforeAll
    void setUp() {
        response1 = given()
                .queryParam(queryParam, wrongEmailAddress)
                .when()
                .get(Routes.getUserAccountByEmail_url)
                .then()
                .statusCode(expectedStatusCode)
                .extract().response();
    }

    @Test
    @DisplayName("Test response code for an invalid email 404")
    void testResponseCodeForAnInvalidEmail404() {
        assertThat(response1.jsonPath().getString("responseCode"), equalTo("404"));
    }

    @Test
    @DisplayName("Test response for an invalid email")
    void testResponseForAnInvalidEmail() {
        assertThat(response1.jsonPath().getString("message"), equalTo("Account not found with this email, try another email!"));
    }
}
