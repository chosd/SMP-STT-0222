package com.kt.smp.stt.test.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author jaime
 * @title SttTestResponseDto
 * @see\n <pre>
 * </pre>
 * @since 2022-03-22
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SttTestResponseDto {

    /**
     * 결과코드 3.3 정의에 따름
     */
    private String resultCode;

    /**
     * 단건 테스트 요청 결과
     * - 성공: "success"
     * - 실패: 테스트 요청 실패 메시지
     */
    private String resultMsg;
}
