package com.kt.smp.base.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * SMP의 page method를 Master SMP에서 자동으로 연동할 수 있도록 하기 위한 Annotation
 * html을 제공하기 위한 request mapping은 별도 생성 후 정보만 입력한다.
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
@RequestMapping
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface SmpServiceWidget {
	// 기본 RequestMapping annotation 내용
	String name();
	@AliasFor("path") String[] value() default {};
	@AliasFor("value") String[] path() default {};
	RequestMethod[] method() default {};
	String[] params() default {};
	String[] headers() default {};
	String[] consumes() default {};
	String[] produces() default {};

	String image() default "";				// 대표 이미지
	String description() default "";		// 설명글
	
}