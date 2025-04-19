package com.idolu.product.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableR2dbcAuditing
@RequiredArgsConstructor
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    private final ObjectMapper objectMapper;


    @Override
    public ConnectionFactory connectionFactory() {
        return null; // properties 파일 통해 설정
    }

    @Override
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new MapToJsonConverter(objectMapper));
        converters.add(new JsonToMapConverter(objectMapper));

        return new R2dbcCustomConversions(getStoreConversions(), converters);
    }
}
