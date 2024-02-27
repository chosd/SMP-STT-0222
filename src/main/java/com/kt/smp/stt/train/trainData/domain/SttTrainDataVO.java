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
public class SttTrainDataVO extends BaseModel {

    // 데이터 구분
    private TrainDataType dataType;

    /*// 서비스 모델
    private ServiceModel serviceModel;*/

    // 서비스 모델 ID
    private Integer serviceModelId;

    // 학습데이터
    private String contents;

    // 가중치
    private Integer repeatCount;

    // 설명
    private String description;

    // 작성자
    private String uploadedBy;

}
