package com.kt.smp.stt.dictation.repository;

import com.kt.smp.stt.dictation.dto.UsageDto;
import com.kt.smp.stt.dictation.dto.UsageSaveDto;
import com.kt.smp.stt.dictation.mapper.UsageMapper;
import com.kt.smp.stt.dictation.type.UsageType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsageRepository {

    private final UsageMapper mapper;

    public UsageRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(UsageMapper.class);
    }

    public List<UsageDto> findByDictationId(Integer dictationId) {
        return mapper.findByDictationId(dictationId);
    }

    public List<UsageDto> findByDictationIdAndType(Integer dictationId, UsageType type) {
        return mapper.findByDictationIdAndType(dictationId, type);
    }

    public void save(UsageSaveDto usage) {
        mapper.save(usage);
    }

    public void updateType(Integer id, UsageType type) {
        mapper.updateType(id, type);
    }

    public void delete(Integer id) {
        mapper.delete(id);
    }

    public void deleteByDictationId(Integer dictationId) {
        mapper.deleteByDictationId(dictationId);
    }
}
