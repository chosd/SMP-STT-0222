package com.kt.smp.stt.operate.session.dto;

import com.google.gson.Gson;
import lombok.*;

/**
 * @author jieun.chang
 * @title ChannelInfo
 * @see\n <pre>
 * </pre>
 * @since 2023-01-11
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionInfoDto {

    /**
     * 채널 점유 중 인지에 대한 상태 값
     * inuse: 사용중
     * ready: 대기중
     * delay: 지연중
     * fail: 서버 연동 실패
     */
    private String status;
    
    //STT세션 INDEX
    private Integer sessionIdx;
    //세션 사용 상태
    private boolean inUse;
    // 가장 최근에 처리된 STT 응답시간
    private Integer responseTime;
    //세션 콜키 정보
    private String callKey;
    // 호 시작시간
    private long sessionStartTime;
    // 콜 유지 시간
    private long sessionHoldingTime;
    // 상담사ID
    private String deviceId;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
