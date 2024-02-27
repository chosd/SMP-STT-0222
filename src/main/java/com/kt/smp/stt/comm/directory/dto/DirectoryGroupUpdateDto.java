package com.kt.smp.stt.comm.directory.dto;

import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

@Getter
public class DirectoryGroupUpdateDto {

    private int id;
    private String name;
    private String description;
    private String updId;
    private String updIp;

    public void setId(int id) {
        this.id = id;
    }

    public void audit(HttpServletRequest request) {
        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        updId = header.getUserId();
        updIp = request.getRemoteAddr();
    }
}
