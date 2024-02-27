package com.kt.smp.stt.dictation.mapper;

import com.kt.smp.stt.dictation.dto.UsageDto;
import com.kt.smp.stt.dictation.dto.UsageSaveDto;
import com.kt.smp.stt.dictation.type.UsageType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UsageMapper {

    List<UsageDto> findByDictationId(Integer dictationId);

    List<UsageDto> findByDictationIdAndType(Integer dictationId, UsageType type);

    void save(UsageSaveDto usage);

    void updateType(Integer id, UsageType type);

    void delete(Integer id);

    void deleteByDictationId(Integer dictationId);
}
