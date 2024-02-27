/**
 * 
 */
package com.kt.smp.stt.reprocess.service;

import java.util.List;

import com.kt.smp.stt.reprocess.dto.ReprocessLogDto;
import com.kt.smp.stt.reprocess.dto.ReprocessStatusDto;
import com.kt.smp.stt.reprocess.dto.SttReprocessRequestDto;
import com.kt.smp.stt.reprocess.dto.SttReprocessResponseDto;

/**
* @FileName : SttReprocessApiService.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 17.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 :
*/
public interface SttReprocessService {

	SttReprocessResponseDto reprocess(List<SttReprocessRequestDto> sttReprocessRequestDto);

	ReprocessLogDto getReprocessLog(String applicationId);

	ReprocessStatusDto getReprocessStatus(String applicationId);
	
	void autoReprocess();

}
