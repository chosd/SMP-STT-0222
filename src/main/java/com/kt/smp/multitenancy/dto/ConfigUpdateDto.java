package com.kt.smp.multitenancy.dto;

import lombok.Getter;

@Getter
public class ConfigUpdateDto {

    private Integer id;
    private String engineParameter;
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private String updId;
    private String updIp;

    public ConfigUpdateDto(ConfigSaveDto save) {

        this.engineParameter = save.getEngineParameter();
        this.dbUrl = save.getDbUrl();
        this.dbUsername = save.getDbUsername();
        this.dbPassword = save.getDbPassword();
        this.updId = save.getUpdId();
        this.updIp = save.getUpdIp();
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
