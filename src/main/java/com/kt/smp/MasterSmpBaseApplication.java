package com.kt.smp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import oracle.jdbc.logging.annotations.DisableTrace;

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
@EnableScheduling
@SpringBootApplication
public class MasterSmpBaseApplication {
	public static void main(String[] args) {
		SpringApplication.run(MasterSmpBaseApplication.class, args);
	}
}
