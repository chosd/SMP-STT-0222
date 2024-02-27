package com.kt.smp.stt.dictation.dto;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;

import lombok.Data;

@Data
public class DictationToVerifyDataDto {

	private List<Integer> dictationIdList;
	
	private Integer datasetId;
    private Integer serviceModelId;
    private String datasetName;
    private String basePath;
    private String detailPath;
    private String verifyDataPath;
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
