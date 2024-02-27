package com.kt.smp.stt.deploy.deploy.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployStatus;
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
 * @title SttDeployStatusTypeHandler
 * @see\n <pre>
 * </pre>
 * @since 2022-04-12
 */
@Slf4j
public class SttDeployStatusTypeHandler extends BaseTypeHandler<List<SttDeployStatus>> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement
            , int i
            , List<SttDeployStatus> sttDeployStatuses
            , JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, new Gson().toJson(sttDeployStatuses));
    }

    @Override
    public List<SttDeployStatus> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return convertToList(resultSet.getString(s));
    }

    @Override
    public List<SttDeployStatus> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return convertToList(resultSet.getString(i));
    }

    @Override
    public List<SttDeployStatus> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return convertToList(callableStatement.getString(i));
    }

    private List<SttDeployStatus> convertToList(String s) {
        if (StringUtils.isEmpty(s)) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(s, new TypeReference<List<SttDeployStatus>>() {});
        } catch (IOException e) {
            log.error("[SttDeployStatusTypeHandler] failed to convert string to list");
        }

        return Collections.emptyList();
    }
}
