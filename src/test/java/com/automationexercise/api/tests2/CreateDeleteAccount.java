package com.automationexercise.api.tests2;

import com.automationexercise.api.endpoints.Routes;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class CreateDeleteAccount {

    static Response response;
    static Map<String, String> items;
    static Map<String, String> updatedItems;

    @Order(1)
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CreateAccount {
        @Order(1)
        @Test
        void createAccount() {
            items = JsonParser.createMap("src\\test\\resources\\user.json");
            response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams(items)
                .post(Routes.postUserAccount_url);
            System.out.println(response.getBody().asString());
        }

        @Order(2)
        @Test
        @DisplayName("Test status code 200")
        void test() {
            assertThat(response.getStatusCode(), equalTo(200));

        }

        @Order(3)
        @Test
        @DisplayName("Test Create Account response message")
        void testCreateAccountResponseMessage() {
            assertThat(response.jsonPath().getString("message"), equalTo("User created!"));
        }

        @Order(4)
        @Test
        @DisplayName("Test Create Account response code should be 201")
        void testCreateAccountResponseCodeShouldBe201() {
            assertThat(response.jsonPath().getString("responseCode"), equalTo("201"));
        }

        @Order(5)
        @Test
        @DisplayName("Update account details")
        void updateAccountDetails() {
            updatedItems = JsonParser.createMap("src\\test\\resources\\user.json");
            response = given()
                    .contentType("application/x-www-form-urlencoded")
                    .formParams(updatedItems)
                    .put(Routes.putUserAccount_url);
            System.out.println(response.getBody().asString());
        }

        @Order(6)
        @Test
        @DisplayName("Test Update account response message")
        void testUpdatedAccountResponseMessage() {
            assertThat(response.jsonPath().getString("message"), equalTo("User updated!"));
        }

        @Order(7)
        @Test
        @DisplayName("Test Update account response code should be 200")
        void testUpdateAccountResponseCodeShouldBe200() {
            assertThat(response.jsonPath().getString("responseCode"), equalTo("200"));
        }

        @Order(8)
        @Test
        @DisplayName("Login with Valid Details")
        void testLoginWithValidDetails() {
            response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams("email", items.get("email"), "password", items.get("password"))
                .post(Routes.postLoginDetails_url);
            System.out.println(response.getBody().asString());
        }

        @Order(9)
        @Test
        @DisplayName("Test Login with Valid details response message")
        void testLoginWithValidDetailsResponseMessage() {
            assertThat(response.jsonPath().getString("message"), equalTo("User exists!"));
        }

        @Order(10)
        @Test
        @DisplayName("Test Login with Valid details response code should be 200")
        void testLoginWithValidDetailsResponseCodeShouldBe200() {
            assertThat(response.jsonPath().getString("responseCode"), equalTo("200"));
        }

        @Order(11)
        @Test
        @DisplayName("Login with Invalid Details")
        void loginWithInvalidDetails() {
            response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams("email", items.get("email") + "io", "password",
                    items.get("password") + "000")
                .post(Routes.postLoginDetails_url);
            System.out.println(response.getBody().asString());
        }

        @Order(12)
        @Test
        @DisplayName("Test Login with Invalid details response message")
        void testLoginWithInvalidDetailsResponseMessage() {
            assertThat(response.jsonPath().getString("message"), equalTo("User not found!"));
        }

        @Order(13)
        @Test
        @DisplayName("Test Login with Invalid details response code should be 404")
        void testLoginWithInvalidDetailsResponseCodeShouldBe404() {
            assertThat(response.jsonPath().getString("responseCode"), equalTo("404"));
        }

        @Order(14)
        @Test
        @DisplayName("Login without Email")
        void loginWithoutEmail() {
            response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams("password", items.get("password"))
                .post(Routes.postLoginDetails_url);
            System.out.println(response.getBody().asString());
        }

        @Order(15)
        @Test
        @DisplayName("Test Login without email response message")
        void testLoginWithoutEmailResponseMessage() {
            assertThat(response.jsonPath().getString("message"),
                equalTo("Bad request, email or password parameter is missing in POST request."));
        }

        @Order(16)
        @Test
        @DisplayName("Test Login without email response code should be 400")
        void testLoginWithoutEmailResponseCodeShouldBe400() {
            assertThat(response.jsonPath().getString("responseCode"), equalTo("400"));
        }

        @Order(17)
        @Test
        @DisplayName("Get user account detail by email")
        void testGetUserDetailsByEmail() {
            response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams("email", items.get("email"))
                .get(Routes.getUserAccountByEmail_url);

        }
    }

    @Order(2)
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CheckUserDetails {

        @Order(1)
        @Test
        @DisplayName("Test User name")
        void testUserName() {
            assertThat(response.jsonPath().getString("user.name"), equalTo(items.get("name")));
        }

        @Order(2)
        @Test
        @DisplayName("Test User email")
        void testUserEmail() {
            assertThat(response.jsonPath().getString("user.email"), equalTo(items.get("email")));
        }

        @Order(3)
        @Test
        @DisplayName("Test User Title")
        void testUserTitle() {
            assertThat(response.jsonPath().getString("user.title"), equalTo(items.get("title")));
        }

        @Order(4)
        @Test
        @DisplayName("Test User birth day")
        void testUserBirthDay() {
            assertThat(response.jsonPath().getString("user.birth_day"), equalTo(items.get("birth_date")));
        }

        @Order(4)
        @Test
        @DisplayName("Test User birth month")
        void testUserBirthMonth() {
            assertThat(response.jsonPath().getString("user.birth_month"), equalTo(items.get("birth_month")));
        }
        @Order(5)
        @Test
        @DisplayName("Test User birth year")
        void testUserBirthYear() {
            assertThat(response.jsonPath().getString("user.birth_year"), equalTo(items.get("birth_year")));
        }
        @Order(6)
        @Test
        @DisplayName("Test User first name")
        void testUserFirstName() {
            assertThat(response.jsonPath().getString("user.first_name"), equalTo(items.get("firstname")));
        }

        @Order(7)
        @Test
        @DisplayName("Test User last name")
        void testUserLastName() {
            assertThat(response.jsonPath().getString("user.last_name"), equalTo(items.get("lastname")));
        }
        @Order(8)
        @Test
        @DisplayName("Test User company")
        void testUserCompany() {
            assertThat(response.jsonPath().getString("user.company"), equalTo(items.get("company")));
        }

        @Order(9)
        @Test
        @DisplayName("Test User address1")
        void testUserAddress1() {
            assertThat(response.jsonPath().getString("user.address1"), equalTo(items.get("address1")));
        }

        @Order(10)
        @Test
        @DisplayName("Test User address2")
        void testUserAddress2() {
            assertThat(response.jsonPath().getString("user.address2"), equalTo(items.get("address2")));
        }

        @Order(11)
        @Test
        @DisplayName("Test User country")
        void testUserCountry() {
            assertThat(response.jsonPath().getString("user.country"), equalTo(items.get("country")));
        }

        @Order(12)
        @Test
        @DisplayName("Test User state")
        void testUserState() {
            assertThat(response.jsonPath().getString("user.state"), equalTo(items.get("state")));
        }

        @Order(13)
        @Test
        @DisplayName("Test User city")
        void testUserCity() {
            assertThat(response.jsonPath().getString("user.city"), equalTo(items.get("city")));
        }

        @Order(14)
        @Test
        @DisplayName("Test User zipcode")
        void testUserZipcode() {
            assertThat(response.jsonPath().getString("user.zipcode"), equalTo(items.get("zipcode")));
        }

    }

    @Order(3)
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DeleteUserClass {
        @Order(1)
        @Test
        @DisplayName("Delete user")
        void deleteUser() {
            response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams("email", items.get("email"), "password", items.get("password"))
                .delete(Routes.deleteUserAccount_url);
            System.out.println(response.getBody().asString());
        }

        @Order(2)
        @Test
        @DisplayName("Test Delete Account response message")
        void testDeleteAccountResponseMessage() {
            assertThat(response.jsonPath().getString("message"), equalTo("Account deleted!"));
        }

        @Order(3)
        @Test
        @DisplayName("Test Delete Account response code should be 200")
        void testDeleteAccountResponseCodeShouldBe200() {
            assertThat(response.jsonPath().getString("responseCode"), equalTo("200"));
        }
    }

}
