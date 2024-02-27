package com.kt.smp.stt.operate.session.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wonyoung.ahn
 * @title SttSessionResponseDto
 * @see\n <pre>
 * </pre>
 * @since 2023-09-13
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponseDto {

    private String resultCode;

    private String resultMsg;

    private String reqDate;

    private String resDateTime;
    
    private Integer totalServerCnt;
    
    private Integer completeServerCnt;

    private List<ServerInfoDto> serverInfo = new ArrayList<>();
    
    private String delay;
}
