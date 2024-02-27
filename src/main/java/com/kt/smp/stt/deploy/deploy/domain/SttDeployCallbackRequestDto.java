package com.kt.smp.stt.deploy.deploy.domain;

import com.google.gson.Gson;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jaime
 * @title SttDeployCallbackRequestDto
 * @see\n <pre>
 * </pre>
 * @since 2022-03-14
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SttDeployCallbackRequestDto {

    // 하나_SMP_STT API_연동규격서 결과코드 3.3 정의에 따름
    private String resultCode;

    /**
     * 성공: success
     * 실패: 배포 실패 원인 메시지
     */
    private String resultMsg;

    // 서비스 코드 이름
    private String serviceCode;

    /**
     * 배포 상태
     * serviceCode에 대한 배포 상태는 serviceCode를 제공하는 SRU 서버들의 배포 상태로 결정
     */
    private String status;

    // serviceCode를 제공하는 SRU 서버들의 배포 상태
    private List<SttDeployStatus> deployList = new ArrayList<>();

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
