package com.automationexercise.api.csv_path.brands_list_tests;

import com.automationexercise.api.config.Config;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * <h1>GetBrandsListTest</h1>
 *
 * <p>This test class verifies the integrity of the brands list returned by the GET
 * <code>/brandsList</code> API endpoint from AutomationExercise.</p>
 *
 * <p><strong>API Under Test:</strong></p>
 * <ul>
 *   <li><strong>Endpoint:</strong> <code>{Config.BASE_URL}{Config.BRANDS_ENDPOINT}</code></li>
 *   <li><strong>Method:</strong> GET</li>
 *   <li><strong>Expected HTTP Status Code:</strong> 200</li>
 *   <li><strong>Expected List Size:</strong> Defined in <code>Config.EXPECTED_LIST_SIZE</code></li>
 *   <li><strong>Expected Server:</strong> Defined in <code>Config.EXPECTED_SERVER_NAME</code></li>
 *   <li><strong>Expected Content-Type:</strong> Defined in <code>Config.EXPECTED_CONTENT_TYPE</code></li>
 * </ul>
 *
 * <p>The tests in this class perform the following verifications:</p>
 * <ol>
 *   <li>Validate that each brand entry in the response matches the corresponding
 *       data provided in the CSV file (<code>Config.BRANDS_CSV_PATH</code>).</li>
 *   <li>Check the overall HTTP status code returned by the endpoint.</li>
 *   <li>Verify that the total number of brands matches the expected size.</li>
 *   <li>Assert that the server header in the response matches the expected server name.</li>
 *   <li>Assert that the Content-Type header in the response is as expected.</li>
 *   <li>Ensure that none of the brand names in the response are empty.</li>
 *   <li>Ensure that all brand IDs in the response are unique.</li>
 * </ol>
 *
 * <p>This comprehensive test suite ensures that the brands list is returned correctly and
 * that its data integrity is maintained.</p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetBrandsListTest {

    private Response response;

    /**
     * Setup: Perform a GET request to the /brandsList endpoint once before all tests.
     * Store the response for reuse in subsequent tests.
     */
    @BeforeAll
    void setup() {
        response = given()
                .when()
                .get(Config.BASE_URL + Config.BRANDS_ENDPOINT)
                .then()
                .extract().response();

        System.out.println("BrandsList Response Body:\n" + response.getBody().asString());
    }

    /**
     * 1. Parameterized test that validates each brand ID and brand name from the CSV file.
     * This ensures the JSON response matches the expected data row by row.
     */
    @Order(1)
    @TestTemplate
    @DisplayName("Verify each brand in the CSV matches the JSON response")
    @ParameterizedTest(name = "{index} - Brand ID: {0}, Brand Name: {1}")
    @CsvFileSource(files = Config.BRANDS_CSV_PATH, numLinesToSkip = 1)
    void testBrandsList(String brandId, String expectedName) {
        // Check the brand ID
        String actualId = response.jsonPath()
                .getString("brands.find { it.id == " + brandId + "}.id");
        assertThat("Brand ID mismatch!", actualId, equalTo(brandId));

        // Check the brand name
        String actualName = response.jsonPath()
                .getString("brands.find { it.id == " + brandId + "}.brand");
        assertThat("Brand name mismatch for ID " + brandId, actualName, equalTo(expectedName));
    }

    /**
     * 2. Basic test: Validate the overall HTTP status code is the expected 200.
     */
    @Order(2)
    @Test
    @DisplayName("Validate HTTP status code")
    void testStatusCode() {
        assertThat("Status code mismatch!",
                response.getStatusCode(), equalTo(Config.EXPECTED_STATUS_CODE));
    }

    /**
     * 3. Basic test: Check that the total number of brands matches an expected size.
     * This value is set in Config.EXPECTED_LIST_SIZE. Adjust as needed.
     */
    @Order(3)
    @Test
    @DisplayName("Validate list size of the brands")
    void testListSize() {
        List<String> brandIds = response.jsonPath().getList("brands.id");
        assertThat("List size mismatch!",
                brandIds.size(), equalTo(Config.EXPECTED_LIST_SIZE));
    }

    /**
     * 4. Basic test: Verify the 'server' header matches the expected server name.
     * (e.g., 'cloudflare' or 'Apache')
     */
    @Order(4)
    @Test
    @DisplayName("Validate server name in response headers")
    void testServerName() {
        assertThat("Server name mismatch!",
                response.header("server"), equalTo(Config.EXPECTED_SERVER_NAME));
    }

    /**
     * 5. Basic test: Validate the 'content-type' header matches the expected value.
     * e.g., 'text/html; charset=utf-8' or 'application/json'
     */
    @Order(5)
    @Test
    @DisplayName("Validate content-type header")
    void testContentType() {
        assertThat("Content-Type mismatch!",
                response.header("content-type"), equalTo(Config.EXPECTED_CONTENT_TYPE));
    }

    /**
     * 6. Additional coverage: Ensure no brand name in the JSON response is empty.
     * This helps catch cases where brand names might be missing or blank.
     */
    @Order(6)
    @Test
    @DisplayName("Validate that all brand names are not empty")
    void testBrandNamesNotEmpty() {
        List<String> allBrandNames = response.jsonPath().getList("brands.brand");
        for (String brandName : allBrandNames) {
            assertFalse(brandName == null || brandName.trim().isEmpty(),
                    "Found an empty brand name in the response!");
        }
    }

    /**
     * 7. Additional coverage: Check that all brand IDs in the JSON are unique.
     * This ensures there are no accidental duplicates. If duplicates exist, the test fails.
     */
    @Order(7)
    @Test
    @DisplayName("Validate that all brand IDs are unique")
    void testUniqueBrandIds() {
        List<Integer> brandIds = response.jsonPath().getList("brands.id", Integer.class);
        Set<Integer> uniqueIds = new HashSet<>(brandIds);

        assertThat("Duplicate brand IDs found!",
                uniqueIds.size(), is(brandIds.size()));
    }
}
