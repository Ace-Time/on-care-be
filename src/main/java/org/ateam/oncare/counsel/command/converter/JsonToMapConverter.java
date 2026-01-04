package org.ateam.oncare.counsel.command.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.ateam.oncare.counsel.command.dto.JsonUtil;

import java.util.Map;

/**
 * JPA용 JSON Converter (Command 레이어)
 * JsonUtil 공통 유틸리티 사용
 */
@Converter(autoApply = false)
public class JsonToMapConverter implements AttributeConverter<Map<String, Object>, String> {
    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        return JsonUtil.mapToJson(attribute);
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        return JsonUtil.jsonToMap(dbData);
    }
}
