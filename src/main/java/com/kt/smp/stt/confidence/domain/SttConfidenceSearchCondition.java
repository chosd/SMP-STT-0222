package com.kt.smp.stt.confidence.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kt.smp.common.dto.PageParam;
import lombok.*;

/**
 * @author jaime
 * @title SttStatisticSearchCondition
 * @see\n <pre>
 * </pre>
 * @since 2022-07-04
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SttConfidenceSearchCondition extends PageParam {

    private Long serviceModelId;

    private String serviceCode;

    private SttConfidenceSearchUnit searchUnit = SttConfidenceSearchUnit.MINUTE;

    private String from;

    private String to;
}
