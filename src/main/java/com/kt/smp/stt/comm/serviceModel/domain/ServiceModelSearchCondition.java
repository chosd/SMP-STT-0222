package com.kt.smp.stt.comm.serviceModel.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author jieun.chang
 * @title ServiceModelSearchCondition
 * @see\n <pre>
 * </pre>
 * @since 2022-12-15
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceModelSearchCondition {

    private String serviceModelName;
    private String serviceCode;
    private String uploadedBy;
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate from;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate to;

    @Override
    public String toString() {
        return "SttTrainDataSearchCondition{" +
                "serviceModelName=" + serviceModelName + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", uploadedBy='" + uploadedBy + '\'' +
                ", description='" + description + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
