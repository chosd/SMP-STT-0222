package com.kt.smp.fileutil.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kt.smp.common.util.crypto.TextCrypto;
import com.kt.smp.fileutil.constant.ExcelConstants;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.train.trainData.domain.SttTrainAmDataVO;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataAmSearchCondition;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataSearchCondition;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataVO;
import com.kt.smp.stt.train.trainData.repository.SttTrainDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author jieun.chang
 * @title SttTrainDataExcelDownloadService
 * @see\n <pre>
 * </pre>
 * @since 2022-12-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TrainDataExcelDownloadService extends FastExcelService {

    private final SttTrainDataRepository sttTrainDataRepository;
    private final ServiceModelService serviceModelService;
    private final TextCrypto textCrypto;
    
    
    @Override
    public int count(Object searchCondition) {

        return (int) sttTrainDataRepository.count(castSearchCondition(searchCondition));
    }
    
    @Override
    public int amCount(Object searchCondition) {

        return (int) sttTrainDataRepository.amCount(castAmSearchCondition(searchCondition));
    }

    @Override
    public String getSheetName() {
        return "LM 학습데이터 검색 결과";
    }
    
    @Override
    public String getAmSheetName() {
        return "AM 학습데이터 검색 결과";
    }

    @Override
    public String getFileName(Long id) {
        return "LM 학습데이터 목록";
    }
    @Override
    public String getAmFileName(Long id) {
        return "AM 학습데이터 목록";
    }

    public List<String> getHeaders() {
        return Arrays.asList("No", "데이터 구분(*)", "서비스 모델(*)", "학습데이터(*)", "가중치(*)", "설명", "등록일", "수정일");
    }

    public List<String> getAmHeaders() {
        return Arrays.asList("No", "모델타입(*)", "서비스 모델(*)", "정답지 데이터셋(*)", "학습음성갯수", "설명", "등록일", "수정일");
    }
    
    @Override
    public void makePage(Worksheet ws, int pageStartRowIdx, int sheetIndex, Object searchCondition) throws IOException {
    	SttTrainDataSearchCondition bpSearchCondition = castSearchCondition(searchCondition);

        List<ServiceModelVO> serviceModelList = serviceModelService.listAll();

        PageHelper.startPage(
                pageStartRowIdx / getPageSize() + (sheetIndex * ExcelConstants.MAX_ROWS_PER_SHEET / getPageSize()) + 1,
                getPageSize(), "ID DESC");

        Page<SttTrainDataVO> page = sttTrainDataRepository.listPage(bpSearchCondition);
        List<SttTrainDataVO> results = page.getResult();

        int row = pageStartRowIdx + 1;
        
        Map<String, String> serviceNameMap = new HashMap<>();
        
        for (ServiceModelVO serviceModel : serviceModelList) {
        	serviceNameMap.put(serviceModel.getServiceCode(), serviceModel.getServiceModelName());
        }

        for (SttTrainDataVO sttTrainDataVO : results) {
        	//log.info(">>>> receive sttTrainDataVO getServiceModelId : "+sttTrainDataVO.getServiceModelId());
            ws.value(row, 0, row);
            ws.value(row, 1, getDefaultStringIfEmpty(sttTrainDataVO.getDataType()));
            ws.value(row, 2, getDefaultStringIfEmpty(serviceNameMap.get(sttTrainDataVO.getServiceModelId().toString())));
            ws.value(row, 3, getDefaultStringIfEmpty(textCrypto.decrypt(sttTrainDataVO.getContents())));
            ws.value(row, 4, getDefaultStringIfEmpty(sttTrainDataVO.getRepeatCount()));
            ws.value(row, 5, getDefaultStringIfEmpty(sttTrainDataVO.getDescription()));
            ws.value(row, 6, getDefaultStringIfEmpty(sttTrainDataVO.getRegDt()));
            ws.value(row, 7, getDefaultStringIfEmpty(sttTrainDataVO.getUpdDt()));

            ws.range(row, 0, row, getHeaders().size() - 1).style().horizontalAlignment("center").set();

            if (++row % getFlushSize() == 0) {
                ws.flush();
            }
        }
    }

    @Override
    public void amMakePage(Worksheet ws, int pageStartRowIdx, int sheetIndex, Object searchCondition) throws IOException {
        
    	SttTrainDataAmSearchCondition bpSearchCondition = castAmSearchCondition(searchCondition);

        List<ServiceModelVO> serviceModelList = serviceModelService.listAll();

        PageHelper.startPage(pageStartRowIdx / getPageSize() + (sheetIndex * ExcelConstants.MAX_ROWS_PER_SHEET / getPageSize()) + 1,getPageSize(), "AM_DATA_ID DESC");

        Page<SttTrainAmDataVO> page = sttTrainDataRepository.amDataSearch(bpSearchCondition);
        List<SttTrainAmDataVO> results = page.getResult();

        int row = pageStartRowIdx + 1;
        
        Map<String, String> serviceNameMap = new HashMap<>();
        
        for (ServiceModelVO serviceModel : serviceModelList) {
        	serviceNameMap.put(serviceModel.getServiceCode(), serviceModel.getServiceModelName());
        }
        
        for (SttTrainAmDataVO sttTrainAmDataVO : results) {
            ws.value(row, 0, row);
            ws.value(row, 1, getDefaultStringIfEmpty(sttTrainAmDataVO.getModelType() ));
            ws.value(row, 2, getDefaultStringIfEmpty(serviceNameMap.get(sttTrainAmDataVO.getServiceModelId().toString())));
            ws.value(row, 3, getDefaultStringIfEmpty(sttTrainAmDataVO.getDatasetName()));
            ws.value(row, 4, getDefaultStringIfEmpty(sttTrainAmDataVO.getTrainVoiceCount()));
            ws.value(row, 5, getDefaultStringIfEmpty(sttTrainAmDataVO.getDescription()));
            ws.value(row, 6, getDefaultStringIfEmpty(sttTrainAmDataVO.getRegDt()));
            ws.value(row, 7, getDefaultStringIfEmpty(sttTrainAmDataVO.getUpdDt()));

            ws.range(row, 0, row, getHeaders().size() - 1).style().horizontalAlignment("center").set();

            if (++row % getFlushSize() == 0) {
                ws.flush();
            }
        }
    }

//    private String convertIdToName(Integer serviceModelId, List<ServiceModelVO> serviceModelList) {
//        String result = "";
//        
//        for (ServiceModelVO serviceModel : serviceModelList) {
//           if (serviceModel.getServiceCode().equals(serviceModelId.toString()) ) { // 이전 비교방식으로는 값이 동일하더라도 동일여부 체크가 안되었음
//                result = serviceModel.getServiceModelName();
//            }
//        }
//        
//        return result;
//    }

    private SttTrainDataSearchCondition castSearchCondition(Object searchCondition) {
        SttTrainDataSearchCondition searchConditionDto = new SttTrainDataSearchCondition();

        try {
            searchConditionDto = (SttTrainDataSearchCondition) searchCondition;
        } catch (ClassCastException e) {
            log.error(e.getMessage());
        }

        return searchConditionDto;
    }
    
    private SttTrainDataAmSearchCondition castAmSearchCondition(Object searchCondition) {
    	
        SttTrainDataAmSearchCondition searchConditionDto = new SttTrainDataAmSearchCondition();

        try {
            searchConditionDto = (SttTrainDataAmSearchCondition) searchCondition;
        } catch (ClassCastException e) {
            log.error(e.getMessage());
        }

        return searchConditionDto;
    }

    private String getDefaultStringIfEmpty(Object obj) {
        return !ObjectUtils.isEmpty(obj) ? obj.toString() : "";
    }
}
