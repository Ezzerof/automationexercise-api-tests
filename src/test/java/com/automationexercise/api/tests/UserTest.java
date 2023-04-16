package com.automationexercise.api.tests;

import com.automationexercise.api.endpoints.UserEndPoint;
import com.automationexercise.api.payload.User;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    Faker faker;
    User userPayload;

    @BeforeEach
    public void setupData() {
        faker = new Faker();
        userPayload = new User();
        userPayload.setId(faker.idNumber().hashCode());
        userPayload.setName(faker.name().username());
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
    void testPostUser() {
        Response response = UserEndPoint.createUser(userPayload);

        response.then().log().all();

    }

}
