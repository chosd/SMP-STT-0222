package com.kt.smp.base.dto;

import org.springframework.web.bind.annotation.RequestMethod;

import lombok.Getter;
import lombok.Setter;

/**
 * SMP의 api method를 Master SMP에서 자동으로 연동하기 위한 DTO
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
public class SmpServiceApiDto {
		private String name;				// API 이름
		private RequestMethod[] methods;	// API method
		private String[] uris;				// API URI
		private String type;				// API 타입
		private String description;			// API 설명
}
