package com.kt.smp.stt.deploy.model.domain;

import com.kt.smp.common.domain.BaseModel;
import com.kt.smp.stt.common.ServiceModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author jaime
 * @title SttUploadVO
 * @see\n <pre>
 * </pre>
 * @since 2022-02-21
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SttDeployModelVO extends BaseModel {

    // 서비스 모델
    private Long serviceModelId;

    // 배포 모델 파일 경로
    private String modelPath;

    // md5 checksum
    private String modelAuthKey;

    // 설명
    private String description;

    // 작성자
    private String uploadedBy;

    // 등록일시
//    private LocalDateTime uploadedDate;
    private String uploadedDate;

    // 학습데이터 수
    private Long dataNum;
    
    // 음성학습데이터 총 걸린 시간
    private String dataTime;
    
    // 학습모델타입
    private String modelType;

    // 결과모델 ID
    private String resultModelId;

    // 모델 파일명
    private String modelFileName;

    @Override
    public String toString() {
        return "SttDeployModelVO{" +
                "serviceModelId=" + serviceModelId +
                ", modelPath='" + modelPath + '\'' +
                ", modelAuthKey='" + modelAuthKey + '\'' +
                ", description='" + description + '\'' +
                ", uploadedBy='" + uploadedBy + '\'' +
                ", uploadedDate=" + uploadedDate +
                ", dataNum=" + dataNum +
                ", dataTime=" + dataTime +
                ", modelType=" + modelType +
                ", resultModelId='" + resultModelId + '\'' +
                ", modelFileName='" + modelFileName + '\'' +
                '}';
    }
}
