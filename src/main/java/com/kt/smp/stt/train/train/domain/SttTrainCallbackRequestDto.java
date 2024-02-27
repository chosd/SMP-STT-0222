package com.kt.smp.stt.train.train.domain;

import com.kt.smp.stt.common.dto.BaseResultDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author jaime
 * @title SttTrainCallbackRequestDto
 * @see\n <pre>
 * </pre>
 * @since 2022-04-05
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SttTrainCallbackRequestDto extends BaseResultDto {

    // serviceCode 이름
    private String serviceCode;

    /**
     * 학습 상태
     * ready: 미학습 상태
     * training: 학습 중
     * complete: 학습 완료
     * fail: 학습 실패
     */
    private String status;

    /**
     * 학습된 lmType
     * CLASS: class LM
     * SERVICE: service LM
     * E2ESL : 지도학습(Fine Tunning)
     * E2EUSL : 비지도학습
     * E2ELM : 텍스트학습(biasing LM)
     * E2EMSL : 반지도 학습(비지도+지도)
     */
    private String modelType;

    // 학습된 모델 저장경로 (nas 경로)
    private String modelPath;

    // 학습 모델의 무결성 검증을 위한 md5 hash checksum (32자리)
    private String modelAuthKey;
    
    // 학습한 음성데이터의 전체시간, E2ESL/E2EMSL/E2EUSL 일 경우만
    private String trainDataTime;

    @Override
    public String toString() {
        return super.toString() + "SttTrainCallbackRequestDto{" +
                ", serviceCode='" + serviceCode + '\'' +
                ", status='" + status + '\'' +
                ", modelType='" + modelType + '\'' +
                ", modelPath='" + modelPath + '\'' +
                ", modelAuthKey='" + modelAuthKey + '\'' +
                ", trainDataTime='" + trainDataTime + '\'' +
                '}';
    }
}
