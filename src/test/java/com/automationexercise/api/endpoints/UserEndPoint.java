package com.automationexercise.api.endpoints;

import com.automationexercise.api.payload.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserEndPoint {

    public static Response createUser(User payload) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when()
                .post(Routes.postUserAccount_url);

    }

    public static Response readUser(String emailAddress) {
        return given()
                .param("email", emailAddress)
                .when()
                .post(Routes.getUserAccountByEmail_url);

    }

    public static Response updateUser(String username, User payload) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .param("username", username)
                .body(payload)
                .when()
                .put(Routes.putUserAccount_url);

    }

    public static Response deleteUser(String email, String password) {
        return given()
                .param("email", email)
                .param("password", password)
                .when()
                .delete(Routes.deleteUserAccount_url);

    }
}
