package com.kt.smp.multitenancy.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dev.dto.HttpResponse;
import com.dev.func.dataconverter.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.dto.ConfigSaveDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/api")
public class ConfigApiController {

    private final ConfigService configService;
    private final PreferenceValueHolder preferenceValueHolder;
    
    @Value("${spring.datasource.tenant.auto-ddl}")
    private boolean autoDdl;

    @SmpServiceApi(
            name = "서비스 설정 조회",
            method = RequestMethod.GET,
            path = "/config/{projectCode}",
            type = "조회",
            description = "서비스 설정 조회")
    public String get(@PathVariable("projectCode") String projectCode) throws JsonProcessingException {

        try {

            ConfigDto config = configService.getByProjectCode(projectCode);
            return JsonUtil.toJson(HttpResponse.onSuccess(config));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "서비스 설정 등록",
            method = RequestMethod.POST,
            path = "/config",
            type = "등록",
            description = "서비스 설정 등록")
    public String save(HttpServletRequest request, @RequestBody ConfigSaveDto newConfig) throws JsonProcessingException {
    	log.info(">>> newConfig : " + newConfig);
        try {
            newConfig.audit(request);
            configService.save(newConfig);
            if (autoDdl) {
            	preferenceValueHolder.init();
            }
            
            return JsonUtil.toJson(HttpResponse.onSuccess(true));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "서비스 설정 삭제",
            method = RequestMethod.POST,
            path = "/config/{projectCode}/delete",
            type = "삭제",
            description = "서비스 설정 삭제")
    public String save(@PathVariable("projectCode") String projectCode) throws JsonProcessingException {

        try {
            configService.delete(projectCode);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }
}
