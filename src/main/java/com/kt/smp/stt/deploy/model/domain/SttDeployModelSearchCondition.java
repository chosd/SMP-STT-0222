package com.kt.smp.stt.deploy.model.domain;

import com.kt.smp.common.dto.PageParam;
import com.kt.smp.stt.common.ServiceModel;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author jaime
 * @title SttDeployRequestDto
 * @see\n <pre>
 * </pre>
 * @since 2022-02-21
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SttDeployModelSearchCondition extends PageParam {

    private Long serviceModelId;

    private String uploadedBy;

    private String description;

    private String from;
    private String to;
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDate from;
//
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDate to;

    @Override
    public String toString() {
        return "SttDeployUploadSearchCondition{" +
                "serviceModelId=" + serviceModelId +
                ", uploadedBy='" + uploadedBy + '\'' +
                ", description='" + description + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
