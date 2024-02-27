package com.kt.smp.stt.train.train.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author jaime
 * @title SttTrainRequestDto
 * @see\n <pre>
 * </pre>
 * @since 2022-04-05
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SttTrainRequestDto {

    // 학습데이터 저장경로(nas)
    private String trainDataPath;
    
    // 학습 음성데이터 저장경로, 23.09.15 API변경으로 추가
    // 23.10.19 API변경으로 제거
    //private String trainWavPath;

    /// serviceCode명
    private String serviceCode;

    // 학습 완료시 callback할 URL
    private String callbackUrl;

    /**
     * 학습할 lmType
     * CLASS: class LM
     * SERVICE: service LM
     * E2ESL : 지도학습(Fine Tunning)
     * E2EUSL : 비지도학습
     * E2ELM : 텍스트학습(biasing LM)
     * E2EMSL : 반지도 학습(비지도+지도)
     */
    private String modelType;  // 23.09.15 API변경으로 lmType-> modelType으로 변경
    
    
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
    
}
