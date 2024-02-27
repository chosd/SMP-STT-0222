package com.kt.smp.fileutil.controller;

import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.fileutil.service.TrainDataExcelDownloadService;
import com.kt.smp.fileutil.service.TrainDataSampleDownloadService;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataAmSearchCondition;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataSearchCondition;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author jieun.chang
 * @title SttExcelDownloadController
 * @see\n <pre>
 * </pre>
 * @since 2022-12-15
 */
@Tag(name = "파일다운로드", description = "엑셀 다운로드")
@Slf4j
@RestController
@RequestMapping("${smp.service.uri.prefix}/file/excel")
@RequiredArgsConstructor
public class SttExcelDownloadController {

    private final TrainDataExcelDownloadService trainDataExcelDownloadService;

    private final TrainDataSampleDownloadService trainDataSampleDownloadService;

    @SmpServiceApi(name = "LM학습데이터 엑셀 다운로드", method = RequestMethod.GET, path = "/trainData", type = "다운로드", description = "학습데이터 엑셀 다운로드")
    public void downloadBoilerplateList(@ModelAttribute SttTrainDataSearchCondition searchCondition,
                                        HttpServletResponse response) throws UnsupportedEncodingException {

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        String fileNameUtf8 = URLEncoder.encode(trainDataExcelDownloadService.getFileName(null), "UTF-8");
        fileNameUtf8 = fileNameUtf8.replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileNameUtf8 + ".xlsx");

        try (OutputStream os = response.getOutputStream()) {
            List<String> headers = trainDataExcelDownloadService.getHeaders();
            trainDataExcelDownloadService.createWorksheet(os, searchCondition, headers);

        } catch (IOException e) {
            log.error("[ERROR] download train data List : {}", e.getMessage());
        }
    }
    
    @SmpServiceApi(name = "AM학습데이터 엑셀 다운로드", method = RequestMethod.GET, path = "/amTrainData", type = "다운로드", description = "AM학습데이터 엑셀 다운로드")
    public void amDownloadBoilerplateList(@ModelAttribute SttTrainDataAmSearchCondition searchCondition, HttpServletResponse response) throws UnsupportedEncodingException {

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        String fileNameUtf8 = URLEncoder.encode(trainDataExcelDownloadService.getAmFileName(null), "UTF-8");
        fileNameUtf8 = fileNameUtf8.replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileNameUtf8 + ".xlsx");

        try (OutputStream os = response.getOutputStream()) {
            List<String> headers = trainDataExcelDownloadService.getAmHeaders();
            trainDataExcelDownloadService.amCreateWorksheet(os, searchCondition, headers);

        } catch (IOException e) {
            log.error("[ERROR] download train data List : {}", e.getMessage());
        }
    }

    @SmpServiceApi(name = "학습데이터 엑셀 샘플 다운로드", method = RequestMethod.GET, path = "/trainData/sample", type = "다운로드", description = "학습데이터 엑셀 샘플 다운로드")
    public void downloadTrainDataBulkSample(HttpServletResponse response) throws UnsupportedEncodingException {

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        String fileNameUtf8 = URLEncoder.encode(trainDataSampleDownloadService.getFileName(null), "UTF-8");
        fileNameUtf8 = fileNameUtf8.replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileNameUtf8 + ".xlsx");

        try (OutputStream os = response.getOutputStream()) {
            List<String> headers = trainDataSampleDownloadService.getHeaders();
            trainDataSampleDownloadService.createWorksheet(os, null, headers);

        } catch (IOException e) {
            log.error("[ERROR] download train data upload sample : {}", e.getMessage());
        }
    }
}
