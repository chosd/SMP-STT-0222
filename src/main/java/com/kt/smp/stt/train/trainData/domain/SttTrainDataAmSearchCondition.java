package com.kt.smp.stt.train.trainData.domain;

import com.kt.smp.stt.common.ModelType;
import com.kt.smp.stt.common.ServiceModel;
import com.kt.smp.stt.common.TrainDataType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SttTrainDataAmSearchCondition {

    private ModelType modelType;

    private Integer serviceModelId;

    private String datasetName;

    private String uploadedBy;

    private String description;

    private String from;
    
    private String to;
    
    @Override
    public String toString() {
        return "SttTrainDataAmSearchCondition {" +
                "modelType=" + modelType +
                ", serviceModelId=" + serviceModelId +
                ", datasetName='" + datasetName + '\'' +
                ", uploadedBy='" + uploadedBy + '\'' +
                ", description='" + description + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
        
    }
}
