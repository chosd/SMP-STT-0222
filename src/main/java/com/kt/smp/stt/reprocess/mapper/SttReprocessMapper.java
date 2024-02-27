/**
 * 
 */
package com.kt.smp.stt.reprocess.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.smp.stt.reprocess.dto.ReprocessLogDto;
import com.kt.smp.stt.reprocess.dto.ReprocessStatusDto;
import com.kt.smp.stt.reprocess.dto.SttReprocessRequestDto;
import com.kt.smp.stt.reprocess.enums.ReprocessStatus;

@Mapper
public interface SttReprocessMapper {

	public void insert(List<SttReprocessRequestDto> sttReprocessRequestDto);

	public int isApplicationIdDuplicated(String applicationId);

	public ReprocessLogDto getReprocessLog(String applicationId);

	public ReprocessStatusDto getReprocessStatus(String applicationId);

	public void updateReprocessStatus(List<String> applicationList);

	public void autoReprocess();

	public void updateAutoReprocessStatus();

}
