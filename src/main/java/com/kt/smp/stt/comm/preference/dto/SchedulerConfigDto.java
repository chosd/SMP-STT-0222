/**
 * 
 */
package com.kt.smp.stt.comm.preference.dto;

import java.util.List;

import com.kt.smp.stt.comm.preference.enums.DatasToRemove;
import com.kt.smp.stt.comm.preference.enums.FilesToRemove;

import lombok.Data;

@Data
public class SchedulerConfigDto {

	private String schedulerUseYn;					// 스케쥴러 사용여부 ('Y' or 'N)
	private String removerTime;						// 삭제 실행 시간 (시)
	private int removerStandard;					// 삭제 기준 (일)
	private int hwResourceCycle;					// HW 리소스 조회 주기 (초)
	private int statisticsCycle;					// 통계 조회 주기 (분)
	
	private List<FilesToRemove> removerFileList;	// 삭제할 파일 목록
	private List<DatasToRemove> removerDataList;	// 삭제할 데이터 목록
}
