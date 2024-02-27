package com.kt.smp.stt.verify.history.repository;

import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.verify.history.dto.*;
import com.kt.smp.stt.verify.history.mapper.VerifyHistoryMapper;
import com.kt.smp.stt.verify.history.type.VerifyStatus;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class VerifyHistoryRepository {

    private final VerifyHistoryMapper mapper;

    public VerifyHistoryRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(VerifyHistoryMapper.class);
    }

    public int count(VerifyHistorySearchCondition searchCondition) {
        return mapper.count(searchCondition);
    }

    public List<VerifyHistoryListDto> search(VerifyHistorySearchCondition searchCondition) {
        return mapper.search(searchCondition);
    }

    public VerifyHistoryDto findById(int id) {
        return mapper.findById(id);
    }

    public void save(VerifyHistorySaveDto history) {
        mapper.save(history);
    }

    public void update(VerifyHistoryUpdateDto modifiedHistory) {
        mapper.update(modifiedHistory);
    }

    public VerifyHistoryDto findLatestByServiceModelIdAndStatus(CallbackUpdateDto dto) {
        return mapper.findLatestByServiceModelIdAndStatus(dto);
    }
    
    public int updateSaveYn(int id) {
    	return mapper.updateSaveYn(id);
    }

	public void updateVerifyStatus(Map<String, Object> params) {
		mapper.updateVerifyStatus(params);
	}
}
