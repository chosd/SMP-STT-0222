/******************************************************************************************
 * 본 프로그램소스는 하나은행의 사전승인 없이 임의복제, 복사, 배포할 수 없음
 *
 * Copyright (C) 2018 by co.,Ltd. All right reserved.
 ******************************************************************************************/
package com.kt.smp.stt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.kt.smp.stt.statistics.domain.SttStatisticsResponseDto;
import com.kt.smp.stt.statistics.scheduler.SttStatisticsScheduler;

@SpringBootTest
@ActiveProfiles("local")
class EngineTest {

	@Autowired SttStatisticsScheduler sttStatisticsScheduler;
	
	@Test
	public void sttStatisticsInfoAll() {
		// 8.2 STT 통계 정보 조회 요청
		// [BaseUrl]/stt/statistics/info/all?date={date}
		System.out.println(">>> test");
		
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String prevDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String projectCode = "test-project-code";
		
		SttStatisticsResponseDto responseDto 
			= sttStatisticsScheduler.callApi(prevDate, projectCode);
		
		System.out.println(">>> responseDto : " + responseDto);
		System.out.println(">>> responseDto : " + responseDto.getServerInfo());
		System.out.println(">>> responseDto : " + responseDto.getServerInfo().get(0).getSupportSvcList());
		System.out.println(">>> responseDto : " + responseDto.getServerInfo().get(0).getCallInfo());
	}
}
