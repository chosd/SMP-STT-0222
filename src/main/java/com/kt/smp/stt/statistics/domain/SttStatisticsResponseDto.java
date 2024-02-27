package com.kt.smp.stt.statistics.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jaime
 * @title SttStatisticResponseDto
 * @see\n <pre>
 * </pre>
 * @since 2022-07-06
 */
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SttStatisticsResponseDto {

    private String resultCode;

    private String resultMsg;

    private String reqDate;

    private String resDateTime;
    
    private Integer totalServerCnt;
    
    private Integer completeServerCnt;

    private List<SttStatisticsServerInfoVO> serverInfo = new ArrayList<>();
}
