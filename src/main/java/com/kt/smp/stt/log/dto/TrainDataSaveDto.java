package com.kt.smp.stt.log.dto;

import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import lombok.Getter;
import javax.servlet.http.HttpServletRequest;

@Getter
public class TrainDataSaveDto {

    private String content;
    private Integer weight;
    private int serviceModelId;
    private String serviceCode;
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

    public String[] splitContent() {
        return content.split(System.lineSeparator());
    }
    
    public void setServiceModelId(String serviceCode) {
    	this.serviceModelId = Integer.parseInt(serviceCode);
    }
}
