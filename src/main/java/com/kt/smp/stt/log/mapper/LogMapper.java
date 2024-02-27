package com.kt.smp.stt.log.mapper;

import com.kt.smp.stt.log.dto.LogDto;
import com.kt.smp.stt.log.dto.LogListDto;
import com.kt.smp.stt.log.dto.LogSaveDto;
import com.kt.smp.stt.log.dto.LogSearchCondition;
import com.kt.smp.stt.log.type.CallStatus;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface LogMapper {
    int count(LogSearchCondition searchCondition);

    List<LogListDto> search(LogSearchCondition searchCondition);

    LogDto findById(Integer id);

    List<LogDto> findByCallKey(String callKey);

    String findFirstStartAtByCallKey(String startAt);

    List<String> findTranscriptByCallKeyAndCer(String callKey, Double cer);

    String findServiceModelNameByCallKey(String callKey);
    LogDto findByWavFilePath(String wavFilePath);

    int countByCallKeyAndStatus(String callKey, CallStatus status);

    void save(LogSaveDto newLog);

    void delete(Integer callLogId);

    void updateUsedAsDictation(Integer id, String usedAsDictation);

	int isExistLogInDictationByCallkeyList(List<String> callKeyList);
}
