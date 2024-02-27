package com.kt.smp.fileutil.controller;

import static java.nio.file.StandardOpenOption.CREATE;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.base.dto.HttpResponse;
import com.kt.smp.base.func.JsonUtil;
import com.kt.smp.common.dto.BaseResponseDto;
import com.kt.smp.common.util.FileUtil;
import com.kt.smp.config.CommonConstants;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.comm.directory.service.HomePathResolver;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.common.component.SttCmsResultStatus;
import com.kt.smp.stt.deploy.model.domain.SttDeployModelVO;
import com.kt.smp.stt.deploy.model.service.SttDeployModelService;
import com.kt.smp.stt.train.train.domain.SttTrainVO;
import com.kt.smp.stt.train.train.service.SttTrainService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/resultmodelid/api")
@Tag(name = "STT 결과모델 다운로드", description = "STT 결과 모델 다운로드 API")
public class SttResultModelIdApiController {

    private static final String TAR_GZ = ".tar.gz";

    private final SttTrainService sttTrainService;

    private final ServiceModelService serviceModelService;

    private final SttDeployModelService sttDeployModelService;

    private final HomePathResolver homePathResolver;

    private final ConfigService configService;

    @Value("${spring.profiles.active}")
    private String profile;
    
    @Value("${directory.home}")
    private String directoryHome;

    /**
     * 엔진으로부터 콟백을 받아 학습 모델 경로가 저장되어있는지 확인하는 api
     * 학습 모델 경로가 저장되어있어야 결과 모델 다운로드가 가능
     *
     * @param resultModelId
     * @return
     */
    @SmpServiceApi(name = "결과 모델 조회", method = RequestMethod.GET, path = "/check", type = "조회", description = "결과 모델 조회")
    public ResponseEntity<BaseResponseDto<Object>> check(@RequestParam String resultModelId) {
        SttTrainVO sttTrainVO = sttTrainService.detailByResultModelId(resultModelId);
        //SttDeployModelVO sttDeployModelVO = sttDeployModelService.detailByResultModelId(resultModelId);
        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);

        if ( doesNotExist(sttTrainVO) ) {
        	log.info("============== in doesNotExist(sttTrainVO) ===============");
            responseDto.setResult(false);

            return ResponseEntity.ok(responseDto);
        }

        responseDto.setResult(true);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 결과모델은 아래의 파일들을 감싸는 zip 파일
     * 1. 배포 모델 등록을 위해 필요한 메타데이터를 포함한 json 파일 (sample.json)
     * 2. 엔진에서 생성한 학습 모델 (nas에 저장되어 있는 tar.gz 파일)
     *
     * sample.json에는 아래의 정보가 포함
     * 1. 서비스 모델
     * 2. 학습 데이터 개수
     * 3. 결과모델ID
     * 4. 학습 모델 검증을 위한 MD5 해시 키 (ModelAuthKey)
     *
     * @param resultModelId
     * @param response
     * @return
     * @throws IOException
     */
    @SmpServiceApi(name = "결과 모델 다운로드", method = RequestMethod.GET, path = "/download", type = "다운로드", description = "결과 모델 다운로드")
    public ResponseEntity<byte[]> download(
            @RequestParam String resultModelId, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String homePath = getHomePath(request);
        SttDeployModelVO sttDeployModelVO = sttDeployModelService.detailByResultModelId(resultModelId);
        SttTrainVO sttTrainVO = sttTrainService.detailByResultModelId(resultModelId);
        log.info(">>>>> resultModelId : "+resultModelId);
        String jsonFileName = createSampleJsonFile(homePath, sttTrainVO, sttDeployModelVO);
        List<String> fileNames = new ArrayList<>();
        fileNames.add(jsonFileName);


        if (profile.equals(CommonConstants.LOCAL_PROFILE)) {

            List<Path> paths = List.of(Paths.get(homePath, "upload", "class.txt"));
            Path output = Paths.get(homePath, "upload", "upload" + TAR_GZ);

            try {
                createTarGzipFiles(paths, output);
            } catch (Exception e) {
                log.error("[SttResultModelIdController.downloadFile] ERROR {}", e.getMessage());
            }

            fileNames.add(output.toString());

        } else {
        	if(!ObjectUtils.isEmpty(sttTrainVO)) {
        		log.info(">>> before fileNames.add, sttTrainVO.getModelPath : "+sttTrainVO.getModelPath());	
        	}else {
        		log.info(">>> before fileNames.add, sttDeployModelVO.getModelPath : "+sttDeployModelVO.getModelPath());
        	}
        	
            fileNames.add(org.springframework.util.ObjectUtils.isEmpty(sttTrainVO) ? sttDeployModelVO.getModelPath() : sttTrainVO.getModelPath());
        }

        String zipFileName = (org.springframework.util.ObjectUtils.isEmpty(sttTrainVO)? sttDeployModelVO.getResultModelId() : sttTrainVO.getResultModelId())+".zip";
        zipFileName = URLEncoder.encode(zipFileName, StandardCharsets.UTF_8);

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment;filename=" + zipFileName);

        try(ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
            for(String fileName : fileNames) {
            	log.info(">>>> in for(String fileName : fileNames), fileName : "+fileName);
                FileSystemResource fileSystemResource = new FileSystemResource(fileName);
                ZipEntry zipEntry = new ZipEntry(fileSystemResource.getFilename());
                zipEntry.setSize(fileSystemResource.contentLength());
                zipEntry.setTime(System.currentTimeMillis());
                log.info("---------------- before zipOutputStream.putNextEntry ------------------");
                zipOutputStream.putNextEntry(zipEntry);

                StreamUtils.copy(fileSystemResource.getInputStream(), zipOutputStream);
                log.info("--------------- before zipOutputStream.closeEntry -----------------");
                zipOutputStream.closeEntry();
            }

//            zipOutputStream.finish();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            log.info("--------------- before byteArrayOutputStream.writeTo ---------------");
            byteArrayOutputStream.writeTo(zipOutputStream);

            return ResponseEntity.ok(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            log.error("[SttResultModelIdApiController.downloadFile] ERROR {}", e.getMessage());
        } finally {
            File jsonFile = new File(jsonFileName);

            if (jsonFile.exists()) {
                FileUtil.deleteFile(jsonFile);
            }
        }

        return new ResponseEntity<byte[]>(
                JsonUtil.toJson(HttpResponse.onFailure("결과 모델 다운로드 실패"))
                .getBytes(StandardCharsets.UTF_8)
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean doesNotExist(SttTrainVO sttTrainVO) {
        return ObjectUtils.isEmpty(sttTrainVO)
                || StringUtils.isEmpty(sttTrainVO.getModelPath());
    }

    private boolean doesNotExist(SttDeployModelVO sttDeployModelVO) {
        return ObjectUtils.isEmpty(sttDeployModelVO)
                || StringUtils.isEmpty(sttDeployModelVO.getModelPath());
    }

    private void createTarGzipFiles(List<Path> paths, Path output)
            throws IOException {
        try (OutputStream fOut = Files.newOutputStream(output, CREATE);
             BufferedOutputStream buffOut = new BufferedOutputStream(fOut);
             GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(buffOut);
             TarArchiveOutputStream tOut = new TarArchiveOutputStream(gzOut)) {

            for (Path path : paths) {
                if (!Files.isRegularFile(path)) {
                    throw new IOException("Support only file!");
                }

                TarArchiveEntry tarEntry = new TarArchiveEntry(
                        path.toFile(),
                        path.getFileName().toString());

                tOut.putArchiveEntry(tarEntry);

                // copy file to TarArchiveOutputStream
                Files.copy(path, tOut);

                tOut.closeArchiveEntry();
            }

            tOut.finish();
        }
    }

    private String createSampleJsonFile(String homePath, SttTrainVO sttTrainVO, SttDeployModelVO sttDeployModelVO)
            throws IOException {
    	log.info(">>>>> sttTrainVO.getModelType : "+sttTrainVO.getModelType()+" / sttTrainVO.getModelPath : "+sttTrainVO.getModelPath()+" / sttTrainVO.getDataTime : "+sttTrainVO.getDataTime());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("serviceModel", org.springframework.util.ObjectUtils.isEmpty(sttTrainVO)
                ? serviceModelService.detailS(sttDeployModelVO.getServiceModelId() + "").getServiceModelName() 
                		: serviceModelService.detailS(sttTrainVO.getServiceModelId() + "").getServiceModelName());
        if(sttTrainVO.getModelType().equals("CALSS")||sttTrainVO.getModelType().equals("SERVICE")||sttTrainVO.getModelType().equals("E2ELM")) {
        	jsonObject.addProperty("dataNum", ObjectUtils.isEmpty(sttTrainVO)? sttDeployModelVO.getDataNum() : sttTrainVO.getDataNum());	
        }else {
        	jsonObject.addProperty("dataTime", ObjectUtils.isEmpty(sttTrainVO)? sttDeployModelVO.getDataTime() : sttTrainVO.getDataTime());
        }
        jsonObject.addProperty("modelType", ObjectUtils.isEmpty(sttTrainVO)? sttDeployModelVO.getModelType() : sttTrainVO.getModelType());
        jsonObject.addProperty("resultModelId", org.springframework.util.ObjectUtils.isEmpty(sttTrainVO)? sttDeployModelVO.getResultModelId() : sttTrainVO.getResultModelId());
        jsonObject.addProperty("modelAuthKey", org.springframework.util.ObjectUtils.isEmpty(sttTrainVO)? sttDeployModelVO.getModelAuthKey() : sttTrainVO.getModelAuthKey());

        Files.createDirectories(Paths.get(homePath, "upload"));
        String fileName = Paths.get(homePath, "upload", "sample.json").toString();

        try (
            FileWriter fileWriter = new FileWriter(fileName)
        ) {
            fileWriter.write(jsonObject.toString());

            return fileName;
        } catch (IOException e) {
            log.error("[JSONObject File Create] {}", e.getMessage());
        }

        return null;
    }

    private String getHomePath(HttpServletRequest request) {

        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        ConfigDto config = configService.getByProjectCode(header.getProjectCode());
        //return homePathResolver.resolve(config);
        return directoryHome;
    }
}
