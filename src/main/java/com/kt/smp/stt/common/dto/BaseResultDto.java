package com.kt.smp.stt.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResultDto {

    // 하나_SMP_STT API_연동규격서 결과코드 3.3 정의에 따름
    private String resultCode;

    /**
     * 학습 상태 조회 결과
     * 성공: "success"
     * 실패: 학습 조회 실패 원인 메시지
     */
    private String resultMsg;

    @Override
    public String toString() {
        return "BaseDto{" +
                "resultCode='" + resultCode + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                '}';
    }
}
