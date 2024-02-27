/**
 * 
 */
package com.kt.smp.stt.comm.preference.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = Shape.OBJECT)
public enum DatasToRemove {
	
	AM_TRAIN_DATA("REMOVER_DATA_AM_TRAIN_DATA", "AM 학습데이터")
	,LM_TRAIN_DATA("REMOVER_DATA_LM_TRAIN_DATA", "LM 학습데이터")
	,TRAIN_MNG("REMOVER_DATA_TRAIN_MNG", "학습 내역")
	,VERIFY_DATA("REMOVER_DATA_VERIFY_DATA", "검증 데이터")
	,VERIFY_MNG("REMOVER_DATA_VERIFY_MNG", "검증 이력")
	,DEPLOY_MODEL("REMOVER_DATA_DEPLOY_MODEL", "배포 모델")
	,DEPLOY_MNG("REMOVER_DATA_DEPLOY_MNG", "배포 이력")
	,CALLINFO("REMOVER_DATA_CALLINFO", "상담 내역")
	,STT_RESULT("REMOVER_DATA_STT_RESULT", "STT 결과")
	,HW_RESOURCE("REMOVER_DATA_HW_RESOURCE", "HW 리소스 데이터")
	,STATISTICS("REMOVER_DATA_STATISTICS", "통계 데이터")
	,TEST("REMOVER_DATA_TEST", "단건 테스트")
	;
	
	private final String description;
	
	private final String name;

	public static DatasToRemove findByDescription(String description) {
		DatasToRemove result = null;
		
		for (DatasToRemove dataToRemove : DatasToRemove.values()) {
			if (dataToRemove.description.equals(description) ) {
				result = dataToRemove;
			}
		}
		
		return result;
	}
}
