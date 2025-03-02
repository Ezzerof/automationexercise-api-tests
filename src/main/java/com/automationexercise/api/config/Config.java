package com.automationexercise.api.config;

public class Config {

    // API Endpoints
    public static final String BASE_URL = "https://automationexercise.com";
    public static final String BRANDS_ENDPOINT = "/api/brandsList";
    public static final String PRODUCTS_ENDPOINT = "/api/productsList";
    public static final String USERS_ENDPOINT = "/api/users";

    // Expected Values
    public static final int EXPECTED_STATUS_CODE = 200;
    public static final String EXPECTED_METHOD_NOT_SUPPORTED_MESSAGE = "This request method is not supported.";
    public static final String EXPECTED_RESPONSE_CODE_405 = "405";
    public static final int EXPECTED_LIST_SIZE = 34;
    public static final String EXPECTED_SERVER_NAME = "cloudflare";
    public static final String EXPECTED_CONTENT_TYPE = "text/html; charset=utf-8";

    // CSV File Paths
    public static final String BRANDS_CSV_PATH = "src/test/resources/AllBrands.csv";
}
