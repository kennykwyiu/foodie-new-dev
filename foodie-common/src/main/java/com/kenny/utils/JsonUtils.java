package com.kenny.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 *
 * @Title: JsonUtils.java
 *
 */
public class JsonUtils {

    // Define the Jackson object
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Convert an object to a JSON string.
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
        try {
            String jsonString = MAPPER.writeValueAsString(data);
            return jsonString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert a JSON result set to an object
     *
     * @param jsonData JSON data
     * @param beanType Object type in the object
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T object = MAPPER.readValue(jsonData, beanType);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert JSON data to a list of pojo objects
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> list = MAPPER.readValue(jsonData, javaType);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}