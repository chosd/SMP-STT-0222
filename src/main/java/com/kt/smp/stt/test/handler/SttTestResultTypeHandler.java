package com.kt.smp.stt.test.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kt.smp.stt.test.domain.SttTestResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * @author jaime
 * @title SttTestTypeHandler
 * @see\n <pre>
 * </pre>
 * @since 2022-05-05
 */
@Slf4j
public class SttTestResultTypeHandler extends BaseTypeHandler<List<SttTestResult>> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement
            , int i
            , List<SttTestResult> sttTestResults
            , JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, new Gson().toJson(sttTestResults));
    }

    @Override
    public List<SttTestResult> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return convertToList(resultSet.getString(s));
    }

    @Override
    public List<SttTestResult> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return convertToList(resultSet.getString(i));
    }

    @Override
    public List<SttTestResult> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return convertToList(callableStatement.getString(i));
    }

    private List<SttTestResult> convertToList(String s) {
        if (StringUtils.isEmpty(s)) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(s, new TypeReference<List<SttTestResult>>() {});
        } catch (IOException e) {
            log.error("[SttTestResultTypeHandler] failed to convert string to list");
        }

        return Collections.emptyList();
    }
}
