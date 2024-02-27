package com.kt.smp.stt.verify.data.dto;

import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class VerifyDataMultipartSaveDto {

    private Integer datasetId;
    private Integer serviceModelId;
    private String datasetName;
    private String basePath;
    private String detailPath;
    private String verifyDataPath;
    private MultipartFile answerSheet;
    private MultipartFile wavFile;
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
