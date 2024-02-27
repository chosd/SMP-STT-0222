package com.kt.smp.stt.confidence.service;

import com.kt.smp.common.util.JacksonUtil;
import com.kt.smp.common.util.crypto.TextCrypto;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.confidence.domain.SttConfidenceSearchCondition;
import com.kt.smp.stt.confidence.domain.SttConfidenceVO;
import com.kt.smp.stt.confidence.dto.ConfidenceDataDto;
import com.kt.smp.stt.confidence.dto.SttResultDto;
import com.kt.smp.stt.confidence.repository.SttConfidenceRepository;
import com.kt.smp.stt.log.dto.LogSaveDto;
import com.kt.smp.stt.log.service.LogService;
import com.kt.smp.stt.log.type.CallDirection;
import com.kt.smp.stt.log.type.CallStatus;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SttConfidenceService {

    private final LogService logService;
    private final ServiceModelService serviceModelService;
    private final SttConfidenceRepository sttConfidenceRepository;
    
    private final TextCrypto textCrypto;
    
    // 신뢰도 정보 저장
    public void record(ConfidenceDataDto newConfidence) {
       
        List<LogSaveDto> newLogList = new ArrayList<>();
        
        newLogList.add(convert(newConfidence, newConfidence.getSttResult()));
        logService.save(newLogList);
    }

    private LogSaveDto convert(ConfidenceDataDto confidence, SttResultDto sttResult) {
    	
    	String transcript = sttResult.getTranscript();
    	
    	String projectCode = TenantContextHolder.getProjectCode();
    	if (PreferenceValueHolder.textEncrypt.get(projectCode)) {
    		transcript = textCrypto.encrypt(transcript);
    	};
    	
        return LogSaveDto.builder()
                .callKey(confidence.getCallkey())
                .customerIdentifier(confidence.getPhoneType())
                .direction(getDirection(confidence))
                .startTime(sttResult.getStartTime())
                .endTime(sttResult.getEndTime())
                .wavFilePath(sttResult.getSentenceWavPath())
//                .serviceModelId(getServiceModelId(confidence))
                .serviceModelId(Integer.parseInt(confidence.getServiceCode()))
                .assistantId(getAssistantId(confidence))
                .status(CallStatus.COMPLETE)
                .failCause(null)
                .transcript(transcript)
                .transcriptStart(sttResult.getStartTime())
                .transcriptEnd(sttResult.getEndTime())
                .cer(sttResult.getConfidence())
                .sentenceWavPath(sttResult.getSentenceWavPath())
                .words(JacksonUtil.objectToJson(sttResult.getWords()))
                .build();
    }
    
    private CallDirection getDirection(ConfidenceDataDto confidence) {

        Integer txrx = confidence.getTxrx();
        if (txrx == 0) {
            return CallDirection.TX;
        }

        return CallDirection.RX;
    }

    private Integer getServiceModelId(ConfidenceDataDto confidence) {
    	ServiceModelVO serviceModel = serviceModelService.detailByServiceCode(confidence.getServiceCode());
    	
        return Long.valueOf(serviceModel.getId()).intValue();
    }

    private String getAssistantId(ConfidenceDataDto confidence) {

        String[] splits = confidence.getDeviceId().split("_");
        if (splits.length < 2) {
            return "";
        }

        return splits[1];
    }

    public List<SttConfidenceVO> confidenceChartData(SttConfidenceSearchCondition searchCondition) {
        return sttConfidenceRepository.confidenceChartData(searchCondition);
    }
}
