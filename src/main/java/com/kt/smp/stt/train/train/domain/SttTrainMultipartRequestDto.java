package com.kt.smp.stt.train.train.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
*@FileName : SttTrainMultipartRequestDto.java
@Project : kt-stt-service_r
@Date : 2023. 9. 19.
*@작성자 : wonyoung.ahn
*@변경이력 :
*@프로그램설명 :
*/
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SttTrainMultipartRequestDto {

    /// serviceCode명
    private String serviceCode;

    // 학습 완료시 callback할 URL
    private String callbackUrl;
    
    //추가
    private String modelType;
}
