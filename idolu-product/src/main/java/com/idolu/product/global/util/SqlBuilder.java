package com.idolu.product.global.util;

import org.springframework.r2dbc.core.DatabaseClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlBuilder {

    private final StringBuilder sql;
    private final Map<String, Object> params = new HashMap<>();

    public SqlBuilder(String sql) {
        this.sql = new StringBuilder(sql);
    }

    public SqlBuilder append(String sqlPart) {
        sql.append(sqlPart).append("\n");
        return this;
    }

    public SqlBuilder append(String sqlPart, Object paramValue) {
        sql.append(sqlPart).append("\n");
        extractParams(sqlPart).forEach(paramName -> {
            if (paramValue != null) {
                params.put(paramName, paramValue);
            }
        });

        return this;
    }

    public SqlBuilder appendIfPresent(String sqlPart, Object paramValue) {
        if (paramValue != null) {
            this.append(sqlPart, paramValue);
        }

        return this;
    }

    private List<String> extractParams(String sqlPart) {
        List<String> paramNames = new ArrayList<>();
        Matcher matcher = Pattern.compile(":([\\w_]+)").matcher(sqlPart); // :로 시작하는 문자 추출
        while (matcher.find()) {
            paramNames.add(matcher.group(1));
        }

        return paramNames;
    }

    public DatabaseClient.GenericExecuteSpec execute(DatabaseClient databaseClient) {
        DatabaseClient.GenericExecuteSpec executeSpec = databaseClient.sql(sql.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            executeSpec = executeSpec.bind(entry.getKey(), entry.getValue());
        }

        return executeSpec;
    }
}
