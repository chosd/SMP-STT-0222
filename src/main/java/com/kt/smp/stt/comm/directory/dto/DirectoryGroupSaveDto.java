package com.kt.smp.stt.comm.directory.dto;

import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

@Getter
public class DirectoryGroupSaveDto {

    private String name;
    private String description;
    private String gubun;
    private String path;
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
