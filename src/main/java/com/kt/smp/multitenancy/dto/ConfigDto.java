package com.kt.smp.multitenancy.dto;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigDto {

    private Integer id;
    private String siteCode;
    private String projectCode;
    private String engineParameter;
    private String dbDriverClassName;
    private String dbUrl;
    private String dbUsername;
    private String encDbPassword;
    private String dbPassword;
    private String isDefault;
    private String updDt;
    private String updId;

    public static ConfigDto empty() {
        return new ConfigDto();
    }

    public boolean isDefault() {
        return "Y".equals(isDefault);
    }

    public DataSource buildDatasource() {

        return DataSourceBuilder.create()
                .driverClassName(dbDriverClassName)
                .url(dbUrl)
                .username(dbUsername)
                .password(dbPassword)
                .build();
    }
}
