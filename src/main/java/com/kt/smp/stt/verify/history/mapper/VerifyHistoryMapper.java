package com.kt.smp.stt.verify.history.mapper;

import com.kt.smp.stt.verify.history.dto.*;
import com.kt.smp.stt.verify.history.type.VerifyStatus;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VerifyHistoryMapper {
    public int count(VerifyHistorySearchCondition searchCondition);

    public List<VerifyHistoryListDto> search(VerifyHistorySearchCondition searchCondition);

    public VerifyHistoryDto findById(int id);

    public VerifyHistoryDto findLatestByServiceModelIdAndStatus(CallbackUpdateDto callbackUpdateDto);

    public void save(VerifyHistorySaveDto history);

    public void update(VerifyHistoryUpdateDto modifiedHistory);

    public int updateSaveYn(int id);

	public void updateVerifyStatus(Map<String, Object> params);
}
