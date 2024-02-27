/**
 * 
 */
package com.kt.smp.stt.comm.preference.dto;

import lombok.Data;

@Data
public class AgentConfigDto {
	
	// 세션 대상 서버
	private String sessionTarget;				// "host" or "sub"
	
	// 멀티파트 정보
	private String multipartHostDeploy;			// HOST 배포 요청 MULTIPART 사용 여부 ("Y" or "N)
	private String multipartHostTest;			// HOST 단건 테스트 요청 MULTIPART 사용 여부 ("Y" or "N)
	private String multipartSubDeploy;			// HOST-DEPLOY 배포 요청 MULTIPART 사용 여부 ("Y" or "N)
	private String multipartSubTest;			// HOST-DEPLOY 단건 테스트 요청 MULTIPART 사용 여부 ("Y" or "N)
}
