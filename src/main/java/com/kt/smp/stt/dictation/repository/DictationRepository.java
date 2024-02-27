package com.kt.smp.stt.dictation.repository;

import com.kt.smp.stt.common.dto.SttConfigDto;
import com.kt.smp.stt.confidence.dto.ConfidenceConfigInsertDTO;
import com.kt.smp.stt.dictation.dto.*;
import com.kt.smp.stt.dictation.mapper.DictationMapper;
import com.kt.smp.stt.dictation.type.UsageType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DictationRepository {

    private final DictationMapper mapper;

    public DictationRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(DictationMapper.class);
    }

    public int count(DictationSearchCondition searchCondition) {
        return mapper.count(searchCondition);
    }

    public List<DictationListDto> search(DictationSearchCondition searchCondition) {
        return mapper.search(searchCondition);
    }

    public List<DictationDto> findAll() {
        return mapper.findAll();
    }

    public DictationDto findById(Integer id) {
        return mapper.findById(id);
    }

    public void save(DictationSaveDto dictation) {
        mapper.save(dictation);
    }

    public void update(DictationUpdateDto modifiedDictation) {
        mapper.update(modifiedDictation);
    }

    public void updateType(Integer id, UsageType usageType) {
        mapper.updateType(id, usageType);
    }
    
    public void updateReUse(DictationSaveDto dictation) {
    	mapper.updateReUse(dictation);
    };

    public void delete(Integer id) {
        mapper.delete(id);
    }

    public int countByWavFilePath(String wavFilePath) {
        return mapper.countByWavFilePath(wavFilePath);
    }

    public DictationDto findByWavFilePath(String wavFilePath) {
        return mapper.findByWavFilePath(wavFilePath);
    }

    public void updatePreempt(Integer id, String isPreempted) {
        mapper.updatePreempt(id, isPreempted);
    }

    public SttConfigDto confidenceConfig(ConfidenceConfigDto dto) {
        return mapper.confidenceConfig(dto);
    }
    
    public int confidenceSave(List<ConfidenceConfigInsertDTO> dto) {
        return mapper.confidenceSave(dto);
    }
    
    public int countByDeprecatedWavFilePath(String wavFilePath) {
		return mapper.countByDeprecatedWavFilePath(wavFilePath);
	}

	public int getIdByWavFilePath(String wavFilePath) {
		return mapper.getIdByWavFilePath(wavFilePath);
	}
	
	public int findIdBySttLogId(Integer logId) {
		return mapper.findIdBySttLogId(logId);
	}

	public String findUseYnBySttLogId(Integer logId) {
		return mapper.findUseYnBySttLogId(logId);
	}
}
