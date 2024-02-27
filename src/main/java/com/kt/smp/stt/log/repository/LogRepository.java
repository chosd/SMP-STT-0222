package com.kt.smp.stt.log.repository;

import com.kt.smp.stt.log.dto.LogDto;
import com.kt.smp.stt.log.dto.LogListDto;
import com.kt.smp.stt.log.dto.LogSaveDto;
import com.kt.smp.stt.log.dto.LogSearchCondition;
import com.kt.smp.stt.log.mapper.LogMapper;
import com.kt.smp.stt.log.type.CallStatus;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogRepository {

    private final LogMapper mapper;

    public LogRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(LogMapper.class);
    }

    public int count(LogSearchCondition searchCondition) {
        return mapper.count(searchCondition);
    }

    public List<LogListDto> search(LogSearchCondition searchCondition) {
        return mapper.search(searchCondition);
    }

    public LogDto findById(Integer id) {
        return mapper.findById(id);
    }

    public List<LogDto> findByCallKey(String callKey) {
        return mapper.findByCallKey(callKey);
    }

    public String findFirstStartAtByCallKey(String startAt) {
        return mapper.findFirstStartAtByCallKey(startAt);
    }

    public List<String> findTranscriptByCallKeyAndCer(String callKey, Double cer) {
        return mapper.findTranscriptByCallKeyAndCer(callKey, cer);
    }

    public String findServiceModelNameByCallKey(String callKey) {
        return mapper.findServiceModelNameByCallKey(callKey);
    }
    public LogDto findByWavFilePath(String wavFilePath) {
        return mapper.findByWavFilePath(wavFilePath);
    }

    public int countByCallKeyAndStatus(String callKey, CallStatus status) {
        return mapper.countByCallKeyAndStatus(callKey, status);
    }

    public void save(LogSaveDto newLog) {
        mapper.save(newLog);
    }

    public void delete(Integer callLogId) {
        mapper.delete(callLogId);
    }

    public void updateUsedAsDictation(Integer id, String usedAsDictation) {
        mapper.updateUsedAsDictation(id, usedAsDictation);
    }

	public int isExistLogInDictationByCallkeyList(List<String> callKeyList) {
		return mapper.isExistLogInDictationByCallkeyList(callKeyList);
	}
}
