package com.kt.smp.base.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Master SMP에서 자동으로 연동하기 위한 서비스 정보 Dto
 * @author AICC 기술개발 지관욱
 * @since 2022.10.12
 * @version 1.0
 *
 * 참고 api :: "[POST] /" 요청 시 서비스 정보 제공에 사용됨
 *
 * <<개정 이력>>
 * 수정일 / 수정자 / 수정내용
 * 2022.10.12 / 지관욱 / 초기 생성
 */
@Setter
@Getter
public class SmpServiceDto {
    private String name;
    private String version;
    private String description;
    private String prefix;
    private Date lastUpdatedAt;
    private List<SmpServiceApiDto> apis;
    private List<SmpServicePageDto> pages;
    private List<SmpServiceWidgetDto> widgets;
}
