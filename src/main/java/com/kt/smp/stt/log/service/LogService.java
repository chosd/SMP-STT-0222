package com.kt.smp.stt.log.service;

import com.kt.smp.common.util.crypto.AudioCrypto;
import com.kt.smp.common.util.crypto.TextCrypto;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.common.ServiceModel;
import com.kt.smp.stt.common.TrainDataType;
import com.kt.smp.stt.dictation.dto.DictationSaveDto;
import com.kt.smp.stt.dictation.service.DictationService;
import com.kt.smp.stt.log.dto.*;
import com.kt.smp.stt.log.repository.LogRepository;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataVO;
import com.kt.smp.stt.train.trainData.service.SttTrainDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LogService {

    private final LogRepository logRepository;
    private final ServiceModelService serviceModelService;
    private final SttTrainDataService trainDataService;
    private final DictationService dictationService;
    private final AudioCrypto audioCrypto;
    private final TextCrypto textCrypto;

    public int count(LogSearchCondition searchCondition) {
    	convertStartAt(searchCondition);
    	log.info("startAtTo >>> {}",searchCondition.getStartAtTo());
        return logRepository.count(searchCondition);
    }

    public List<LogListDto> search(LogSearchCondition searchCondition) {
    	
    	List<LogListDto> logList = logRepository.search(searchCondition);
    	
        for (LogListDto log : logList) {
            if(log.getWorstTranscript() != null) {
            	log.setWorstTranscript(textCrypto.decrypt(log.getWorstTranscript()));
            }
        }
//        convertStartAt(searchCondition);
        log.info("startAtTo >>> {}",searchCondition.getStartAtTo());
        return logRepository.search(searchCondition);
    }
    
    public LogSearchCondition convertStartAt(LogSearchCondition searchCondition) {
    	/*
    	 * 수협향 커스터마이징 
    	 * startAtTo값이 날짜만 넘어와 조건문을 통하여 시간 추가
    	 * 1. startAtTo가 없는 경우 현재시간에서 -1시간
    	 * 2. startAtTo가 있는데 오늘 날짜인 경우 현재시간에서 -1시간
    	 * 3. startAtTo가 있는데 오늘 날짜가 아닌 경우 23:59:59
    	 * */
//    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    	String currentDate = LocalDate.now().format(formatter);
    	
//    	if(searchCondition.getStartAtTo() == null || searchCondition.getStartAtTo().equals(currentDate)) {
//    		searchCondition.setStartTimeAtTo(LocalDateTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//    	}
    	if(searchCondition.getStartAtTo() != null) {
    		searchCondition.setStartTimeAtTo(searchCondition.getStartAtTo().concat(" 23:59:59"));
    	}
    	return searchCondition;
    }

    public LogDto get(Integer id) {
        return logRepository.findById(id);
    }

    public LogDetailDto getByCallKey(String callKey) {

        List<LogDto> logList = logRepository.findByCallKey(callKey);
        if(logList.size() > 0) {
        	for (LogDto log : logList) {
                if(log.getTranscript() != null) {
                	log.setTranscript(textCrypto.decrypt(log.getTranscript()));
                }
            }

            LogDto firstLog = logList.get(0);

    		return LogDetailDto.builder()
	    		.callKey(firstLog.getCallKey())
	    		.status(firstLog.getStatus().getLabel())
	    		.serviceModelName(firstLog.getServiceModelName())
	    		.direction(firstLog.getDirection())
	    		.startAt(firstLog.getStartAt())
	    		.logList(logList)
	    		.build();
        } else {
        	return LogDetailDto.builder()
	    		.callKey("")
	    		.status("")
	    		.serviceModelName("")
	    		.direction(null)
	    		.startAt("")
	    		.logList(null)
	    		.build();
        }
        
    }

    @Transactional
    public void save(LogSaveDto newLog) {
        logRepository.save(newLog);
    }

    @Transactional
    public void save(List<LogSaveDto> newLogList) {
        for (LogSaveDto newLog : newLogList) {
            save(newLog);
        }
    }

    @Transactional
    public void delete(LogDeleteDto target) {

        List<String> targetCallKeyList = target.getTargetCallKeyList();
        for (String callKey : targetCallKeyList) {

            List<LogDto> logList = logRepository.findByCallKey(callKey);

            for (LogDto log : logList) {
                logRepository.delete(log.getId());
            }
        }
    }

    @Transactional
    public void saveTrainData(TrainDataSaveDto newTrainData) {

        ServiceModelVO serviceModel = serviceModelService.detailByServiceCode(newTrainData.getServiceCode());
        if (serviceModel == null) {
            throw new IllegalArgumentException("알 수 없는 서비스모델입니다");
        }
        newTrainData.setServiceModelId(serviceModel.getServiceCode());
        List<SttTrainDataVO> trainDataList = makeTrainData(newTrainData);
//    	String projectCode = TenantContextHolder.getProjectCode();

        for (SttTrainDataVO trainData : trainDataList) {

            if (isValidTrainData(trainData)) {
//            	if(PreferenceValueHolder.textEncrypt.get(projectCode)) {
//            		trainData.setContents(textCrypto.encrypt(trainData.getContents()));
//            	}
                trainDataService.insert(trainData);
            }
        }
    }

    private boolean isValidTrainData(SttTrainDataVO trainData) {

        return !trainDataService.hasInvalidContents(trainData.getContents()) &&
               !trainDataService.hasDuplicateContents(trainData.getContents(), ServiceModel.CallBot.getCode().toString());
    }

    private List<SttTrainDataVO> makeTrainData(TrainDataSaveDto newTrainData) {

        List<SttTrainDataVO> trainDataList = new ArrayList<>();
        String[] contentList = newTrainData.splitContent();

        for (String content : contentList) {
            trainDataList.add(buildTrainData(content, newTrainData));
        }

        return trainDataList;
    }

    private SttTrainDataVO buildTrainData(String content, TrainDataSaveDto newTrainData) {

        return SttTrainDataVO
                .builder()
                .dataType(TrainDataType.COMMON)
                .serviceModelId(newTrainData.getServiceModelId())
                .contents(content)
                .repeatCount(newTrainData.getWeight())
                .description(newTrainData.getDescription())
                .regId(newTrainData.getRegId())
                .regIp(newTrainData.getRegIp())
                .updId(newTrainData.getUpdId())
                .updIp(newTrainData.getUpdIp())
                .build();
    }

    @Transactional
    public void saveDictation(HttpServletRequest request, LogDto log) {

        DictationSaveDto dictation = makeDictation(log);
        dictation.audit(request);
        dictationService.save(dictation);
        logRepository.updateUsedAsDictation(log.getId(), "Y");
    }

    @Transactional
    public void saveDictation(HttpServletRequest request, BulkDictationSaveDto bulkDictationSave) {

        List<String> callKeyList = bulkDictationSave.getCallKeyList();
        
        if (isExistLogInDictation(callKeyList)) {
        	throw new IllegalArgumentException("이미 전사데이터로 등록된 결과가 포함되어 있습니다.");
        };

        for (String callKey : callKeyList) {
            List<LogDto> logList = logRepository.findByCallKey(callKey);
            for (LogDto log : logList) {
                saveDictation(request, log);
            }
        }
    }

    private boolean isExistLogInDictation(List<String> callKeyList) {
        int count = logRepository.isExistLogInDictationByCallkeyList(callKeyList);
        if (count > 0) {
        	return true;
        }
		return false;
	}

	private DictationSaveDto makeDictation(LogDto log) {

        return new DictationSaveDto(
        		log.getId(),
                log.getTranscript(),
                log.getWavFilePath(),
                log.getServiceModelId(),
                log.getDirection());
    }

    public byte[] getWavFileBytes(Integer id) throws IOException {
    	
        LogDto logDto = logRepository.findById(id);
        String filePath = logDto.getWavFilePath();
        
        if(filePath == null) {
        	filePath = logDto.getSentenceWavFilePath();
        }
        
        if (audioCrypto.isEncrypted(filePath)) {
        	return audioCrypto.getDecryptedByteArray(filePath);
        }
        
        Path wavFilePath = Paths.get(filePath);

        return Files.readAllBytes(wavFilePath);
        
    }
}
