package com.automationexercise.api.json_path;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class JsonParser {

    static ObjectMapper objectMapper = new ObjectMapper();


    public static Map<String, String> createMap(String file) {
        Map<String, String> map = new HashMap<>();

        String json;
        try {
            json = readFileAsString(file);
            map = objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String readFileAsString(String file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

//    public static Map<Integer,Map<String, Object>> parseJSON(String jsonStr) {
//        JSONObject jsonObj = new JSONObject(jsonStr);
//        JSONArray productsArr = jsonObj.getJSONArray("products");
//        Map<Integer, Map<String, Object>> productMap = new HashMap<Integer, Map<String, Object>>();
//
//        for(int i=0; i<productsArr.length(); i++) {
//            JSONObject productObj = productsArr.getJSONObject(i);
//            int id = productObj.getInt("id");
//            String name = productObj.getString("name");
//            String price = productObj.getString("price");
//            String brand = productObj.getString("brand");
//            JSONObject categoryObj = productObj.getJSONObject("category");
//            JSONObject usertypeObj = categoryObj.getJSONObject("usertype");
//            String usertype = usertypeObj.getString("usertype");
//            String category = categoryObj.getString("category");
//
//            Map<String, Object> productDetails = new HashMap<String, Object>();
//            productDetails.put("name", name);
//            productDetails.put("price", price);
//            productDetails.put("brand", brand);
//            productDetails.put("usertype", usertype);
//            productDetails.put("category", category);
//
//            productMap.put(id, productDetails);
//        }
//        return productMap;
//    }

}
