package com.kt.smp.stt.verify.data.controller;

import com.dev.dto.HttpResponse;
import com.dev.func.dataconverter.JsonUtil;
import com.dev.type.ResponseStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.common.util.JacksonUtil;
import com.kt.smp.fileutil.controller.SttResultModelIdApiController;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.comm.directory.dto.DirectoryGroupDeleteDto;
import com.kt.smp.stt.verify.data.dto.VerifyDataDeleteDto;
import com.kt.smp.stt.verify.data.dto.VerifyDataDto;
import com.kt.smp.stt.verify.data.dto.VerifyDataMultipartSaveDto;
import com.kt.smp.stt.verify.data.dto.VerifyDataSearchCondition;
import com.kt.smp.stt.verify.data.service.VerifyDataService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/api/verify")
public class VerifyDataApiController {
	
	private final ConfigService configService;
    private final VerifyDataService dataService;

    @SmpServiceApi(
            name = "데이터 목록 조회",
            method = RequestMethod.GET,
            path = "/data",
            type = "조회",
            description = "데이터 목록 조회")
    public String search(@ModelAttribute VerifyDataSearchCondition searchCondition) throws JsonProcessingException {

        try {

            int count = dataService.count(searchCondition);
            return JsonUtil.toJson(HttpResponse.onSuccess(count, 0 < count ? dataService.search(searchCondition) : null));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "데이터 조회",
            method = RequestMethod.GET,
            path = "/data/{id}",
            type = "조회",
            description = "데이터 조회")
    public String get(@PathVariable("id") int id) throws JsonProcessingException {

        try {

            VerifyDataDto data = dataService.get(id);
            
            return JsonUtil.toJson(HttpResponse.onSuccess(data));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(name = "데이터셋 이름 중복체크",method = RequestMethod.GET,path = "/data/name/duplicate",type = "조회",description = "데이터셋 이름 중복체크")
    public String duplicateNameCheck(@RequestParam("encodedName") String encodedName) throws JsonProcessingException {

        try {

            String name = URLDecoder.decode(encodedName, StandardCharsets.UTF_8);
            if (dataService.isExistName(name)) {
                return JsonUtil.toJson(HttpResponse.onSuccess(com.dev.type.ResponseStatus.CONFLICT));
            }

            return JsonUtil.toJson(HttpResponse.onSuccess(ResponseStatus.ACCEPTED));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }
    
    @SmpServiceApi(name = "상세 디렉토리명 중복체크",method = RequestMethod.GET,path = "/data/path/duplicate",type = "조회",description = "상세 디렉토리명 중복체크")
    public String duplicatePathCheck(@RequestParam("path") String path,@RequestParam("serviceModelId") String serviceCode) throws JsonProcessingException {

        try {

            
            if (dataService.isExistPath(path, serviceCode)) {
                return JsonUtil.toJson(HttpResponse.onSuccess(com.dev.type.ResponseStatus.CONFLICT));
            }

            return JsonUtil.toJson(HttpResponse.onSuccess(ResponseStatus.ACCEPTED));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }
    
    @SmpServiceApi(
            name = "데이터 등록",
            method = RequestMethod.POST,
            path = "/data",
            type = "등록",
            description = "데이터 등록")
    public String save(
            HttpServletRequest request,
            @ModelAttribute VerifyDataMultipartSaveDto newData) throws JsonProcessingException {
    	
        try {
            newData.audit(request);
            dataService.save(newData);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "데이터 삭제",
            method = RequestMethod.POST,
            path = "/data/delete",
            type = "삭제",
            description = "데이터 삭제")
    public String deleteList(@RequestBody VerifyDataDeleteDto target) throws JsonProcessingException {
    	log.info("target >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(target));
        try {
            
        	dataService.delete(target);
            
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "음원파일 재생",
            method = RequestMethod.GET,
            path = "/data/{id}/wav",
            type = "조회",
            description = "음원파일 재생",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<InputStreamResource> getWavFileResource(@PathVariable("id") Integer id) {

        byte[] wavFileBytes = dataService.getWavFileBytes(id);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(wavFileBytes));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(wavFileBytes.length);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
    
    private void setCurrentTenantContext() {
    	List<ConfigDto> configDtos = configService.getAllUserDefined();
    	for (ConfigDto configDto : configDtos) {
    		TenantContextHolder.set(configDto.getProjectCode());	
    	}
    }
}
