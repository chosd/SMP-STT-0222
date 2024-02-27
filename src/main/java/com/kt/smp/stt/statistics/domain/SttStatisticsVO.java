package com.kt.smp.stt.statistics.domain;

import com.kt.smp.common.domain.BaseModel;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jaime
 * @title SttStatisticVO
 * @see\n <pre>
 * </pre>
 * @since 2022-07-04
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SttStatisticsVO extends BaseModel {

    // 통계정보 응답 시간 (분단위)
    private String resDateTime;
    
    // SCU에서 제공하는 SVC 목록
    private List<String> supportSvcList = new ArrayList<>();
    
    // 통계 정보 조회를 성공한 SVC 목록
    private List<String> completeSvcList = new ArrayList<>();
    
    // 서비스 코드명
    private String serviceCode;
    
    // STT 요청 건수
    private Integer requestCount;
    
    // STT 완료 건수
    private Integer completeCount;

    // STT 실패 건수
    private Integer failCount;

    // STT 진행 건수
    private Integer busyCallCount;
    
    // 서버명
    private String serverName;
    
    // C-POD 전체 개수
    private Integer totalServerCnt;
    
    // STT 통계 정보 조회 성공한 C-POD개수
    private Integer completeServerCnt;
}
