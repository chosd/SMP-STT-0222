package com.kt.smp.stt.verify.dataset.controller;

import com.dev.dto.HttpResponse;
import com.dev.func.dataconverter.JsonUtil;
import com.dev.type.ResponseStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.stt.comm.directory.dto.*;
import com.kt.smp.stt.comm.directory.service.DirectoryService;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.verify.dataset.dto.*;
import com.kt.smp.stt.verify.dataset.service.VerifyDatasetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/api/verify")
public class VerifyDatasetApiController {

    private final VerifyDatasetService datasetService;
    private final ServiceModelService serviceModelService;
    private final DirectoryService directoryService;

    @SmpServiceApi(
            name = "데이터셋 목록 조회",
            method = RequestMethod.GET,
            path = "/dataset",
            type = "조회",
            description = "데이터셋 그룹 목록 조회")
    public String search(@ModelAttribute VerifyDatasetSearchCondition searchCondition) throws JsonProcessingException {

        try {

            int count = datasetService.count(searchCondition);
            return JsonUtil.toJson(HttpResponse.onSuccess(count, 0 < count ? datasetService.search(searchCondition) : null));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "데이터셋 조회",
            method = RequestMethod.GET,
            path = "/dataset/{id}",
            type = "조회",
            description = "데이터셋 조회")
    public String get(@PathVariable("id") int id) throws JsonProcessingException {

        try {

            VerifyDatasetDto dataset = datasetService.get(id);
            return JsonUtil.toJson(HttpResponse.onSuccess(dataset));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "데이터셋 이름 중복체크",
            method = RequestMethod.GET,
            path = "/dataset/name/duplicate",
            type = "조회",
            description = "데이터셋 이름 중복체크")
    public String duplicateNameCheck(@RequestParam("encodedName") String encodedName) throws JsonProcessingException {

        try {

            String name = URLDecoder.decode(encodedName, StandardCharsets.UTF_8);
            if (datasetService.isExistName(name)) {
                return JsonUtil.toJson(HttpResponse.onSuccess(com.dev.type.ResponseStatus.CONFLICT));
            }

            return JsonUtil.toJson(HttpResponse.onSuccess(ResponseStatus.ACCEPTED));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "데이터셋 디렉토리 중복체크",
            method = RequestMethod.GET,
            path = "/dataset/directory/duplicate",
            type = "조회",
            description = "데이터셋 디렉토리 중복체크")
    public String duplicateDirectoryCheck(@RequestParam("directoryId") Integer directoryId) throws JsonProcessingException {

        try {

            if (datasetService.isExistDirectory(directoryId)) {
                return JsonUtil.toJson(HttpResponse.onSuccess(com.dev.type.ResponseStatus.CONFLICT));
            }

            return JsonUtil.toJson(HttpResponse.onSuccess(ResponseStatus.ACCEPTED));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "데이터셋 등록",
            method = RequestMethod.POST,
            path = "/dataset",
            type = "등록",
            description = "데이터셋 그룹 등록")
    public String save(
            HttpServletRequest request,
            @RequestBody VerifyDatasetSaveDto newDataset) throws JsonProcessingException {

        try {
        	// 서비스모델의 시퀀스ID값이 아닌 실사용 서비스코드값을 가져오기 위함
//        	String serviceModelCode = serviceModelService.detail(newDataset.getServiceModelId()).getServiceCode();
        	String serviceModelCode = newDataset.getServiceModelId() + "";
        	newDataset.setServiceModelId(Integer.parseInt(serviceModelCode));
        	log.info(">>>>>> newDataset.getName() : "+newDataset.getName()+" / newDataset.getServiceModelId :"+newDataset.getServiceModelId());
            newDataset.audit(request);
            datasetService.save(newDataset);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "데이터셋 수정",
            method = RequestMethod.POST,
            path = "/dataset/{id}/update",
            type = "수정",
            description = "데이터셋 수정")
    public String update(
            HttpServletRequest request,
            @PathVariable("id") int id,
            @RequestBody VerifyDatasetUpdateDto modifiedDataset) throws JsonProcessingException {

        try {
            modifiedDataset.audit(request);
            datasetService.update(id, modifiedDataset);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "데이터셋 삭제",
            method = RequestMethod.POST,
            path = "/dataset/delete",
            type = "삭제",
            description = "데이터셋 그룹 삭제")
    public String deleteList(@RequestBody VerifyDatasetDeleteDto target) throws JsonProcessingException {

        try {
            datasetService.delete(target);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "서비스모델 목록 조회",
            method = RequestMethod.GET,
            path = "/dataset/serviceModel",
            type = "조회",
            description = "데이터셋 서비스모델 목록 조회")
    public String getServiceModelList() throws JsonProcessingException {

        try {
            List<ServiceModelVO> serviceModelList = serviceModelService.listAll();
            return JsonUtil.toJson(HttpResponse.onSuccess(serviceModelList));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리 목록 조회",
            method = RequestMethod.GET,
            path = "/dataset/directory",
            type = "조회",
            description = "디렉토리 목록 조회")
    public String getDirectoryList() throws JsonProcessingException {

        try {
            List<DirectoryListDto> directoryList = directoryService.getAll();
            return JsonUtil.toJson(HttpResponse.onSuccess(directoryList));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "데이터셋 다운로드",
            method = RequestMethod.GET,
            path = "/dataset/{id}/download",
            type = "다운로드",
            description = "데이터셋 다운로드")
    public void download(@PathVariable("id") Integer id, HttpServletResponse response) {

        Path zipFilePath = null;
        try {

            zipFilePath = datasetService.getAnswerSheetAndWavFile(id);

            try(FileInputStream inputStream = new FileInputStream(zipFilePath.toFile())) {
                setResponseHeader(response);
                ServletOutputStream outputStream = response.getOutputStream();
                StreamUtils.copy(inputStream, outputStream);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            log.error("[ERROR] download dataset fail : {}", ex.getMessage());
        } catch (InvalidPathException e) {
        	//windows 환경에서 실행할 경우 파일명 에러
        	log.error(e.getMessage() + ">>>>>> [windows] 파일명에 ':'를 포함할 수 없습니다.");
        } finally {
            deleteZipFile(zipFilePath);
        }
    }

    private void setResponseHeader(HttpServletResponse response) throws UnsupportedEncodingException {
        String filename = URLEncoder.encode("검증데이터.zip", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\";");
        response.setContentType("application/zip");
    }

    private void deleteZipFile(Path zipFilePath) {

        try {
            if (zipFilePath != null && zipFilePath.toFile().exists()) {
                Files.delete(zipFilePath);
            }
        } catch (IOException e) {
        	log.error(e.getMessage());
        }

    }
}
