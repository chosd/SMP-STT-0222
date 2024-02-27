package com.kt.smp.stt.deploy.model.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jaime
 * @title DeployMngModel
 * @see\n <pre>
 * </pre>
 * @since 2022-03-03
 */
@Setter
@Getter
public class DeployMngModel {

    // 결과모델 ID와 결과모델 설명 매핑
    private List<Map<String, String>> resultModelId2Descriptions = new ArrayList<>();

    // 서비스모델 코드과 결과모델 ID를 매핑
    private Map<String, List<String>> serviceCode2ResultModelIds = new HashMap<>();
    
    // 결과모델ID와 모델타입 맵핑
    private List<Map<String, String>> resultModelId2ModelTypes = new ArrayList<>();
    
    
}
