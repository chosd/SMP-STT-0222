package com.kt.smp.stt.train.trainData.dto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SttTrainDataMultipartSaveDto {
	
	private Integer amDataId;
	private Integer serviceModelId;
	private String modelType;
	private String datasetName;
	private String description;
  private Integer dataSource;
	private MultipartFile answerFile;
  private MultipartFile voiceFile;
  private Integer trainVoiceCount;
  private String answerFileName;
  private String voiceFileName;
  private String amDataPath;
  private String basePath;
  private String detailPath;
  private String regId;
  private String regIp;
  private String updId;
  private String updIp;
  private String firstPathYn;

    public void audit(HttpServletRequest request) {

        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        regId = header.getUserId();
        regIp = request.getRemoteAddr();
        updId = header.getUserId();
        updIp = request.getRemoteAddr();
    }
}
