package com.kt.smp;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Spring Boot Entry Point
 * @author AICC 기술개발 지관욱
 * @since 2022.10.12
 * @version 1.0
 * 
 * 참고 web :: https://spring.io/projects/spring-boot
 * 
 * <<개정 이력>>
 * 수정일 / 수정자 / 수정내용
 * 2022.10.12 / 지관욱 / 초기 생성
 */
public class ServletInitializer extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MasterSmpBaseApplication.class);
	}

}
