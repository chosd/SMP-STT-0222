package com.kt.smp.stt.operate.session.dto;

import java.util.List;

import lombok.*;

/**
 * @author wonyoung.ahn
 * @title SttServerSessionInfoDTO
 * @see\n <pre>
 * </pre>
 * @since 2023-09-13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SttServerSessionInfoDto {
	//serviceCode 이름
    private String serviceCode;
    //해당 serviceCode의 전체 STT 세션 수
    private Integer sessionMaxCnt;
    //현재 연결된 세션 수
    private Integer sessionCnt;
    //해당 serviceCode의 연결된 STT 세션상세 정보  
    private List<SttSessionInfoDto> sessionInfo;
}
