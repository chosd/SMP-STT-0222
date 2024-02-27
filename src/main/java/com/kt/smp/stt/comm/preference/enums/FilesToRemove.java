/**
 * 
 */
package com.kt.smp.stt.comm.preference.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = Shape.OBJECT)
public enum FilesToRemove {

	AM_TRAIN_DATA("REMOVER_FILE_AM_TRAIN_DATA", "AM 학습데이터")
	,DEPLOY_MODEL("REMOVER_FILE_DEPLOY_MODEL", "배포 모델")
	,VERIFY_DATA("REMOVER_FILE_VERIFY_DATA", "검증 데이터")
	,CALL_INFO("REMOVER_FILE_CALLINFO", "상담 내역 청취")
	,TEST("REMOVER_FILE_TEST", "단건 테스트")
	;
	
	private final String description;
	
	private final String name;
	
	public static FilesToRemove findByDescription(String description) {
		FilesToRemove result = null;
		
		for (FilesToRemove fileToRemove : FilesToRemove.values()) {
			if (fileToRemove.description.equals(description) ) {
				result = fileToRemove;
			}
		}
		
		return result;
	}
	
}
