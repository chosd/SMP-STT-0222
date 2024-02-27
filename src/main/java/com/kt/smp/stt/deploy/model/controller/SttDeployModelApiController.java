package com.kt.smp.stt.deploy.model.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.common.dto.BaseResponseDto;
import com.kt.smp.common.util.FileUtil;
import com.kt.smp.fileutil.dto.UploadFileResponseDto;
import com.kt.smp.fileutil.util.FileUploadUtil;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.comm.directory.service.HomePathResolver;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.common.ResultCode;
import com.kt.smp.stt.common.component.SttCmsResultStatus;
import com.kt.smp.stt.deploy.model.domain.SttDeployModelListVO;
import com.kt.smp.stt.deploy.model.domain.SttDeployModelSearchCondition;
import com.kt.smp.stt.deploy.model.domain.SttDeployModelVO;
import com.kt.smp.stt.deploy.model.dto.SttDeployModelSearchResponseDto;
import com.kt.smp.stt.deploy.model.service.SttDeployModelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static com.kt.smp.stt.common.ResponseMessage.*;
import static com.kt.smp.stt.common.component.SttCmsResultStatus.DATA_DUPLICATED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/deploy/model/api")
@Tag(name = "STT 배포 모델 관리", description = "STT 배포 모델 관리 API")
public class SttDeployModelApiController {

    private final static String REG_DT_DESC = "REG_DT DESC";

    //public static final String DEPLOY_MODEL_PATH = "/deployModel/svc/"; 기존소스
    public static final String DEPLOY_MODEL_PATH = "/model/svc/";

    private final ServiceModelService serviceModelService;

    private final SttDeployModelService sttDeployModelService;

    private final HomePathResolver homePathResolver;

    private final ConfigService configService;
    
    @Value("${directory.home}")
    private String directoryHome;
    
    @Value("${spring.profiles.active}")
    private String profile;

    @SmpServiceApi(name = "배포 모델 목록 검색", method = RequestMethod.GET, path = "/listPage", type = "검색", description = "배포 모델 목록 검색")
    public ResponseEntity<SttDeployModelSearchResponseDto> listPage(
            @ModelAttribute SttDeployModelSearchCondition searchCondition) {
        searchCondition.setOrderBy(REG_DT_DESC);

        PageHelper.startPage(searchCondition.getPage(), searchCondition.getPageSize(), REG_DT_DESC);
        Page<SttDeployModelVO> page = sttDeployModelService.list(searchCondition);
        SttDeployModelSearchResponseDto searchResponseDto = new SttDeployModelSearchResponseDto(page);

        return ResponseEntity.ok(searchResponseDto);
    }

    @SmpServiceApi(name = "배포 모델 상세조회", method = RequestMethod.GET, path = "/{deployModelId}", type = "상세조회", description = "배포 모델 상세 조회")
    public ResponseEntity<BaseResponseDto<Object>> detail(@PathVariable(name = "deployModelId") Long deployModelId) {
        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);

        try {
            SttDeployModelVO detail = sttDeployModelService.detail(deployModelId);
            responseDto.setResult(detail);
        } catch (Exception e) {
            log.error("[ERROR] detail : {}", e.getMessage());
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(DETAIL_FAIL_MESSAGE);
        }

        return ResponseEntity.ok(responseDto);
    }
    
    @SmpServiceApi(name = "배포모델 결과모델ID 중복체크",method = RequestMethod.POST,path = "/existModelId",type = "중복체크",description = "배포모델 결과모델ID 중복체크")
    public ResponseEntity<BaseResponseDto<Object>> existModelIdCheck(@RequestParam("resultModelId") String resultModelId) {
      
  	  BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
  	  boolean hasDuplicatedModelId = sttDeployModelService.hasDuplicateResultModelId(resultModelId);
  	  log.info(">>> result hasDuplicatedModelId : "+ hasDuplicatedModelId);
      
  	  if (hasDuplicatedModelId) {

  		  responseDto.setResultCode(DATA_DUPLICATED.getResultCode());
      
  	  }

  	  return makeResponse(responseDto, "speakerCodeCheck");
    }
    
    private ResponseEntity<BaseResponseDto<Object>> makeResponse(BaseResponseDto<Object> responseDto,String function) {
    	    
    	responseDto.setResultMsg(getMessage(function, responseDto.getResultCode()));

    	return ResponseEntity.ok(responseDto);
    }
    
    private String getMessage(String function, String resultCode) {
        String msg = "";
        StringBuffer sbMsg = new StringBuffer(msg);

        String key = "";

        // 성공 응답이 아닌 경우, ${function}.fail (있으면) 메시지 먼저 출력
        if (!resultCode.startsWith("0")) {
          key = "stt deploymodel" + "." + function + ".fail";
          //msg = messageProperties.getMessage(key);
//          msg = "실패";
            sbMsg.append("실패");
        }

        //key = MESSAGE_KEY + "." + function + "." + resultCode;
        key = "stt deploymodel" + "." + function + "." + resultCode;
//        msg += "성공";
        sbMsg.append("성공");

        return msg;
      }

    /**
     * 설명이 255자 이하일 경우에만 등록
     * 이미 등록한 배포 모델을 재등록 요청할 경우 설명만 수정이 됨 (한 마디로 덮어 쓰는 구조, 중복 등록 X)
     * 버전 관리를 위해 NAS 내 파일 경로를 { 서비스 코드 } / { System.currentTimeMillis() } / 배포 모델.tar.gz와 같이 저장
     *
     * @param request
     * @param modelFile
     * @param sttDeployModelVO
     * @return
     * @throws IOException
     */
    @SmpServiceApi(name = "배포 모델 등록", method = RequestMethod.POST
            , path = "/insert", type = "등록"
            , description = "배포 모델 등록", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponseDto<Object>> insert(HttpServletRequest request,
                                                          @RequestPart("modelFile") MultipartFile modelFile,
                                                          @RequestPart("properties") SttDeployModelVO sttDeployModelVO) throws IOException {
        SttDeployModelVO findModel = sttDeployModelService.detailByResultModelId(sttDeployModelVO.getResultModelId());
        boolean modelExists = org.apache.commons.lang3.ObjectUtils.isNotEmpty(findModel) ? true : false;
        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        log.info(">>>>> receive data | sttDeployModelVO.getDataTime : "+sttDeployModelVO.getDataTime()+" / sttDeployModelVO.getDataNum : "+sttDeployModelVO.getDataNum()
        +" / sttDeployModelVO.getModelType : "+sttDeployModelVO.getModelType());
        if (StringUtils.isNotEmpty(sttDeployModelVO.getDescription())
                && sttDeployModelVO.getDescription().length() > 255) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(DESCRIPTION_AT_MOST_MESSAGE);

            return ResponseEntity.ok(responseDto);
        }

        UploadFileResponseDto fileResponseDto = requestFileUpload(modelFile, sttDeployModelVO.getServiceModelId(), sttDeployModelVO.getModelFileName(), request);

        if (org.apache.commons.lang3.ObjectUtils.isEmpty(fileResponseDto)
                || ObjectUtils.isEmpty(fileResponseDto.isUploadSuccess())
                || !fileResponseDto.isUploadSuccess()) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(FILE_UPLOAD_FAIL_MESSAGE);

            return ResponseEntity.ok(responseDto);
        }

        if (modelExists) {
            updateDeployUpload(request, sttDeployModelVO, fileResponseDto);
            responseDto.setResultMsg(DEPLOY_MODEL_DESCRIPTION_UPDATE_MESSAGE);
        } else {
            insertDeployModel(request, sttDeployModelVO, fileResponseDto);
            responseDto.setResultMsg(DEPLOY_MODEL_UPLOAD_SUCCESS_MESSAGE);
        }

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 배포 모델 설명을 수정하는 api
     * 설명은 255자 이하여야 함
     *
     * @param request
     * @param sttDeployModelVO
     * @return
     * @throws IOException
     */
    @SmpServiceApi(name = "배포 모델 수정", method = RequestMethod.POST, path = "/update", type = "수정", description = "배포 모델 수정")
    public ResponseEntity<BaseResponseDto<Object>> update(HttpServletRequest request,
                                                          @RequestBody SttDeployModelVO sttDeployModelVO) throws IOException {
        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        responseDto.setResultMsg(DEPLOY_MODEL_DESCRIPTION_UPDATE_MESSAGE);

        if (StringUtils.isNotEmpty(sttDeployModelVO.getDescription())
                && sttDeployModelVO.getDescription().length() > 255) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(DESCRIPTION_AT_MOST_MESSAGE);

            return ResponseEntity.ok(responseDto);
        }

        updateDeployUpload(request, sttDeployModelVO, null);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 한 개 이상의 배포 모델이 선택될 경우에만 삭제가 진행 됨
     * hard delete 진행 (DB에서 완전 제거)
     *
     * @param request
     * @param sttDeployModelListVO
     * @return
     * @throws IOException
     */
    @SmpServiceApi(name = "배포 모델 삭제", method = RequestMethod.POST, path = "/delete", type = "삭제", description = "배포 모델 삭제")
    public ResponseEntity<BaseResponseDto<Object>> delete(HttpServletRequest request,
                                                          @RequestBody SttDeployModelListVO sttDeployModelListVO) throws IOException {
        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        responseDto.setResultMsg(DEPLOY_MODEL_DELETE_SUCCESS_MESSAGE);

        if (ObjectUtils.isEmpty(sttDeployModelListVO)
                || sttDeployModelListVO.getModelIdList().size() == 0) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(SELECT_AT_LEAST_ONE_DEPLOY_MODEL_MESSAGE);

            return ResponseEntity.ok(responseDto);
        }

        for (Long modelId : sttDeployModelListVO.getModelIdList()) {
            SttDeployModelVO sttDeployModelVO = sttDeployModelService.detail(modelId);
            String modelPath = sttDeployModelVO.getModelPath();

            try {
                File file = new File(modelPath);
                FileUtil.deleteFile(file);
            } catch (Exception e) {
                log.error("[SttDeployModelApiController.delete ERROR] {}", e.getMessage());
            }
        }

        sttDeployModelService.delete(sttDeployModelListVO);

        return ResponseEntity.ok(responseDto);
    }

    private UploadFileResponseDto requestFileUpload(MultipartFile modelFile, long serviceModelId, String modelFileName, HttpServletRequest request) throws IOException {

        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        ConfigDto config = configService.getByProjectCode(header.getProjectCode());
        //String homePath = homePathResolver.resolve(config); 기존소스
        String homePath = directoryHome; // KT nas 경로에 맞추기 위함
        String serviceModelCode = serviceModelId+"";
        log.info(">>>> before createDirectories serviceModelCode : "+serviceModelCode);
        //Files.createDirectories(Paths.get(homePath + DEPLOY_MODEL_PATH + serviceModelCode + "/" + System.currentTimeMillis() + "/")); 기존소스
        Files.createDirectories(Paths.get(homePath + DEPLOY_MODEL_PATH + serviceModelCode + "/" ));
        
        //return FileUploadUtil.uploadFile(modelFile, homePath + DEPLOY_MODEL_PATH + serviceModelCode + "/" + System.currentTimeMillis() + "/");
        return FileUploadUtil.uploadFileDeploy(modelFile, homePath + DEPLOY_MODEL_PATH + serviceModelCode + "/", profile );
    }

    private void insertDeployModel(HttpServletRequest request
            , SttDeployModelVO sttDeployModelVO
            , UploadFileResponseDto fileResponseDto) {

        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        String name = header.getUserId();
        String ip = request.getRemoteAddr();

        sttDeployModelVO.setModelPath(fileResponseDto.getFilePath());
        sttDeployModelVO.setUploadedBy(name);
        sttDeployModelVO.setRegId(name);
        sttDeployModelVO.setRegIp(ip);
        sttDeployModelVO.setUpdId(name);
        sttDeployModelVO.setUpdIp(ip);
        log.info(">>>> before deployModel insert | fileResponseDto.getFilePath : "+fileResponseDto.getFilePath());
        sttDeployModelService.insert(sttDeployModelVO);
    }

    private void updateDeployUpload(HttpServletRequest request, SttDeployModelVO sttDeployModelVO, UploadFileResponseDto fileResponseDto) {
        if (fileResponseDto != null) {
            sttDeployModelVO.setModelPath(fileResponseDto.getFilePath());
        }

        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        sttDeployModelVO.setUpdId(header.getUserId());
        sttDeployModelVO.setUpdIp(request.getRemoteAddr());
        
        sttDeployModelService.update(sttDeployModelVO);
    }

}
