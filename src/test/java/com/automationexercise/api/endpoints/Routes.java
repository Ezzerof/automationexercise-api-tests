package com.automationexercise.api.endpoints;

public class Routes {

    public static String base_url = "https://automationexercise.com/api";

    // Product List module
    public static String getProducts_url = base_url + "/productsList";
    public static String postProducts_url = base_url + "/productsList";
    public static String postSearchProduct_url = base_url + "/searchProduct";

    // Brand List module
    public static String getBrands_url = base_url + "/brandsList";
    public static String putBrands_url = base_url + "/brandsList";

    // User module
    public static String postLoginDetails_url = base_url + "/verifyLogin";
    public static String postUserAccount_url = base_url + "/createAccount";
    public static String deleteUserAccount_url = base_url + "/deleteAccount";
    public static String putUserAccount_url = base_url + "/updateAccount";
    public static String getUserAccountByEmail_url = base_url + "/getUserDetailByEmail";

}
