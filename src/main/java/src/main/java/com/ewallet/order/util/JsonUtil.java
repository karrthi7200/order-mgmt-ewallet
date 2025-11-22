package src.main.java.com.ewallet.order.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T stringToJson(String jsonString, Class<T> tClass) {
        try {
            return mapper.readValue(jsonString, tClass);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JSON string", e);
        }
    }

}
