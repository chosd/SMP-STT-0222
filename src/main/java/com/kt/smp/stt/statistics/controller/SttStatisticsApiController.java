package com.kt.smp.stt.statistics.controller;

import com.github.pagehelper.PageHelper;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.stt.statistics.domain.SttStatisticsResponseDto;
import com.kt.smp.stt.statistics.domain.SttStatisticsSearchCondition;
import com.kt.smp.stt.statistics.dto.SttStatisticsSearchResponseDto;
import com.kt.smp.stt.statistics.scheduler.SttStatisticsScheduler;
import com.kt.smp.stt.statistics.service.SttStatisticsService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/statistics/api")
@Tag(name = "STT 통계", description = "STT 통계 API")
public class SttStatisticsApiController {

    private final static String REG_DT_DESC = "REG_DT DESC";

    private final SttStatisticsService sttStatisticsService;
    
    private final SttStatisticsScheduler sttStatisticsSchedulerV2;

    @SmpServiceApi(name = "STT 통계 목록 검색", method = RequestMethod.GET, path = "/listPage", type = "검색", description = "STT 통계 목록 검색")
    public ResponseEntity<SttStatisticsSearchResponseDto> listPage(
            @ModelAttribute SttStatisticsSearchCondition searchCondition) {

    	PageHelper.startPage(searchCondition.getPage(), searchCondition.getPageSize(), REG_DT_DESC);

        SttStatisticsSearchResponseDto searchResponseDto = sttStatisticsService.getStatisticsList(searchCondition);   	
        
        return ResponseEntity.ok(searchResponseDto);
    }
    
    @SmpServiceApi(name = "STT 통계 차트 검색", method = RequestMethod.GET, path = "/chartData", type = "검색", description = "STT 통계 차트 검색")
    public ResponseEntity<JSONObject> chartData(
            @ModelAttribute SttStatisticsSearchCondition searchCondition) {
    	
        JSONObject chartData = sttStatisticsService.getStatisticsChartData(searchCondition);
        
        return ResponseEntity.ok(chartData);
    }

    // postman으로 테스트하기 위한 컨트롤러(데이터 저장용)
    @SmpServiceApi(name = "STT 통계 목록 검색", method = RequestMethod.POST, path = "/listTest", type = "검색", description = "STT 통계 목록 검색")
    public void listPageTest(@RequestBody SttStatisticsResponseDto searchCondition) throws UnknownHostException {
    	log.info("searchCondition >>> {}",searchCondition);
    	sttStatisticsSchedulerV2.insertStatisticsInfo(searchCondition);
    }
}
