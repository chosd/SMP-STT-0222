/******************************************************************************************
 * 본 프로그램소스는 하나은행의 사전승인 없이 임의복제, 복사, 배포할 수 없음
 *
 * Copyright (C) 2018 by co.,Ltd. All right reserved.
 ******************************************************************************************/
package com.kt.smp.stt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.kt.smp.common.util.EncUtil;

import lombok.extern.slf4j.Slf4j;

/*@SpringBootTest
@ActiveProfiles("local")*/
@Slf4j
class EncTest {

	@Test
	public void encTest() {
		String src = "Rapeech123";
		System.out.println(">>> src : " + src);
		String key = "4F8ABBD4EE68E655F42146E87D6E4022";
		
		String enc = EncUtil.encAES128(src, key);
		System.out.println(">>> enc : " + enc);
		
		String dec;
		try {
			dec = EncUtil.decAES128(enc, key);
			System.out.println(">>> dec : " + dec);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void encdecTest() {
		EncUtil util = new EncUtil();
		String key = "4F8ABBD4EE68E655F42146E87D6E4022";
		String stm123 = "stm123";
		String stmadm123 = "stmadm123";
		String WEBADMIN123 = "WEBADMIN123";
		String stm123EncResult = util.encAES128(stm123, key);
		String stmadm123EncResult = util.encAES128(stmadm123, key);
		String WEBADMIN123EncResult = util.encAES128(WEBADMIN123, key);
		String qwe = "123qwe";
		String qweEncResult = util.encAES128(qwe, key);
		String dptmxldpa123zhs = "dptmxldpa123zhs";
		String dptmxldpa123zhResult = util.encAES128(dptmxldpa123zhs, key);
		String rapeech123 = "Rapeech123";
		String rapeech123Result = util.encAES128(rapeech123, key);
		String rapeech12 = "Rapeech12!";
		String rapeech12Result = util.encAES128(rapeech12, key);
		
		log.info(">>>> rapeech12Result : "+rapeech12Result);
		log.info(">>>> rapeech123Result : "+rapeech123Result);
		log.info(">>>>> dptmxldpa123zhResult : "+dptmxldpa123zhResult);
		log.info(" >>>  qweEncResult : "+qweEncResult);
		log.info(">>> stm123EncResult : "+stm123EncResult);
		log.info(">> stmadm123EncResult : "+stmadm123EncResult);
		log.info(">>> WEBADMIN123EncResult : "+WEBADMIN123EncResult);
	}
	
}
