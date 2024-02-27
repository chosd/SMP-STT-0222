package com.kt.smp.stt.log.controller;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dev.dto.HttpResponse;
import com.dev.func.dataconverter.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.stt.log.dto.BulkDictationSaveDto;
import com.kt.smp.stt.log.dto.LogDeleteDto;
import com.kt.smp.stt.log.dto.LogDetailDto;
import com.kt.smp.stt.log.dto.LogDto;
import com.kt.smp.stt.log.dto.LogSaveDto;
import com.kt.smp.stt.log.dto.LogSearchCondition;
import com.kt.smp.stt.log.dto.TrainDataSaveDto;
import com.kt.smp.stt.log.service.LogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/api")
public class LogApiController {

    private final LogService logService;

    @SmpServiceApi(
            name = "STT결과 목록 조회",
            method = RequestMethod.GET,
            path = "/log",
            type = "조회",
            description = "STT결과 목록 조회")
    public String search(@ModelAttribute LogSearchCondition searchCondition) throws JsonProcessingException {

        try {

            int count = logService.count(searchCondition);
            return JsonUtil.toJson(HttpResponse.onSuccess(count, 0 < count ? logService.search(searchCondition) : null));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "STT결과 조회",
            method = RequestMethod.GET,
            path = "/log/{callKey}",
            type = "조회",
            description = "STT결과 조회")
    public String get(@PathVariable("callKey") String callKey) throws JsonProcessingException {

        try {

            LogDetailDto log = logService.getByCallKey(callKey);
            return JsonUtil.toJson(HttpResponse.onSuccess(log));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "STT결과 등록",
            method = RequestMethod.POST,
            path = "/log",
            type = "등록",
            description = "STT결과 등록")
    public String save(@RequestBody LogSaveDto target) throws JsonProcessingException {
    	log.info(">>> insert log : " + target.toString());
        try {
            logService.save(target);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "STT결과 삭제",
            method = RequestMethod.POST,
            path = "/log/delete",
            type = "삭제",
            description = "STT결과 삭제")
    public String deleteList(@RequestBody LogDeleteDto target) throws JsonProcessingException {

        try {
            logService.delete(target);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "음원파일 재생",
            method = RequestMethod.GET,
            path = "/log/{id}/wav",
            type = "조회",
            description = "음원파일 재생",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<InputStreamResource> getWavFileResource(@PathVariable("id") Integer id) {

    	try {
    		byte[] wavFileBytes = logService.getWavFileBytes(id);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(wavFileBytes));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(wavFileBytes.length);
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    	} catch(Exception e) {
    		log.error("getWavFileResource ::: " + e.getMessage());
    		return new ResponseEntity<>(null, null, HttpStatus.OK);
    	}
    }

    @SmpServiceApi(
            name = "LM학습데이터 등록",
            method = RequestMethod.POST,
            path = "/log/trainData/save",
            type = "등록",
            description = "LM학습데이터 등록")
    public String saveTrainData(
            HttpServletRequest request,
            @RequestBody TrainDataSaveDto newTrainData) throws JsonProcessingException {

        try {
            newTrainData.audit(request);
            logService.saveTrainData(newTrainData);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "전사데이터 등록",
            method = RequestMethod.POST,
            path = "/log/{logId}/dictation/save",
            type = "등록",
            description = "전사데이터 등록")
    public String saveDictation(
            HttpServletRequest request,
            @PathVariable("logId") Integer logId) throws JsonProcessingException {

        try {
            LogDto log = logService.get(logId);
            if (log == null) {
                throw new IllegalArgumentException("등록되지 않은 STT결과 입니다");
            }

            logService.saveDictation(request, log);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "전사데이터 대량 등록",
            method = RequestMethod.POST,
            path = "/log/dictation/save",
            type = "등록",
            description = "전사데이터 대량 등록")
    public String saveBulkDictation(
            HttpServletRequest request, @RequestBody BulkDictationSaveDto bulkDictationSave) throws JsonProcessingException {

        try {

            bulkDictationSave.audit(request);
            logService.saveDictation(request, bulkDictationSave);

            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }
}
