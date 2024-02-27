package com.kt.smp.stt.operate.session.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wonyoung.ahn
 * @title SttSessionInfoDTO
 * @see\n <pre>
 * </pre>
 * @since 2023-09-13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SttSessionInfoDto {
    //STT세션 INDEX
	public Integer sessionIdx;
    //세션 사용 상태
	public Integer inUse;
    // 가장 최근에 처리된 STT 응답시간
	public Integer responseTime;
    //세션 콜키 정보
	public String callKey;
    // 호 시작시간
	public long sessionStartTime;
	// 호 유지시간
	public long sessionHoldingTime;
    // 상담사ID
	public String deviceId;
}
