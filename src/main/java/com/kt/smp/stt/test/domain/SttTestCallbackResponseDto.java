package com.kt.smp.stt.test.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author jaime
 * @title SttTestCallbackResponseDto
 * @see\n <pre>
 * </pre>
 * @since 2022-05-31
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SttTestCallbackResponseDto {

    /**
     * 하나_SMP_STT API_연동규격서_v1.0.docx 결과코드 3.3 정의에 따름
     */
    private String resultCode;

    /**
     * 성공: success
     * 실패: 배포 실패 원인 메시지
     */
    private String resultMsg;

    @Override
    public String toString() {
        return "SttDeployResponseDto{" +
                "resultCode='" + resultCode + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                '}';
    }
}
