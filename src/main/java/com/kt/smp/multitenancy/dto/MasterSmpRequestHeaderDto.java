package com.kt.smp.multitenancy.dto;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.kt.smp.config.CommonConstants;

import lombok.Getter;

@Getter
public class MasterSmpRequestHeaderDto {

    private String siteCode;
    private String projectCode;
    private String userId;
    private String userName;

    public MasterSmpRequestHeaderDto(String siteCode, String projectCode, String userId, String userName) {
        this.siteCode = siteCode;
        this.projectCode = projectCode;
        this.userId = userId;
        this.userName = userName;
    }

    public static MasterSmpRequestHeaderDto generate(HttpServletRequest request) {

        String siteCode = request.getHeader("x-smp-site-code");
        String projectCode = request.getHeader(CommonConstants.X_SMP_PROJECT_CODE);
        String userId = request.getHeader("x-smp-user-id");
        String userName = request.getHeader("x-smp-user-name");

        return new MasterSmpRequestHeaderDto(
                StringUtils.isBlank(siteCode) ? "site" : siteCode,
                StringUtils.isBlank(projectCode) ? "project" : projectCode,
                StringUtils.isBlank(userId) ? "userId" : userId,
                StringUtils.isBlank(userName) ? "userName" : userName);
    }
}
