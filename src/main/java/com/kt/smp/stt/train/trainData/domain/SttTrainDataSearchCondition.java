package com.kt.smp.stt.train.trainData.domain;

import com.kt.smp.stt.common.ServiceModel;
import com.kt.smp.stt.common.TrainDataType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author jieun.chang
 * @title SttTrainDataSearchCondition
 * @see\n <pre>
 * </pre>
 * @since 2022-03-23
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SttTrainDataSearchCondition {

    private TrainDataType dataType;

    private String serviceModelId;

    private String contents;

    private String uploadedBy;

    private String description;

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDate from;
    
    private String from;
    
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDate to;

    private String to;
    
    @Override
    public String toString() {
        return "SttTrainDataSearchCondition{" +
                "dataType=" + dataType +
                ", serviceModelId=" + serviceModelId +
                ", contents='" + contents + '\'' +
                ", uploadedBy='" + uploadedBy + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
