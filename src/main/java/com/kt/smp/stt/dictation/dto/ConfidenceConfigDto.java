package com.kt.smp.stt.dictation.dto;

import com.kt.smp.common.domain.BaseModel;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author jaime
 * @title SttStatisticVO
 * @see\n <pre>
 * </pre>
 * @since 2022-07-04
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConfidenceConfigDto extends BaseModel{
	
    // 서비스 코드명
    private String serviceCode;
    
    // 서비스 모델명
    private String serviceModelName;
    
    // 서비스 모델 아이디
    private Long serviceModelId;
    
    // 신뢰도 값
    private float confidence;
    
    // 저장할 파일 최대 개수
    private Integer fileSaveCount;
    
    // 저장할 파일 현재 개수
    private Integer fileStartCount;
    
    // 음원파일 암호화 유무
    private String encryptYn;
    
    // 신뢰도 기능 사용 여부
    private String useYn;
    
    // description
    private String description;
    
    // 현재 실행 여부
    private String exeYn;
    
    @Override
    public String toString() {
        return "SttConfidenceConfigVO{" +
                "serviceCode=" + serviceCode +
                ", serviceModelName='" + serviceModelName + '\'' +
                ", serviceModelId='" + serviceModelId + '\'' +
                ", confidence=" + confidence +
                '}';
    }
}
