package com.automationexercise.api.tests2;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class JsonParser {

    static ObjectMapper objectMapper = new ObjectMapper();


    public static Map<String, String> createMap(String file) {
        Map<String, String> map =  new HashMap<>();

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

    public static void writeResponseIntoJson(String json, String pathWhereToSave) {
        try {
            FileWriter file = new FileWriter(pathWhereToSave);
            file.write(json);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
