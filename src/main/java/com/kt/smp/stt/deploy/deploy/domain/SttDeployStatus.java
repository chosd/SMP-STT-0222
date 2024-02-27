package com.kt.smp.stt.deploy.deploy.domain;

import com.google.gson.Gson;
import lombok.*;

/**
 * @author jaime
 * @title SttDeployStatus
 * @see\n <pre>
 * </pre>
 * @since 2022-03-14
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SttDeployStatus {

    // SRU 서버 이름
    private String serverName;

    /**
     * ready: 미배포 상태
     * deploying: 배포 중
     * complete: 배포 완료
     * fail: 배포 실패패
    */
    private String status;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
