package com.kt.smp.stt.comm.serviceModel.domain;

import com.kt.smp.common.domain.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @author jieun.chang
 * @title ServiceModelVO
 * @see\n <pre>
 * </pre>
 * @since 2022-12-15
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceModelVO extends BaseModel {

    // 서비스모델명
    private String serviceModelName;

    // 서비스코드
    private String serviceCode;

    // 설명
    private String description;
    
    // 등록일
    private String regDt;
    
    // 등록자
    private String regId;
    
    // 등록자 IP
    private String regIp;
    
    // 수정일
    private String updDt;
    
    // 수정자
    private String updId;
    
    // 수정자 IP
    private String updIp;
    
    // 작성자
    private String uploadedBy;

}
