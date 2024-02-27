package com.kt.smp.stt.verify.dataset.dto;

import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class VerifyDatasetSaveDto {

    private int serviceModelId;
    private int directoryId;
    private String name;
    private String description;
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
}
