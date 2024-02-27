package com.kt.smp.stt.train.train.domain;

import com.kt.smp.common.dto.PageParam;
import com.kt.smp.stt.common.ServiceModel;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author jaime
 * @title SttTrainSearchCondition
 * @see\n <pre>
 * </pre>
 * @since 2022-04-05
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SttTrainSearchCondition extends PageParam {

    private Long serviceModelId;

    private String updatedBy;

    private String status;

    private String from;
    
    private String to;

    private String description;
    
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDate from;
//
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDate to;

    @Override
    public String toString() {
        return "SttTrainSearchCondition{" +
                "serviceModelId=" + serviceModelId +
                ", updatedBy='" + updatedBy + '\'' +
                ", status='" + status + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
