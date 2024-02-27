package com.kt.smp.base.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * SMP의 widget method를 Master SMP에서 자동으로 연동하기 위한 DTO
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
public class SmpServiceWidgetDto {
	private String name;				// widget 이름
	private RequestMethod[] methods;	// widget method
	private String[] uris;				// widget URI
	private String html;				// widget html uri
	private String description;			// widget 설명

	
	// TODO 2022.10.12 :: widget 정보 제공 연동을 위한 내용 추가 정의 필요
	private String imagePath;			// widget 설명 대표 이미지
}
