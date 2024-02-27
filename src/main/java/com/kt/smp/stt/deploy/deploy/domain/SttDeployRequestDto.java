package com.kt.smp.stt.deploy.deploy.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author jaime
 * @title SttDeployRequestDto
 * @see\n <pre>
 * </pre>
 * @since 2022-03-14
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SttDeployRequestDto {

    // serviceCode 이름
    private String serviceCode;

    // 학습 모델의 무결성 검증을 위한 md5 hash checksum (32자리)
    private String modelAuthKey;

    // 배포할 모델파일이 위치한 경로 (nas에 위치)
    private String modelPath;

    // 배포 완료시 callback할 URL
    private String callbackUrl;

    /**
     * 배포할 modelType
     * CLASS: class LM
     * SERVICE: service LM
     */
    private String modelType;
}
