package com.kt.smp.stt.dictation.dto;

import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.stt.log.type.CallDirection;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

@Getter
public class DictationSaveDto {

	private Integer sttLogId;
    private String transcript;
    private String dictatedText;
    private String wavFilePath;
    private Long serviceModelId;
    private String serviceCode;
    private CallDirection direction;
    private String regId;
    private String regIp;
    private String updId;
    private String updIp;

    public DictationSaveDto(Integer sttLogId, String transcript, String wavFilePath, Long serviceModelId, CallDirection direction) {
        this.sttLogId = sttLogId;
    	this.transcript = transcript;
        this.wavFilePath = wavFilePath;
        this.serviceModelId = serviceModelId;
        this.direction = direction;
    }

    public void audit(HttpServletRequest request) {

        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        regId = header.getUserId();
        regIp = request.getRemoteAddr();
        updId = header.getUserId();
        updIp = request.getRemoteAddr();
    }
    
    public void setTranscript(String transcript) {
    	this.transcript = transcript;
    }
    
    public void setDictatedText(String dictatedText) {
    	this.dictatedText = dictatedText;
    }

}
