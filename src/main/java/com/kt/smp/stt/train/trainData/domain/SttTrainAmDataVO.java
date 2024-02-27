package com.kt.smp.stt.train.trainData.domain;

import com.kt.smp.common.domain.BaseModel;
import com.kt.smp.stt.common.ServiceModel;
import com.kt.smp.stt.common.TrainDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author jieun.chang
 * @title SttTrainDataVO
 * @see\n <pre>
 * </pre>
 * @since 2022-03-23
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SttTrainAmDataVO extends BaseModel {

    /*// 서비스 모델
    private ServiceModel serviceModel;*/

    // 서비스 모델 ID
    private Integer serviceModelId;

    // 데이터 구분 (1 : 일반, 2 : 전사)
    private Integer dataSource;

    // 모델타입
    private String modelType;

    // 정답지 데이터셋 명
    private String datasetName;

    // 설명
    private String description;

    // 학습음성갯수
    private Integer trainVoiceCount;
    
    private String answerFileName;
    
    private String voiceFileName;
    
    private String amDataPath;
    
    private String firstPathYn;

}
