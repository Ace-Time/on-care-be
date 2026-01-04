package org.ateam.oncare.counsel.command.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Map을 JSON 문자열로 변환
     */
    public static String mapToJson(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error("Error converting Map to JSON", e);
            throw new IllegalArgumentException("Error converting Map to JSON", e);
        }
    }

    /**
     * JSON 문자열을 Map으로 변환
     */
    public static Map<String, Object> jsonToMap(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new HashMap<>();
        }

        try {
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to Map: {}", json, e);
            throw new IllegalArgumentException("Error converting JSON to Map", e);
        }
    }
}
