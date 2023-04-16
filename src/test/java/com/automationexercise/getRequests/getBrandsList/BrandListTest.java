package com.automationexercise.getRequests.getBrandsList;

import com.automationexercise.api.endpoints.Routes;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BrandListTest {

    public final static int expectedStatusCode = 200; // Entered by tester
    public final static int sizeOfTheList = 34; // Entered by tester
    public final static String serverName = "cloudflare"; // Entered by tester
    final static String contentType = "Content-Type=text/html; charset=utf-8";
    static Response response;
    static int actualStatusCode = 0;

    @BeforeAll
    static void init() {
        response = given()
                .when()
                .get(Routes.getBrands_url)
                .then()
                .extract().response();
    }

    @TestTemplate
    @DisplayName("Testing brand list")
    @ParameterizedTest(name = "{index} - Brand id: {0}")
    @CsvFileSource(files = "src\\test\\resources\\AllBrands.csv", numLinesToSkip = 1)
    public void testBrandList(String id, String brand) {

        //try {
        assertThat(response.jsonPath().getString("brands.find { it.id == " + id + "}.id"), equalTo(id));
        assertThat(response.jsonPath().getString("brands.find { it.id == " + id + "}.brand"), equalTo(brand));
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
    @DisplayName("Check size of the list should be: " + sizeOfTheList)
    void checkSizeOfTheList() {
        List<String> productNames = response.jsonPath().getList("brands.id");
        assertThat(productNames.size(), equalTo(sizeOfTheList));
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
}
