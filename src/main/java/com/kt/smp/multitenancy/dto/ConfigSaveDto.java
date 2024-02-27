package com.kt.smp.multitenancy.dto;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ConfigSaveDto {

    private String siteCode;
    private String projectCode;
    private String engineParameter;
    private String dbDriverClassName;
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private String regId;
    private String regIp;
    private String updId;
    private String updIp;

    public void audit(HttpServletRequest request) {

        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        regId = header.getUserId();
        regIp = request.getRemoteAddr();
        updId = header.getUserId();
        updIp = request.getRemoteAddr();
    }

    public void setDbDriverClassName(String dbDriverClassName) {
        this.dbDriverClassName = dbDriverClassName;
    }

    public void addDefaultOption() {

        StringBuffer sb = new StringBuffer(dbUrl);
    	
        if (!dbUrl.contains("allowMultiQueries=true")) {

            if (dbUrl.contains("?")) {
//                dbUrl += "&allowMultiQueries=true";

                sb.append("&allowMultiQueries=true");

                return;
            }

//            dbUrl += "?allowMultiQueries=true";
            sb.append("?allowMultiQueries=true");
        }
    }

}
