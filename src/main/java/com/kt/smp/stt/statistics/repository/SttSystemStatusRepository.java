/**
 * 
 */
package com.kt.smp.stt.statistics.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.kt.smp.stt.monitoring.dto.SystemStatusSearchRequestDto;
import com.kt.smp.stt.monitoring.dto.SystemStatusSearchResult;
import com.kt.smp.stt.statistics.domain.SttServerInfoVO;
import com.kt.smp.stt.statistics.mapper.SttSystemStatusMapper;

/**
*@FileName : SttSystemStatusRepository.java
@Project : kt-stt-service_r
@Date : 2023. 10. 17.
*@작성자 : rapeech
*@변경이력 :
*@프로그램설명 :
*/
@Repository
public class SttSystemStatusRepository {
	
	private SttSystemStatusMapper mapper;
	
    public SttSystemStatusRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(SttSystemStatusMapper.class);
    }
    
    public Integer count(SystemStatusSearchRequestDto searchConditonDto) {
    	return mapper.count(searchConditonDto);
    }
    
	public Integer countByDate(SystemStatusSearchRequestDto searchConditionDto) {
		return mapper.countByDate(searchConditionDto);
	}
    
    //stt 조회
    public Page<SystemStatusSearchResult> listPage(SystemStatusSearchRequestDto searchConditionDto){
    	return mapper.listPage(searchConditionDto);
    }
    
	public Page<SystemStatusSearchResult> listByDate(SystemStatusSearchRequestDto searchConditionDto) {
		return mapper.listByDate(searchConditionDto);
	}

	public int exists(SttServerInfoVO serverInfo) {
		return mapper.exists(serverInfo);
	}
	
	public int insert(SttServerInfoVO serverInfo) {
		return mapper.insert(serverInfo);
	}
	
	public int generateSystemStatusStatHour() {
		return mapper.insertSystemStatusStatHour();
	}
	
	public List<SystemStatusSearchResult> lastData(){
		return mapper.getLastData();
	}

	public List<SystemStatusSearchResult> getRealtimeChart() {
		return mapper.getRealtimeChart();
	}

}
