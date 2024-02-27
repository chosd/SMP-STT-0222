package com.kt.smp.stt.verify.request.dto;

import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;

import lombok.Builder;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Builder
public class VerifyRequestDto {

    private Integer serviceModelId;
    private Integer deployId;
    private Integer datasetId;
    private String datasetName;
    private String description;
    private String regId;
    private String regIp;
    private String projectCode;

    public void audit(HttpServletRequest request) {

        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        regId = header.getUserId();
        regIp = request.getRemoteAddr();
        projectCode = header.getProjectCode();
    }
}
