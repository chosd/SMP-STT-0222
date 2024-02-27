/**
 * 
 */
package com.kt.smp.stt.recognition.controller;

import com.kt.smp.stt.confidence.dto.*;
import com.kt.smp.stt.recognition.dto.SttRecognitionSearchCondition;
import com.kt.smp.stt.recognition.service.SttRecognitionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.common.dto.BaseResponseDto;
import com.kt.smp.stt.common.component.SttCmsResultStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* @FileName : SttRecognitionApiController.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 25.
* @작성자 : rapeech
* @변경이력 :
* @프로그램설명 :
*/
@Slf4j
@RestController
@RequestMapping("${smp.service.uri.prefix}/recognition/api")
@RequiredArgsConstructor
public class SttRecognitionApiController {

    private final SttRecognitionService sttRecognitionService;

    /**
    *@MethodName : listPage
    *@작성일 : 2023. 9. 20.
    *@작성자 : homin.lee
    *@변경이력 :
    *@Method설명 :
    *@param searchCondition
    *@return
    */
    @SmpServiceApi(name = "STT 인식률 추이(평균) 차트 검색", method = RequestMethod.GET, path = "/listPage", type = "검색", description = "STT 인식률 추이 검색")
    public ResponseEntity<BaseResponseDto<SttRecognitionSearchResponseDto>> listPage(
            @ModelAttribute SttRecognitionSearchCondition searchCondition) {

        BaseResponseDto<SttRecognitionSearchResponseDto> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);

        SttRecognitionSearchResponseDto recognitionChartData = sttRecognitionService.recognitionChartData(searchCondition);
        //log.info(">>>> size : "+recognitionChartData.getChartList().toString());
        responseDto.setResult(recognitionChartData);
        return ResponseEntity.ok(responseDto);
    }
}
