package com.kt.smp.stt.statistics.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SttStatisticsServerInfoVO {

	// C-POD 이름
    private String serverName;
    // C-POD 간 통신 연결 상태
    private Boolean connectionStatus;
    // C-POD 에서 제공하는  svc목혹
    private List<String> supportSvcList = new ArrayList<>();
    //Call에 대한 통계 정보
    private List<SttStatisticsCallInfo> callInfo = new ArrayList<>();
}
