/**
 * 
 */
package com.kt.smp.stt.reprocess.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.kt.smp.stt.reprocess.dto.ReprocessLogDto;
import com.kt.smp.stt.reprocess.dto.ReprocessStatusDto;
import com.kt.smp.stt.reprocess.dto.SttReprocessRequestDto;
import com.kt.smp.stt.reprocess.mapper.SttReprocessMapper;

@Repository
public class SttReprocessRepository {
	
	private final SttReprocessMapper mapper;
	
	public SttReprocessRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(SttReprocessMapper.class);
    }

	public void insert(List<SttReprocessRequestDto> sttReprocessRequestDto) {
		mapper.insert(sttReprocessRequestDto);
	}

	public boolean isApplicationIdDuplicated(String applicationId) {
		return mapper.isApplicationIdDuplicated(applicationId) > 0;
	}

	public ReprocessLogDto getReprocessLog(String applicationId) {
		return mapper.getReprocessLog(applicationId);
	}

	public ReprocessStatusDto getReprocessStatus(String applicationId) {
		return mapper.getReprocessStatus(applicationId);
	}

	public void updateReprocessStatus(List<String> applicationList) {
		mapper.updateReprocessStatus(applicationList);
	}
	
	public void autoReprocess() {
		mapper.autoReprocess();
	}

	public void updateAutoReprocessStatus() {
		mapper.updateAutoReprocessStatus();
	}

}
