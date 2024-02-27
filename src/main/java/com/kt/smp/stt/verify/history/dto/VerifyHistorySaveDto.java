package com.kt.smp.stt.verify.history.dto;

import com.kt.smp.stt.verify.history.type.VerifyStatus;
import com.kt.smp.stt.verify.request.dto.VerifyRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyHistorySaveDto {

    private Integer datasetId;
    private String datasetName;
    private Integer deployId;
    private VerifyStatus status;
    private String description;
    private String regId;
    private String regIp;
    private String updId;
    private String updIp;
    
    private Integer serviceModelId;


    public static VerifyHistorySaveDto from(VerifyRequestDto verifyRequest) {

        VerifyHistorySaveDto history = new VerifyHistorySaveDto();
        history.setDatasetId(verifyRequest.getDatasetId());
        history.setDatasetName(verifyRequest.getDatasetName());
        history.setDeployId(verifyRequest.getDeployId());
        history.setDescription(verifyRequest.getDescription());
        history.setStatus(VerifyStatus.VERIFYING);
        history.setRegId(verifyRequest.getRegId());
        history.setRegIp(verifyRequest.getRegIp());
        history.setUpdId(verifyRequest.getRegId());
        history.setUpdIp(verifyRequest.getRegIp());
        history.setServiceModelId(verifyRequest.getServiceModelId());
        return history;
    }
}
