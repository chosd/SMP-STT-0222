package com.kt.smp.base.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * SMP의 page method를 Master SMP에서 자동으로 연동하기 위한 DTO
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
public class SmpServicePageDto {
	private String name;			// page 이름
	private String[] uris;			// page URI
	private String description;		// page 설명
	private String html;			// page html uri
	private String parent;			// page의 상위 uri
}
