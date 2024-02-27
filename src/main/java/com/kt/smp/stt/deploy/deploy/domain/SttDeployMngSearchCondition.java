package com.kt.smp.stt.deploy.deploy.domain;

import com.kt.smp.common.dto.PageParam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @title STT 배포 모델 등록 DTO
 * @since 2022.02.18
 * @author kyungtae
 * @see <pre></pre>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SttDeployMngSearchCondition extends PageParam {

    private Long serviceModelId;

    private String createdBy;

    private String description;

    private String status;

    private String from;
    private String to;
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDate from;
//
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDate to;

    @Override
    public String toString() {
        return "SttDeployMngSearchCondition{" +
                "serviceModelId=" + serviceModelId +
                ", createdBy='" + createdBy + '\'' +
                ", description='" + description + '\'' +
                ", deployStatus='" + status + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
