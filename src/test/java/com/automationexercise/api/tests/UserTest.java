package com.automationexercise.api.tests;

import com.automationexercise.api.endpoints.Routes;
import com.automationexercise.api.endpoints.UserEndPoint;
import com.automationexercise.api.payload.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    Faker faker;
    User userPayload;

    @BeforeEach
    public void setupData() {
        faker = new Faker();
        userPayload = new User();
        userPayload.setName("MoonStar"); // username is set
        userPayload.setPassword("hello123");
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        userPayload.setAddress1(faker.address().streetAddressNumber());
        userPayload.setAddress2(faker.address().secondaryAddress());
        userPayload.setCompany(faker.company().bs());
        userPayload.setBirth_date("7");
        userPayload.setBirth_month("10");
        userPayload.setBirth_year("2000");
        userPayload.setEmail(faker.internet().safeEmailAddress());
        userPayload.setMobile_number(faker.phoneNumber().cellPhone());
        userPayload.setCountry("India");
        userPayload.setTitle("Mr.");
        userPayload.setCity("London");
        userPayload.setZipcode("46868");
        userPayload.setState("Manchester");
    }

    @Test
    @DisplayName("Test post User")
    void testPostUser() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userPayload);
        System.out.println(json);
        Response response = UserEndPoint.createUser(userPayload);

        response.then().log().all();
        //assertEquals(response.getStatusCode(), 201);

    }

    @Test
    public void testUpdateAccount() {

        String name = "John Smith";
        String email = "johnsmith@example.com";
        String password = "newpassword";
        String title = "Mr";
        String birthDate = "01";
        String birthMonth = "01";
        String birthYear = "1990";
        String firstName = "John";
        String lastName = "Smith";
        String company = "ABC Inc.";
        String address1 = "123 Main St";
        String address2 = "";
        String country = "US";
        String zipCode = "12345";
        String state = "CA";
        String city = "Los Angeles";
        String mobileNumber = "1234567890";

        Response response = given()
                .contentType(ContentType.ANY)
                .config(RestAssured.config()
                        .encoderConfig(
                                EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs("*/*", ContentType.TEXT)))
                .body("{"
                        + "\"name\": \"" + name + "\","
                        + "\"email\": \"" + email + "\","
                        + "\"password\": \"" + password + "\","
                        + "\"title\": \"" + title + "\","
                        + "\"birth_date\": \"" + birthDate + "\","
                        + "\"birth_month\": \"" + birthMonth + "\","
                        + "\"birth_year\": \"" + birthYear + "\","
                        + "\"firstname\": \"" + firstName + "\","
                        + "\"lastname\": \"" + lastName + "\","
                        + "\"company\": \"" + company + "\","
                        + "\"address1\": \"" + address1 + "\","
                        + "\"address2\": \"" + address2 + "\","
                        + "\"country\": \"" + country + "\","
                        + "\"zipcode\": \"" + zipCode + "\","
                        + "\"state\": \"" + state + "\","
                        + "\"city\": \"" + city + "\","
                        + "\"mobile_number\": \"" + mobileNumber + "\""
                        + "}")
                .when()
                .log().all()
                .post(Routes.postUserAccount_url);

        response.then()
                .assertThat()
                .statusCode(200)
                .body(equalTo("User updated!"));
    }

}
