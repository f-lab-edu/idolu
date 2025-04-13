package com.idolu.product.global.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.Map;

@WritingConverter
@AllArgsConstructor
public class MapToJsonConverter implements Converter<Map<String, String>, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convert(Map<String, String> source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
