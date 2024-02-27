package com.kt.smp.stt.statistics.domain;

import lombok.*;

/**
 * @author jaime
 * @title SttStatisticsDetailSearchCondition
 * @see\n <pre>
 * </pre>
 * @since 2022-07-20
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SttStatisticsDetailSearchConditionDto {

    private String serviceCode;

    private String from;

    private String to;

    private String resDateTime;

    private String regDt;

    private String serverName;
}
