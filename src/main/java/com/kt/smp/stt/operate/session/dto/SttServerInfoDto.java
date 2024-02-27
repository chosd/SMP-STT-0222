package com.kt.smp.stt.operate.session.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wonyoung.ahn
 * @title SttSessionServerInfoDto
 * @see\n <pre>
 * </pre>
 * @since 2023-09-13
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SttServerInfoDto {

	// C-POD 이름
    private String serverName;
    // C-POD 간 통신 연결 상태(True일 경우만 하기내역 출력)
    private Boolean connectionStatus;
    // 해당 C-POD에서 제공하는 Service Code 리스트
    private List<String> supportSvcList;
    //해당 C-POD의 svc 별 STT 세션정보
    private List<SttServerSessionInfoDto> serverSessionInfo;
}
