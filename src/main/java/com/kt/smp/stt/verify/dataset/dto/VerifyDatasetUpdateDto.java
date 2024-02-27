package com.kt.smp.stt.verify.dataset.dto;

import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

@Getter
public class VerifyDatasetUpdateDto {

    private Integer id;

    private String name;
    private String description;
    private String updId;
    private String updIp;


    public void audit(HttpServletRequest request) {
        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        updId = header.getUserId();
        updIp = request.getRemoteAddr();
    }

    public void setId(int id) {
        this.id = id;
    }
}
