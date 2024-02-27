package com.kt.smp.stt.dictation.mapper;

import com.kt.smp.stt.common.dto.SttConfigDto;
import com.kt.smp.stt.confidence.dto.ConfidenceConfigInsertDTO;
import com.kt.smp.stt.dictation.dto.*;
import com.kt.smp.stt.dictation.service.DictationService;
import com.kt.smp.stt.dictation.type.UsageType;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DictationMapper {
    int count(DictationSearchCondition searchCondition);

    List<DictationListDto> search(DictationSearchCondition searchCondition);

    List<DictationDto> findAll();

    DictationDto findById(Integer id);

    void save(DictationSaveDto dictation);

    void update(DictationUpdateDto modifiedDictation);

    void updateType(Integer id, UsageType usageType);
    
    void updateReUse(DictationSaveDto dictation);
    
    void delete(Integer id);

    int countByWavFilePath(String wavFilePath);

    DictationDto findByWavFilePath(String wavFilePath);

    void updatePreempt(Integer id, String isPreempted);

    public SttConfigDto confidenceConfig(ConfidenceConfigDto dto);
    
    public int confidenceSave(List<ConfidenceConfigInsertDTO> dto);

    int countByDeprecatedWavFilePath(String wavFilePath);

	int getIdByWavFilePath(String wavFilePath);

	String findUseYnBySttLogId(Integer logId);

	int findIdBySttLogId(Integer logId);
}
