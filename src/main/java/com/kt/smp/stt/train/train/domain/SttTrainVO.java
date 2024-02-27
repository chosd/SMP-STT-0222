package com.kt.smp.stt.train.train.domain;

import com.kt.smp.common.domain.BaseModel;
import com.kt.smp.stt.common.ServiceModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author jaime
 * @title SttTrainVO
 * @see\n <pre>
 * </pre>
 * @since 2022-04-05
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SttTrainVO extends BaseModel {

    // 서비스 모델
    private Long serviceModelId;

    // 설명
    private String description;

    // 작성자
    private String updatedBy;

    // 요청 시간
//    private LocalDateTime requestedAt;
    private String requestedAt;

    // 종료 시간
//    private LocalDateTime completedAt;
    private String completedAt;

    // 소요시간
    private Long duration;

    // 학습데이터 수
    private Long dataNum;
    
    // 음성학습데이터 총 걸린 시간
    private String dataTime;

    // 결과모델 ID
    private String resultModelId;

    // STT API_연동규격서 결과코드 3.3 정의에 따름
    private String resultCode;

    /**
     * 학습 결과
     * 성공: "success"
     * 실패: 학습 실패 원인 메시지
     */
    private String resultMsg;

    /**
     * 학습 상태
     * ready: 미학습 상태
     * training: 학습 중
     * complete: 학습 완료
     * fail: 학습 실패
     */
    private String status;

    /**
     * 학습 요청한 lmType
     * CLASS: class LM
     * SERVICE: service LM
     * E2ESL : 지도학습(Fine Tunning)
     * E2EUSL : 비지도학습
     * E2ELM : 텍스트학습(biasing LM)
     * E2EMSL : 반지도 학습(비지도+지도)
     */
    private String modelType;  // 2023.09.15 lmType -> modelType 변경

    // 학습된 모델 저장경로 (NAS 경로)
    private String modelPath;

    // 학습 모델의 무결성 검증을 위한 md5 hash checksum (32자리)
    private String modelAuthKey;

    // 학습 데이터 저장경로
    private String trainDataPath;
    
    // 학습 음성데이터 저장경로 , 2023.09.15 추가
    // 23.10.19 API변경으로 제거
    //private String trainWavPath;

    /**
     * [E2ESL,E2EMSL]타입의 경우 사용하는 정답지 파일경로
     */
    private List<String> trainE2ESLDataPathList; // 23.10.19 API변경으로 추가
    
    /**
     * [E2ESL,E2EMSL]타입의 경우 사용하는 지도 학습음성 파일경로
     */
    private List<String> trainE2ESLWavPathList; // 23.10.19 API변경으로 추가
    
    /**
     * [E2EUSL,E2EMSL]타입의 경우 사용하는 비지도 학습음성 파일경로
     * 위 경우를 종합해보면
     * E2ESL : trainE2ESLDataPathList,trainE2ESLWavPathList 사용 
     * E2EMSL : trainE2ESLDataPathList,trainE2ESLWavPathList,trainE2EUSLWavPathList 사용
     * E2EUSL : trainE2EUSLWavPathList 사용
     */
    private List<String> trainE2EUSLWavPathList; // 23.10.19 API변경으로 추가
    
    // 학습등록시 선택한 데이터셋 목록 저장하기 위함
    private List<String> trainAmDatasetList;
    
    @Override
    public String toString() {
        return "SttTrainVO{" +
                "serviceModelId=" + serviceModelId +
                ", description='" + description + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", requestedAt=" + requestedAt +
                ", completedAt=" + completedAt +
                ", duration=" + duration +
                ", dataNum=" + dataNum +
                ", resultModelId='" + resultModelId + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                ", status='" + status + '\'' +
                ", modelType='" + modelType + '\'' +
                ", modelPath='" + modelPath + '\'' +
                ", modelAuthKey='" + modelAuthKey + '\'' +
                ", trainDataPath='" + trainDataPath + '\'' +
                '}';
    }
}
