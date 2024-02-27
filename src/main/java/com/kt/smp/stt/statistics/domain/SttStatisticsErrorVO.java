package com.kt.smp.stt.statistics.domain;

import com.kt.smp.common.domain.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author jaime
 * @title SttStatisticsErrorVO
 * @see\n <pre>
 * </pre>
 * @since 2022-07-18
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SttStatisticsErrorVO extends BaseModel {

    // 통계정보 응답 시간 (분단위)
    private String resDateTime;

    // 서비스 코드명
    private String serviceCode;

    // 서버명
    private String serverName;
}
