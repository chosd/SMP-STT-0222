package com.kt.smp.stt.statistics.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author jaime
 * @title SttStatisticCallInfo
 * @see\n <pre>
 * </pre>
 * @since 2022-07-06
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SttStatisticsCallInfo {

    private String serviceCode;

    private Integer requestCount;

    private Integer completeCount;

    private Integer failCount;

    private Integer processingCount;
}
