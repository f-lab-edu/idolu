package com.idolu.product.global.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ReadingConverter
@RequiredArgsConstructor
public class JsonToMapConverter implements Converter<String, Map<String, String>> {

    private final ObjectMapper objectMapper;

    @Override
    public Map<String, String> convert(@NotNull String jsonStr) {
        try {
            return objectMapper.readValue(jsonStr, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("파싱 과정에서 문제가 발생했습니다. json: {}", jsonStr, e);
        }

        return new HashMap<>();
    }
}
