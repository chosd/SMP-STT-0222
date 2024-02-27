package com.kt.smp.stt.recognition.service;

import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.confidence.dto.SttRecognitionSearchResponseDto;
import com.kt.smp.stt.recognition.dto.SttRecognitionDetailVO;
import com.kt.smp.stt.recognition.dto.SttRecognitionSearchCondition;
import com.kt.smp.stt.recognition.dto.SttRecognitionVO;
import com.kt.smp.stt.recognition.enums.RecognitionError;
import com.kt.smp.stt.recognition.exception.RecognitionException;
import com.kt.smp.stt.recognition.repository.SttRecognitionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SttRecognitionServiceImpl implements SttRecognitionService{

    private final ServiceModelService serviceModelService;
    private final SttRecognitionRepository sttRecognitionRepository;

    @Override
    public SttRecognitionSearchResponseDto recognitionChartData(SttRecognitionSearchCondition searchCondition) {
        List<String> serviceCodeList = searchCondition.getServiceCodeList();
        if(serviceCodeList == null) {
        	throw new RecognitionException(RecognitionError.SERVICE_CODE_NOT_SELECTED);
        }
        
        List<SttRecognitionVO> sttRecognitionList = new ArrayList<>();
        
        for (String serviceCode : serviceCodeList) {
            ServiceModelVO serviceModel = serviceModelService.detailByServiceCode(serviceCode);
            
            SttRecognitionSearchCondition searchParam = SttRecognitionSearchCondition.builder()
                    .serviceCode(serviceCode)
                    .from(searchCondition.getFrom())
                    .to(searchCondition.getTo())
                    .build();
            
            List<SttRecognitionDetailVO> list = sttRecognitionRepository.getRecognitionDetail(searchParam);
            log.info(">>>> getRecognitionDetail list size : "+list.size()+" | "+list.toString());
            SttRecognitionVO sttRecognitionVO = buildSttRecognitionVO(serviceModel, list, list.size());
            log.info(">>>> selected serviceModel input chart data : {}" , sttRecognitionVO);
            sttRecognitionList.add(sttRecognitionVO);
        }
        
        return new SttRecognitionSearchResponseDto(sttRecognitionList, null, SttRecognitionChartDataBuilder.build(sttRecognitionList, searchCondition));
    }
    
    private SttRecognitionVO buildSttRecognitionVO(ServiceModelVO serviceModel, List<SttRecognitionDetailVO> list, Integer versionCount) {
        return SttRecognitionVO.builder()
                .serviceCode(serviceModel.getServiceCode())
                .serviceModelId(serviceModel.getId())
                .serviceModelName(serviceModel.getServiceModelName())
                .totalCount(versionCount)
                .sttRecognitionDetailList(list)
                .build();
    }

}
