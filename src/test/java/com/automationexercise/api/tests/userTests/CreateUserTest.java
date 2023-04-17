package com.automationexercise.api.tests.userTests;

import com.automationexercise.api.endpoints.Routes;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateUserTest {
    public static Response response;

    @Order(1)
    @TestTemplate
    @DisplayName("Create user account")
    @ParameterizedTest(name = "{index} - Name: {0}")
    @CsvFileSource(files = "src\\test\\resources\\CreateUser.csv", numLinesToSkip = 1)
    public void init(String name, String email, String password, String title, String birthDate, String birthMonth, String birthYear, String firstName, String lastName, String company, String address1, String address2, String country, String zipCode, String state, String city, String mobileNumber) {

        //try {
        response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams("name", name, "email", email, "password", password, "title", title, "birth_date", birthDate, "birth_month", birthMonth, "birth_year", birthYear, "firstname", firstName, "lastname", lastName, "company", company, "address1", address1, "address2", address2, "country", country, "zipcode", zipCode, "state", state, "city", city, "mobile_number", mobileNumber)
                .post(Routes.postUserAccount_url)
                .then()
                .extract().response();

//        } catch (AssertionError e) {
//            System.out.println("AssertionError: " + e.getMessage());
//        }
    }

    @Order(2)
    @Test
    @DisplayName("Test response message should be User created!")
    void testResponseMessage() {
        assertThat(response.jsonPath().getString("message"), equalTo("User created!"));
    }

    @Order(3)
    @Test
    @DisplayName("Test response code should be 201")
    void testResponseCodeShouldBe201() {
        assertThat(response.jsonPath().getString("responseCode"), equalTo("201"));
    }

    @Order(4)
    @Test
    @DisplayName("Test status code 200")
    void testStatusCode200() {
        assertThat(response.getStatusCode(), equalTo(200));
    }


}
