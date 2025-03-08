package com.automationexercise.api.config;

public class Config {

    // API Endpoints
    public static final String BASE_URL = "https://automationexercise.com";
    public static final String BRANDS_ENDPOINT = "/api/brandsList";
    public static final String PRODUCTS_ENDPOINT = "/api/productsList";
    public static final String SEARCH_PRODUCT_ENDPOINT = "/api/searchProduct";
    public static final String LOGIN_ENDPOINT = "/api/verifyLogin";
    public static final String CREATE_ACCOUNT_ENDPOINT = "/api/createAccount";
    public static final String DELETE_ACCOUNT_ENDPOINT = "/api/deleteAccount";
    public static final String UPDATE_ACCOUNT_ENDPOINT = "/api/updateAccount";
    public static final String GET_USER_DETAILS_BY_EMAIL_ENDPOINT = "/api/getUserDetailByEmail";
    public static final String USERS_ENDPOINT = "/api/users";

    // Expected Values
    public static final int EXPECTED_STATUS_CODE = 200;
    public static final String EXPECTED_METHOD_NOT_SUPPORTED_MESSAGE = "This request method is not supported.";
    public static final String EXPECTED_ACCOUNT_DELETED_MESSAGE = "Account deleted!";
    public static final String EXPECTED_USER_UPDATED_MESSAGE = "User updated!";
    public static final String USER_EXISTS_MESSAGE = "User exists!";
    public static final String USER_NOT_FOUND_MESSAGE = "User not found!";
    public static final String EXPECTED_SEARCHED_PRODUCT_IN_RETURNED_LIST_MESSAGE = "Expected to find the searched product in the returned list!";
    public static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found with this email, try another email!";
    public static final String BAD_REQUEST_PRODUCT_PARAMETER_IS_MISSING_MESSAGE = "Bad request, search_product parameter is missing in POST request.";
    public static final String BAD_REQUEST_EMAIL_PARAMETER_IS_MISSING_MESSAGE = "Bad request, email parameter is missing in GET request.";
    public static final String BAD_REQUEST_MISSING_PASSWORD_PARAMETER_MESSAGE = "Bad request, password parameter is missing in PUT request.";
    public static final String MISSING_EMAIL_OR_PASSWORD_MESSAGE = "Bad request, email or password parameter is missing in POST request.";

    public static final String EXPECTED_RESPONSE_CODE_404 = "404";
    public static final String EXPECTED_RESPONSE_CODE_405 = "405";
    public static final String EXPECTED_RESPONSE_CODE_400 = "400";
    public static final String EXPECTED_RESPONSE_CODE_200 = "200";
    public static final int EXPECTED_LIST_SIZE = 34;
    public static final String EXPECTED_SERVER_NAME = "cloudflare";
    public static final String EXPECTED_CONTENT_TYPE = "text/html; charset=utf-8";

    // CSV File Paths
    public static final String BRANDS_CSV_PATH = "src/test/resources/brands_data.csv";
    public static final String SEARCH_PRODUCT_CSV_PATH = "src/test/resources/products_search_data.csv";
    public static final String EXPECTED_PRODUCTS_DETAILS_CSV_PATH = "src/test/resources/expected_products_details.csv";
    public static final String CREATE_USER_CSV_PATH = "src/test/resources/create_user_data.csv";
    public static final String CREATE_UPDATE_USER_CSV_PATH = "src/test/resources/create_update_user_data.csv";
    public static final String DELETE_USER_CSV_PATH = "src/test/resources/delete_user_data.csv";
    public static final String UPDATE_USER_CSV_PATH = "src/test/resources/update_user_data.csv";
    public static final String EMAIL_DETAILS_CSV_PATH = "src/test/resources/find_user_data.csv";
    public static final String VALID_LOGIN_CSV_PATH = "src/test/resources/login_valid_data.csv";
    public static final String INVALID_LOGIN_CSV_PATH = "src/test/resources/login_invalid_data.csv";


    // Test Data
    public static final String INVALID_EMAIL = "thisEmailNotExists@gmail.uk";
}
