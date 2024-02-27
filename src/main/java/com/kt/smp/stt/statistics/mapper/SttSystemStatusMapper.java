/**
 * 
 */
package com.kt.smp.stt.statistics.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.kt.smp.stt.monitoring.dto.SystemStatusSearchRequestDto;
import com.kt.smp.stt.monitoring.dto.SystemStatusSearchResult;
import com.kt.smp.stt.statistics.domain.SttServerInfoVO;

/**
*@FileName : SttSystemStatusMapper.java
@Project : kt-stt-service_r
@Date : 2023. 10. 17.
*@작성자 : rapeech
*@변경이력 :
*@프로그램설명 :
*/
@Mapper
public interface SttSystemStatusMapper {
	
	int exists(SttServerInfoVO serverInfo);
    /**
     * Insert int.
     *
     * @param SttServerInfoVO
     * @return int
     */
	int insertSystemStatusStatHour();
	
	int insert(SttServerInfoVO serverInfo);
	
	Integer count(SystemStatusSearchRequestDto searchConditionDto);
	
	Integer countByDate(SystemStatusSearchRequestDto searchConditionDto);
	
	Page<SystemStatusSearchResult> listPage(SystemStatusSearchRequestDto searchConditionDto);
	
	List<SystemStatusSearchResult> list(SystemStatusSearchRequestDto searchConditionDto);
	
	Page<SystemStatusSearchResult> listByDate(SystemStatusSearchRequestDto searchConditionDto);
	
	List<SystemStatusSearchResult> rowListByDate(SystemStatusSearchRequestDto searchConditionDto);

	List<SystemStatusSearchResult> getLastData();
	
	List<SystemStatusSearchResult> getRealtimeChart();

}
