package neu.homework.sunshine.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;

import java.util.LinkedHashMap;

public class JsonUtil {


    public static String toJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static <T> T toClass(String json,Class<T> c){
        ObjectMapper objectMapper = new ObjectMapper();
        T result = null;
        try {
            result = objectMapper.readValue(json,c);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static <T> T parseLinkedHashMapToClass(LinkedHashMap map,Class<T> c){
        String json = JsonUtil.toJson(map);
        return JsonUtil.toClass(json,c);
    }
}
