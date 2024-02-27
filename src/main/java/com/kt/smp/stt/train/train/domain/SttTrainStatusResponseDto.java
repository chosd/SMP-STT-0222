package com.kt.smp.stt.train.train.domain;

import com.kt.smp.stt.common.dto.BaseResultDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author jaime
 * @title SttTrainStatusResponseDto
 * @see\n <pre>
 * </pre>
 * @since 2022-04-05
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SttTrainStatusResponseDto extends BaseResultDto {

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
     */
    private String modelType;

    // 학습된 모델 저장경로 (nas 경로)
    private String modelPath;

    // 학습 모델의 무결성 검증을 위한 md5 hash checksum (32자리)
    private String modelAuthKey;

    @Override
    public String toString() {
        return super.toString() + "SttTrainStatusResponseDto{" +
                "serviceCode='" + serviceCode + '\'' +
                ", status='" + status + '\'' +
                ", modelType='" + modelType + '\'' +
                ", modelPath='" + modelPath + '\'' +
                ", modelAuthKey='" + modelAuthKey + '\'' +
                '}';
    }
}
