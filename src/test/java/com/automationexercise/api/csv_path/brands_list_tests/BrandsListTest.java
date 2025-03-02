package com.automationexercise.api.csv_path.brands_list_tests;

import com.automationexercise.api.config.Config;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BrandsListTest {

    private static Response response;

    @BeforeAll
    static void setup() {
        response = given()
                .when()
                .get(Config.BASE_URL + Config.BRANDS_ENDPOINT)
                .then()
                .extract().response();
    }

    @TestTemplate
    @DisplayName("Testing brand list")
    @ParameterizedTest(name = "{index} - Brand id: {0}")
    @CsvFileSource(files = Config.BRANDS_CSV_PATH, numLinesToSkip = 1)
    public void testBrandsList(String id, String brand) {

        assertThat(response.jsonPath().getString("brands.find { it.id == " + id + "}.id"), equalTo(id));
        assertThat(response.jsonPath().getString("brands.find { it.id == " + id + "}.brand"), equalTo(brand));
    }

    @Test
    @DisplayName("Test Status Code: " + Config.EXPECTED_STATUS_CODE)
    void testStatusCode() {
        assertThat("Status code mismatch!", response.getStatusCode(), equalTo(Config.EXPECTED_STATUS_CODE));
    }

    @Test
    @DisplayName("Check size of the list should be: " + Config.EXPECTED_LIST_SIZE)
    void testListSize() {
        List<String> brandIds = response.jsonPath().getList("brands.id");
        assertThat("List size mismatch!", brandIds.size(), equalTo(Config.EXPECTED_LIST_SIZE));
    }

    @Test
    @DisplayName("Check the server name: " + Config.EXPECTED_SERVER_NAME)
    void testServerName() {
        assertThat("Server name mismatch!", response.header("server"), equalTo(Config.EXPECTED_SERVER_NAME));
    }

    @Test
    @DisplayName("Test the content-type")
    void testContentType() {
        assertThat("Content-Type mismatch!", response.header("content-type"), equalTo(Config.EXPECTED_CONTENT_TYPE));
    }
}
