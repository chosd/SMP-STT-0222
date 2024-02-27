package com.kt.smp.stt.train.train.controller;

import static com.kt.smp.stt.common.ResponseMessage.DESCRIPTION_AT_MOST_MESSAGE;
import static com.kt.smp.stt.common.ResponseMessage.DETAIL_FAIL_MESSAGE;
import static com.kt.smp.stt.common.ResponseMessage.FILE_UPLOAD_FAIL_MESSAGE;
import static com.kt.smp.stt.common.ResponseMessage.NO_TRAIN_DATA_MESSAGE;
import static com.kt.smp.stt.common.ResponseMessage.NO_TRAIN_VO_IN_DB_MESSAGE;
import static com.kt.smp.stt.common.ResponseMessage.RETRAIN_RESTRAINT_MESSAGE;
import static com.kt.smp.stt.common.ResponseMessage.STATUS_INQUIRY_FAIL_MESSAGE;
import static com.kt.smp.stt.common.ResponseMessage.TRAIN_DATA_AT_LEAST_MESSAGE;
import static com.kt.smp.stt.common.ResponseMessage.TRAIN_DATA_AT_MOST_MESSAGE;
import static com.kt.smp.stt.common.ResponseMessage.TRAIN_REQUEST_SUCCESS_MESSAGE;
import static com.kt.smp.stt.common.component.SttCmsResultStatus.INTERNAL_SERVER_ERROR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kt.smp.common.exception.SttException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.common.dto.BaseResponseDto;
import com.kt.smp.common.util.ExternalApiRequester;
import com.kt.smp.common.util.FileUtil;
import com.kt.smp.common.util.JacksonUtil;
import com.kt.smp.config.CommonConstants;
import com.kt.smp.fileutil.dto.UploadFileResponseDto;
import com.kt.smp.fileutil.util.FileUploadUtil;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.common.CallbackUrlResolver;
import com.kt.smp.stt.common.EngineUrlResolver;
import com.kt.smp.stt.common.ResultCode;
import com.kt.smp.stt.common.component.SttCmsResultStatus;
import com.kt.smp.stt.common.dto.BaseResultDto;
import com.kt.smp.stt.train.train.domain.SttTrainCallbackRequestDto;
import com.kt.smp.stt.train.train.domain.SttTrainCallbackResponseDto;
import com.kt.smp.stt.train.train.domain.SttTrainMultipartRequestDto;
import com.kt.smp.stt.train.train.domain.SttTrainRequestDto;
import com.kt.smp.stt.train.train.domain.SttTrainSearchCondition;
import com.kt.smp.stt.train.train.domain.SttTrainStatusResponseDto;
import com.kt.smp.stt.train.train.domain.SttTrainStatusType;
import com.kt.smp.stt.train.train.domain.SttTrainVO;
import com.kt.smp.stt.train.train.dto.SttTrainSearchResponseDto;
import com.kt.smp.stt.train.train.service.SttTrainService;
import com.kt.smp.stt.train.trainData.domain.SttTrainAmDataVO;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataAmSearchCondition;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataSearchCondition;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataVO;
import com.kt.smp.stt.train.trainData.dto.SttTrainDataAmSearchResponseDto;
import com.kt.smp.stt.train.trainData.service.SttTrainDataService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/train/api")
@Tag(name = "STT 학습 관리", description = "STT 학습 관리 API")
public class SttTrainApiController {

    private static final int MAX_PAGE_SIZE = 10000;

    private final static String REG_DT_DESC = "REG_DT DESC";

    private static final String TRAIN_DATA_PATH = "/trainData/svc/";
    
    private static final String TRAINED_DATA_PATH = "/trainData/trainedModel/";

    private static final String STT_TRAIN_STATUS_URL = "/stt/train/status?serviceCode=";
    
    private static final String CORE_TRAIN_URL = "/stt/train";
    
    private static final String CORE_TRAIN_MULTIPART_URL = "/stt/train/multipart";

    private final RestTemplate restTemplate;

    private final EngineUrlResolver engineUrlResolver;

    private final SttTrainService sttTrainService;

    private final SttTrainDataService sttTrainDataService;

    private final ServiceModelService serviceModelService;

    private final ConfigService configService;

    private final CallbackUrlResolver callbackUrlResolver;
    
    private final ExternalApiRequester externalRequestUtil;
    
    
    @Value("${spring.profiles.active}")
    private String profile;
    
    @Value("${directory.home}")
    private String directoryHome;

    @SmpServiceApi(name = "학습 내역 검색", method = RequestMethod.GET, path = "/listPage", type = "검색", description = "학습 내역 검색")
    public ResponseEntity<SttTrainSearchResponseDto> listPage(
    		@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute SttTrainSearchCondition searchCondition) {
        searchCondition.setOrderBy(REG_DT_DESC);
        
        PageHelper.startPage(pageNum , searchCondition.getPageSize(), REG_DT_DESC);
        Page<SttTrainVO> page = sttTrainService.list(searchCondition);
        
        SttTrainSearchResponseDto searchResponseDto = new SttTrainSearchResponseDto(page);
        
        return ResponseEntity.ok(searchResponseDto);
    }
    
    @SmpServiceApi(name = "AM데이터셋 검색", method = RequestMethod.GET, path = "/datasetList", type = "검색", description = "AM데이터셋 검색")
    public ResponseEntity<SttTrainDataAmSearchResponseDto> datasetList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "orderBy", defaultValue = "REG_DT DESC") String orderBy,
    		@ModelAttribute("searchCondition") SttTrainDataAmSearchCondition searchCondition) {
    	
    	PageHelper.startPage(pageNum, pageSize, orderBy);
        Page<SttTrainAmDataVO> page = sttTrainDataService.amDataSearch(searchCondition);
        
        SttTrainDataAmSearchResponseDto searchResponseDto = new SttTrainDataAmSearchResponseDto(page);
        
        return ResponseEntity.ok(searchResponseDto);
    }

    @SmpServiceApi(name = "학습 내역 상세조회", method = RequestMethod.GET, path = "/{trainId}", type = "상세조회", description = "학습 내역 상세 조회")
    public ResponseEntity<BaseResponseDto<Object>> detail(@PathVariable(name = "trainId") Long trainId) {
        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);

        try {
            SttTrainVO detail = sttTrainService.detail(trainId);
            responseDto.setResult(detail);
        } catch (Exception e) {
            log.error("[ERROR] detail : {}", e.getMessage());
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(DETAIL_FAIL_MESSAGE);
        }

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 학습을 정상적으로 진행하기 위해서는
     * 1. 학습 데이터가 30개 이상 1,000,000개 이하
     * 2. 설명이 255자 이하
     *
     * 위 조건에 충족할 경우
     * class.txt 텍스트 파일을 생성하고 조건에 맞는 학습 데이터를 채워 줌 (구분자: 개행문잦, 학습 데이터를 각각의 가중치만큼 반복해서 넣어줌)
     * 해당 파일을 nas에 저장
     *
     * 이후 엔진에 학습 api 요청
     * 1. class.txt가 저장된 경로
     * 2. 서비스모델 코드
     * 3. 학습 결과를 전달 받을 콜백 url
     * 4. LM Type (현재는 CLASS로 고정)
     *
     * @param request
     * @param sttTrainVO
     * @return
     * @throws IOException
     */
    @SmpServiceApi(name = "학습 등록", method = RequestMethod.POST, path = "/insert", type = "등록", description = "학습 등록")
    public ResponseEntity<BaseResponseDto<Object>> insert(HttpServletRequest request, @RequestBody SttTrainVO sttTrainVO) throws IOException {
    	log.info("---->>>>>> profile : " + profile);
//        String serviceModelCode = serviceModelService.detail(sttTrainVO.getServiceModelId()).getServiceCode();
        String serviceModelCode = sttTrainVO.getServiceModelId() + "";
//        sttTrainVO.setServiceModelId(Long.parseLong(serviceModelCode));
        BaseResponseDto<Object> responseDto = null;
        if(sttTrainVO.getModelType().equals("E2ELM")) {
        	responseDto = checkValidation(sttTrainVO, getTrainDataCountS(serviceModelCode ), null); // 기존로직
        }else  {
        	responseDto= amCheckValidation(sttTrainVO, null);
        }

        if (responseDto.getResultCode().equals(ResultCode.INTERNAL_SERVER_ERROR.getCode())) {
            return ResponseEntity.ok(responseDto);
        }
        
        String homePath = getHomePath(request);
        log.info(">>>>>> serviceModelCode : " + serviceModelCode + " / homePath : " + homePath);
        UploadFileResponseDto uploadFileResponseDto = new UploadFileResponseDto();
        if(sttTrainVO.getModelType().equals("E2ESL") ) {
    		uploadFileResponseDto.setTrainE2ESLDataPathList(sttTrainVO.getTrainE2ESLDataPathList());
        	uploadFileResponseDto.setTrainE2ESLWavPathList(sttTrainVO.getTrainE2ESLWavPathList());
        	uploadFileResponseDto.setFilePath("/nas/trainData/svc/2/text.sample"); // 순전히 DB저장용
        	log.info(">>>>>> after uploadFileResponseDto setting : "+uploadFileResponseDto.getTrainE2ESLDataPathList().toString()+" / wavPath : "+uploadFileResponseDto.getTrainE2ESLWavPathList().toString());
        }else if(sttTrainVO.getModelType().equals("E2EMSL")){
        	uploadFileResponseDto.setTrainE2ESLDataPathList(sttTrainVO.getTrainE2ESLDataPathList());
        	uploadFileResponseDto.setTrainE2ESLWavPathList(sttTrainVO.getTrainE2ESLWavPathList());
        	uploadFileResponseDto.setTrainE2EUSLWavPathList(sttTrainVO.getTrainE2EUSLWavPathList());
        	uploadFileResponseDto.setFilePath("/nas/trainData/svc/2/text.sample"); // 순전히 DB저장용
        	log.info(">>>>>> after uploadFileResponseDto setting : "+uploadFileResponseDto.getTrainE2ESLDataPathList().toString()+" / wavPath : "+uploadFileResponseDto.getTrainE2ESLWavPathList().toString()
        	        +" / uslWavPath : "+uploadFileResponseDto.getTrainE2EUSLWavPathList().toString());
        }else if(sttTrainVO.getModelType().equals("E2EUSL")) {
        	uploadFileResponseDto.setTrainE2EUSLWavPathList(sttTrainVO.getTrainE2EUSLWavPathList());
        	uploadFileResponseDto.setFilePath("/nas/trainData/svc/2/text.sample"); // 순전히 DB저장용
        	log.info(">>>>>> after uploadFileResponseDto setting : "+uploadFileResponseDto.getTrainE2EUSLWavPathList().toString());
        }else {
        	// 텍스트타입의 학습요청, CLASS SERVICE E2ELM
        	uploadFileResponseDto = requestFileUpload(homePath, serviceModelCode, sttTrainVO);
        	if (ObjectUtils.isEmpty(uploadFileResponseDto) || ObjectUtils.isEmpty(uploadFileResponseDto.isUploadSuccess()) || !uploadFileResponseDto.isUploadSuccess()) {
                responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
                responseDto.setResultMsg(FILE_UPLOAD_FAIL_MESSAGE);

                return ResponseEntity.ok(responseDto);
            }
        }
        

        if (!profile.equals(CommonConstants.LOCAL_PROFILE)) {
            BaseResultDto sttTrainResponseDto = callCoreTrainApi(request, sttTrainVO, uploadFileResponseDto);

            String resultDescription = ResultCode.findByCode(sttTrainResponseDto.getResultCode()).getDescription();
            
            responseDto.setResultCode(sttTrainResponseDto.getResultCode());
            responseDto.setResultMsg(resultDescription);
            
            if (sttTrainResponseDto.getResultCode().equals("0000")) {
                insertSttTrainVO(request, sttTrainVO, uploadFileResponseDto);
            }

        } else {
            insertSttTrainVO(request, sttTrainVO, uploadFileResponseDto);
            SttTrainVO newSttTrainVO = sttTrainService.detailByResultModelId(sttTrainVO.getResultModelId());
            newSttTrainVO.setModelType(sttTrainVO.getModelType()); // 기존 CLASS 고정값이 아닌 화면에서 전달되는 값 세팅
            updateLocalSttTrainVO(newSttTrainVO);
        }
        
        return ResponseEntity.ok(responseDto);
    }
    
    
    /**
    *@MethodName : insertMultipart
    *@작성일 : 2023. 9. 19.
    *@작성자 : wonyoung.ahn
    *@변경이력 :
    *@Method설명 :
    *@param request
    *@param sttTrainVO
    *@param file
    *@return
    *@throws IOException
    */
    @SmpServiceApi(name = "학습 등록 NAS미사용", method = RequestMethod.POST, path = "/insertMultipart", type = "등록", description = "학습 등록 NAS미사용")
    public ResponseEntity<BaseResponseDto<Object>> insertMultipart(
    		HttpServletRequest request, 
    		@RequestPart("trainInfoData") SttTrainVO sttTrainVO, 
    		@RequestPart("trainData") MultipartFile file) throws IOException {
    	
//        String serviceModelCode = serviceModelService.detailS(sttTrainVO.getServiceModelId() + "").getServiceCode();
        String serviceModelCode = sttTrainVO.getServiceModelId() + "";
        sttTrainVO.setServiceModelId(Long.parseLong(serviceModelCode));
        
    	List<ConfigDto> configDtos = configService.getAllUserDefined();
    	for (ConfigDto configDto : configDtos) {
    		TenantContextHolder.set(configDto.getProjectCode());	
    	}

        BaseResponseDto<Object> responseDto
                = checkValidation(sttTrainVO, getTrainDataCountS(sttTrainVO.getServiceModelId() + ""), null);
        
        if (responseDto.getResultCode().equals(ResultCode.INTERNAL_SERVER_ERROR.getCode())) {
            return ResponseEntity.ok(responseDto);
        }
        
        if (!profile.equals(CommonConstants.LOCAL_PROFILE)) {
            BaseResultDto sttTrainResponseDto = callCoreTrainApiMultipart(request, sttTrainVO, file);

            String resultDescription = ResultCode.findByCode(sttTrainResponseDto.getResultCode()).getDescription();
            
            responseDto.setResultCode(sttTrainResponseDto.getResultCode());
            responseDto.setResultMsg(resultDescription);
            
            log.info(">>>>>> callCoreTrainApiMultipart result : {}",sttTrainResponseDto.toString());
            
            if (sttTrainResponseDto.getResultCode().equals("0000")) {
                insertSttTrainVO(request, sttTrainVO, new UploadFileResponseDto());
            }

        } else {
            insertSttTrainVO(request, sttTrainVO, new UploadFileResponseDto());
            SttTrainVO newSttTrainVO = sttTrainService.detailByResultModelId(sttTrainVO.getResultModelId());
            updateLocalSttTrainVO(newSttTrainVO);
        }
    	
    	return ResponseEntity.ok(responseDto);
    }

    /**
     * 학습과 동일한 프로세스
     * 유일한 차이점이라면 마지막 학습 요청 상태가 FAIL이여야지 재학습 요청 가능하도록 제한
     *
     * @param request
     * @param sttTrainVO
     * @return
     * @throws IOException
     */
    @SmpServiceApi(name = "재학습 등록", method = RequestMethod.POST, path = "/update", type = "재등록", description = "재학습 등록")
    public ResponseEntity<BaseResponseDto<Object>> update(HttpServletRequest request,@RequestBody SttTrainVO sttTrainVO) throws IOException {
//        String serviceModelCode = serviceModelService.detail(sttTrainVO.getServiceModelId()).getServiceCode();
        String serviceModelCode = sttTrainVO.getServiceModelId() + "";
        sttTrainVO.setServiceModelId(Long.parseLong(serviceModelCode));       
    	SttTrainVO lastSttTrainVO = sttTrainService.detailLastOne(sttTrainVO.getServiceModelId() + "");
        lastSttTrainVO.setDescription(sttTrainVO.getDescription());
        BaseResponseDto<Object> responseDto= checkValidation(sttTrainVO, getTrainDataCountS(sttTrainVO.getServiceModelId() + ""), lastSttTrainVO);

        if (responseDto.getResultCode().equals(ResultCode.INTERNAL_SERVER_ERROR.getCode())) {
            return ResponseEntity.ok(responseDto);
        }

        String homePath = getHomePath(request);
        UploadFileResponseDto uploadFileResponseDto = new UploadFileResponseDto();
        if(lastSttTrainVO.getModelType().equals("E2ESL") ) {
    		uploadFileResponseDto.setTrainE2ESLDataPathList(lastSttTrainVO.getTrainE2ESLDataPathList());
        	uploadFileResponseDto.setTrainE2ESLWavPathList(lastSttTrainVO.getTrainE2ESLWavPathList());
        	uploadFileResponseDto.setFilePath("/nas/trainData/svc/2/text.sample"); // 순전히 DB저장용
        }else if(lastSttTrainVO.getModelType().equals("E2EMSL")){
        	uploadFileResponseDto.setTrainE2ESLDataPathList(lastSttTrainVO.getTrainE2ESLDataPathList());
        	uploadFileResponseDto.setTrainE2ESLWavPathList(lastSttTrainVO.getTrainE2ESLWavPathList());
        	uploadFileResponseDto.setTrainE2EUSLWavPathList(lastSttTrainVO.getTrainE2EUSLWavPathList());
        	uploadFileResponseDto.setFilePath("/nas/trainData/svc/2/text.sample"); // 순전히 DB저장용
        }else if(lastSttTrainVO.getModelType().equals("E2EUSL")) {
        	uploadFileResponseDto.setTrainE2EUSLWavPathList(lastSttTrainVO.getTrainE2EUSLWavPathList());
        	uploadFileResponseDto.setFilePath("/nas/trainData/svc/2/text.sample"); // 순전히 DB저장용
        }else {
        	// 텍스트타입의 학습요청, CLASS SERVICE E2ELM
        	uploadFileResponseDto = requestFileUpload(homePath, serviceModelCode, lastSttTrainVO);
        	
        	if (ObjectUtils.isEmpty(uploadFileResponseDto) || ObjectUtils.isEmpty(uploadFileResponseDto.isUploadSuccess()) || !uploadFileResponseDto.isUploadSuccess()) {
                responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
                responseDto.setResultMsg(FILE_UPLOAD_FAIL_MESSAGE);

                return ResponseEntity.ok(responseDto);
            }
        }
        
        //UploadFileResponseDto uploadFileResponseDto = requestFileUpload(homePath,serviceModelService.detail(sttTrainVO.getServiceModelId()).getServiceCode(),sttTrainVO);


        if (!profile.equals(CommonConstants.LOCAL_PROFILE)) {
            BaseResultDto sttTrainResponseDto = callCoreTrainApi(request, lastSttTrainVO, uploadFileResponseDto);

            String resultDescription = ResultCode.findByCode(sttTrainResponseDto.getResultCode()).getDescription();

            responseDto.setResultCode(sttTrainResponseDto.getResultCode());
            responseDto.setResultMsg(resultDescription);
            
            log.info(">>>>>> callCoreTrainApiMultipart result : {}",sttTrainResponseDto.toString());
            
            if (sttTrainResponseDto.getResultCode().equals("0000")) {
                updateSttTrainVO(request, lastSttTrainVO, uploadFileResponseDto);
            }
            
        } else {
            updateLocalSttTrainVO(lastSttTrainVO);
        }

        sttTrainService.updateCallbackFields(lastSttTrainVO);

        return ResponseEntity.ok(responseDto);
    }

  /**
   * @MethodName : detailUpdate
   * @작성일 : 2024. 2. 2.
   * @작성자 : chanmi.joo
   * @변경이력 :
   * @Method설명 : 학습 상세 설명 update
   * @param request
   * @param sttTrainVO
   * @return
   * @throws IOException
   */
  @SmpServiceApi(name = "재학습 등록", method = RequestMethod.POST, path = "/detailUpdate", type = "학습 상세 수정", description = "학습 상세 수정")
  public ResponseEntity<BaseResponseDto<Object>> detailUpdate(HttpServletRequest request,@RequestBody SttTrainVO sttTrainVO) throws IOException {

    BaseResponseDto<Object> responseDto = new BaseResponseDto<>(
      SttCmsResultStatus.SUCCESS
    );

    try {
      sttTrainService.detailUpdate(sttTrainVO);
    } catch (SttException e) {
      log.error("[ERROR] detailUpdate : {}", e.getMessage());
      responseDto.setResultCode(e.getStatus().getResultCode());
    } catch (Exception e) {
      log.error("[ERROR] detailUpdate : {}", e.getMessage());
      responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
    }


    return ResponseEntity.ok(responseDto);
  }

    /**
     * 엔진에 학습 상태 조회 api 호출 후 상태를 반환
     *
     * @param id
     * @return
     */
    @SmpServiceApi(name = "학습 상태 조회", method = RequestMethod.GET, path = "/status", type = "조회", description = "학습 상태 조회")
    public ResponseEntity<BaseResponseDto<Object>> status(HttpServletRequest request, @RequestParam Long id) {
        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        String projectCode = request.getHeader(CommonConstants.X_SMP_PROJECT_CODE);

        if (profile.equals(CommonConstants.LOCAL_PROFILE)) {
            responseDto.setResult("test");

            return ResponseEntity.ok(responseDto);
        }

        SttTrainVO sttTrainVO = sttTrainService.detail(id);

        if (org.apache.commons.lang3.ObjectUtils.isEmpty(sttTrainVO)) {
            throw new IllegalArgumentException(NO_TRAIN_VO_IN_DB_MESSAGE);
        }

//        String serviceCode = Long.toString(sttTrainVO.getServiceModelId());
        String serviceCode = sttTrainVO.getServiceModelId() + "";
        String coreUrl = profile.equals(CommonConstants.LOCAL_PROFILE) ? engineUrlResolver.resolve() : engineUrlResolver.resolve(projectCode);
        ResponseEntity<SttTrainStatusResponseDto> responseEntity
                = restTemplate.getForEntity(coreUrl + STT_TRAIN_STATUS_URL + serviceCode
                , SttTrainStatusResponseDto.class);
        String status = (org.apache.commons.lang3.ObjectUtils.isEmpty(responseEntity) || org.apache.commons.lang3.ObjectUtils.isEmpty(responseEntity.getBody()))
                ? null : responseEntity.getBody().getStatus();

        if (status == null) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(STATUS_INQUIRY_FAIL_MESSAGE);

            ResponseEntity.ok(responseDto);
        }

        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(status)
                && !sttTrainVO.getStatus().equals(status)) {
            sttTrainVO.setStatus(status);
            sttTrainService.update(sttTrainVO);
        }

        responseDto.setResult(status);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 엔진에 학습/재학습 요청 시 전달한 callback url
     * 학습/재학습 결과를 엔진으로부터 수신
     * 따라서, @SmpServiceApi 어노테이션을 붙이지 않음
     *
     * @param requestDto
     * @return
     * @throws IOException 
     */
    // @SmpServiceApi(name = "학습 callback", method = RequestMethod.POST, path = "/callback", type = "콜백", description = "학습 콜백")
    @PostMapping("/callback")
    public ResponseEntity<SttTrainCallbackResponseDto> trainApiCallback(@RequestBody SttTrainCallbackRequestDto requestDto) throws IOException {
        log.info(" >>>>>>>>>>>>>>>> [Callback Train] {}", requestDto);
        List<ConfigDto> configDtos = configService.getAllUserDefined();
    	for (ConfigDto configDto : configDtos) {

        TenantContextHolder.set(configDto.getProjectCode());
      }
    	
	        SttTrainVO sttTrainVO = sttTrainService.detailLastOne(requestDto.getServiceCode());
	
	        if (org.apache.commons.lang3.ObjectUtils.isEmpty(sttTrainVO)) {
	            return ResponseEntity.ok().body(getDBFailResponse());
	        }
	        
	        if(requestDto.getStatus().equals("COMPLETE")) {
	        	String newModelPath = copyTrainedFile(requestDto, sttTrainVO);
	        	log.info(">>> receive newModelPath : "+newModelPath);
	        	requestDto.setModelPath(newModelPath);
	        }
	
	        updateCallbackSttTrainVO(requestDto, sttTrainVO);
	        deleteTrainDataFromNas(sttTrainVO);

        return ResponseEntity.ok().body(getSuccessResponse());
    }
    
    private String copyTrainedFile(SttTrainCallbackRequestDto receiveData, SttTrainVO sttTrainVO) throws IOException {
    	
    	Path source = Paths.get(receiveData.getModelPath());
    	// 아래과정은 엔진에서 준 모델경로값의 파일명을 아이디값을 붙인 값으로 바꾸기 위함
    	String receiveModelPath = receiveData.getModelPath();
    	String[] splitModelPath = receiveModelPath.split("/");
    	String receiveModelName = splitModelPath[splitModelPath.length-1];
    	String[] splitModelName = receiveModelName.split("[.]",2);
    	String originFileName = splitModelName[0];
    	// ex) model_2_E2ELM.tar.gz >  model_2_E2ELM_1.tar.gz
    	String newFileName = originFileName+"_"+sttTrainVO.getId()+"."+splitModelName[1];
    	String newModelPath = "";
    	StringBuffer stringBuffer = new StringBuffer(newModelPath);
    	for(int i=1; i<splitModelPath.length-1; i++  ) {
//    		newModelPath +="/"+splitModelPath[i]; 
    		stringBuffer.append("/");
    		stringBuffer.append(splitModelPath[i]);
    	}
    	
    	String homePath = directoryHome;
    	Path downloadPath = Paths.get(homePath, TRAINED_DATA_PATH );
    	String myPath = downloadPath.toString();
    	String finalModelPath = myPath+"/"+newFileName;
    	
    	log.info(">>>>> receiveModelName : "+receiveModelName+" / "+splitModelName[0]+" / "+splitModelName[1]+" / newFileName "+newFileName);
    	log.info(">>>> finalModelPath : "+finalModelPath);
    	
    	Path target = Paths.get(finalModelPath);
    	
    	try {
			FileUtils.copyFile(source.toFile(), target.toFile());
			return finalModelPath;
		} catch (IOException e) {
			throw new IOException("학습된 모델파일 복사에 실패했습니다(원본파일 경로: " + source + ", 복사본 경로: " + target + ")");
			//e.printStackTrace();
		}
    	
    }

    private BaseResponseDto<Object> checkValidation(SttTrainVO sttTrainVO, int trainDataCount, SttTrainVO lastSttTrainVO) {
        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        responseDto.setResultMsg(TRAIN_REQUEST_SUCCESS_MESSAGE);

        if (trainDataCount == 0) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(NO_TRAIN_DATA_MESSAGE);

            return responseDto;
        }

        if (trainDataCount < 1) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(TRAIN_DATA_AT_LEAST_MESSAGE);

            return responseDto;
        }

        if (trainDataCount > 1000000) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(TRAIN_DATA_AT_MOST_MESSAGE);

            return responseDto;
        }

        if (StringUtils.isNotEmpty(sttTrainVO.getDescription())
                && sttTrainVO.getDescription().length() > 255) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(DESCRIPTION_AT_MOST_MESSAGE);

            return responseDto;
        }

        if (lastSttTrainVO != null
                && !lastSttTrainVO.getStatus().equals(SttTrainStatusType.FAIL.getCode())) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(RETRAIN_RESTRAINT_MESSAGE);

            return responseDto;
        }

        return responseDto;
    }
    
    private BaseResponseDto<Object> amCheckValidation(SttTrainVO sttTrainVO, SttTrainVO lastSttTrainVO) {
        
    	BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        responseDto.setResultMsg(TRAIN_REQUEST_SUCCESS_MESSAGE);

        if (lastSttTrainVO != null && !lastSttTrainVO.getStatus().equals(SttTrainStatusType.FAIL.getCode())) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(RETRAIN_RESTRAINT_MESSAGE);

            return responseDto;
        }

        return responseDto;
    }

    private int getTrainDataCount(long serviceModelId) {
    	String serviceModelCode = serviceModelService.detail(serviceModelId).getServiceCode();
        return sttTrainDataService.getTrainDataCountWithRepeatCount(
        		SttTrainDataSearchCondition.builder().serviceModelId(serviceModelCode).build());
    }
    
    private int getTrainDataCountS(String serviceModelCode) {
    	int result = 0;
    	
    	result = sttTrainDataService.getTrainDataCountWithRepeatCount(SttTrainDataSearchCondition.builder().serviceModelId(serviceModelCode).build());
    	
    		
    	return result;
    }

    private BaseResultDto callCoreTrainApi(HttpServletRequest request, SttTrainVO sttTrainVO, UploadFileResponseDto responseDto) {

    	String receiveModelType = sttTrainVO.getModelType();
    	String trainApiParamDataPath = "";
    	List<String> trainApiParamE2ESLDataPathList = new ArrayList<>();
    	List<String> trainApiParamE2ESLWavPathList = new ArrayList<>();
    	List<String> trainApiParamE2EUSLWavPathList = new ArrayList<>();
    	
    	if(receiveModelType.equals("SERVICE") || receiveModelType.equals("CLASS") || receiveModelType.equals("E2ELM")) {
    		
    		trainApiParamDataPath = responseDto.getFilePath();
    	}else if(receiveModelType.equals("E2ESL")) { // 수협은 E2ELM,E2ESL 만 해당됨
    		log.info(">>>>>> receive uploadFileResponseDto : "+responseDto.getTrainE2ESLDataPathList().toString()+" / wavPath : "+responseDto.getTrainE2ESLWavPathList().toString());
    		trainApiParamE2ESLDataPathList = responseDto.getTrainE2ESLDataPathList();
    		trainApiParamE2ESLWavPathList = responseDto.getTrainE2ESLWavPathList();
    	}else if(receiveModelType.equals("E2EMSL")) {
    		
    		trainApiParamE2ESLDataPathList = responseDto.getTrainE2ESLDataPathList();
    		trainApiParamE2ESLWavPathList = responseDto.getTrainE2ESLWavPathList();
    		trainApiParamE2EUSLWavPathList = responseDto.getTrainE2EUSLWavPathList();
    	}else if(receiveModelType.equals("E2EUSL")) {
    		
    		trainApiParamE2EUSLWavPathList = responseDto.getTrainE2EUSLWavPathList();
    	}
    	
        SttTrainRequestDto requestDto = SttTrainRequestDto.builder()
                .trainDataPath(trainApiParamDataPath)
                .trainE2ESLDataPathList(trainApiParamE2ESLDataPathList)
				.trainE2ESLWavPathList(trainApiParamE2ESLWavPathList)
				.trainE2EUSLWavPathList(trainApiParamE2EUSLWavPathList)
                .serviceCode(sttTrainVO.getServiceModelId() + "")
                .callbackUrl(callbackUrlResolver.trainCallbackUrl())
                .modelType(receiveModelType)
                .build();

        BaseResultDto resultDto = null;

        try {
        	log.info(">>>> before train api call | requestDto.getServiceCode : "+requestDto.getServiceCode()+" / requestDto.getModelType : "+requestDto.getModelType()
        	+" / requestDto.getCallbackUrl : "+requestDto.getCallbackUrl()+" / requestDto.getTrainDataPath : "+requestDto.getTrainDataPath()
        	+" / requestDto.getTrainE2ESLDataPathList :"+requestDto.getTrainE2ESLDataPathList().toString()+" / requestDto.getTrainE2ESLWavPathList : "+requestDto.getTrainE2ESLWavPathList().toString()
        	+" / requestDto.getTrainE2EUSLWavPathList : "+requestDto.getTrainE2EUSLWavPathList().toString());
            resultDto = externalRequestUtil.requestPostWithSavingError(CORE_TRAIN_URL, requestDto);
        } catch (Exception ex) {
        	log.error("[ERROR] {}", ex.getMessage());
        }

        return resultDto;
    }
    
    // 멀티파트 파일 바이너리로 전송
    private BaseResultDto callCoreTrainApiMultipart(HttpServletRequest request
            , SttTrainVO sttTrainVO
            , MultipartFile file) throws IOException {

    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    	
        SttTrainMultipartRequestDto requestDto = SttTrainMultipartRequestDto.builder()
//        		.serviceCode("2")
                .serviceCode(sttTrainVO.getServiceModelId() + "")
                .callbackUrl(callbackUrlResolver.trainCallbackUrl())
                .modelType(sttTrainVO.getModelType())
                .build();

        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
        	@Override
        	public String getFilename() throws IllegalStateException {
        		return file.getOriginalFilename();
        	}
        };
        log.info("requestDto >>> {}", JacksonUtil.objectToJson(requestDto));
        
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("trainInfoData", requestDto);
        body.add("trainData", resource);
       
    	HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String projectCode = request.getHeader(CommonConstants.X_SMP_PROJECT_CODE);
        
        BaseResultDto responseEntity = externalRequestUtil.requestPostWithSavingError(CORE_TRAIN_MULTIPART_URL, requestEntity);

        return responseEntity;
    }

    private void updateSttTrainVO(HttpServletRequest request
            , SttTrainVO sttTrainVO
            , UploadFileResponseDto responseDto) {

        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        
        LocalDateTime currentTime = LocalDateTime.now();
        String formatedNow = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        sttTrainVO.setRequestedAt(formatedNow);
        sttTrainVO.setTrainDataPath(responseDto.getFilePath());
        sttTrainVO.setUpdId(header.getUserId());
        sttTrainVO.setUpdIp(request.getRemoteAddr());
        sttTrainVO.setDataNum((long) getTrainDataCountS(sttTrainVO.getServiceModelId() + ""));
        sttTrainVO.setDataNum(100L);
        sttTrainVO.setStatus(SttTrainStatusType.TRAINING.getCode());

        sttTrainService.update(sttTrainVO);
    }

    private void updateCallbackSttTrainVO(SttTrainCallbackRequestDto requestDto, SttTrainVO sttTrainVO) {
        sttTrainVO.setResultCode(requestDto.getResultCode());

        if (!requestDto.getResultCode().equals(ResultCode.SUCCESS.getCode())) {
            sttTrainVO.setResultMsg(ResultCode.findByCode(requestDto.getResultCode()).getDescription());
        }

        sttTrainVO.setStatus(requestDto.getStatus());
        sttTrainVO.setModelType(requestDto.getModelType());
        sttTrainVO.setModelPath(requestDto.getModelPath());
        sttTrainVO.setModelAuthKey(requestDto.getModelAuthKey());
        // 텍스트타입이 아닌 경우에는 [dataNum] 학습한 음성데이터의 전체시간 STT엔진으로부터 받아서 넣어준다.
        log.info(">>>>>> before train update | sttTrainVO.getModelType : "+sttTrainVO.getModelType()+" / requestDto.getModelType : "+requestDto.getModelType());
        if(!sttTrainVO.getModelType().equals("CLASS") && !sttTrainVO.getModelType().equals("SERVICE") && !sttTrainVO.getModelType().equals("E2ELM")) {
        	
        	sttTrainVO.setDataTime(requestDto.getTrainDataTime());
        }
        log.info(">>>> before train update | sttTrainVO.getStatus : "+sttTrainVO.getStatus()+" / sttTrainVO.getModelType : "+sttTrainVO.getModelType()
        +" / sttTrainVO.getModelPath"+sttTrainVO.getModelPath()
        +" / sttTrainVO.getModelAuthKey : "+sttTrainVO.getModelAuthKey()+" / sttTrainVO.getDataNum : "+sttTrainVO.getDataNum());
        sttTrainService.update(sttTrainVO);
    }

    private void insertSttTrainVO(HttpServletRequest request
            , SttTrainVO sttTrainVO
            , UploadFileResponseDto responseDto) {
    	log.info("============== start insertSttTrainVO() ================");
        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        String name = header.getUserId();
        String ip = request.getRemoteAddr();
        long trainDataNum = 0L;
        
        trainDataNum = (long) getTrainDataCountS(sttTrainVO.getServiceModelId() + "");
        
        sttTrainVO.setTrainDataPath(responseDto.getFilePath());
        sttTrainVO.setResultModelId(getResultModelId(sttTrainVO));
        sttTrainVO.setUpdatedBy(name);
        sttTrainVO.setRegId(name);
        sttTrainVO.setRegIp(ip);
        sttTrainVO.setUpdId(name);
        sttTrainVO.setUpdIp(ip);
        sttTrainVO.setDataNum(trainDataNum);
        //sttTrainVO.setDataNum(100L); 기존 소스
        sttTrainVO.setStatus(SttTrainStatusType.TRAINING.getCode());
        log.info(">>>>> before insert | sttTrainVO.getTrainAmDatasetList : "+sttTrainVO.getTrainAmDatasetList().toString());
        log.info(">>>> before insert | dataNum : "+trainDataNum+" / sttTrainVO.getTrainDataPath : "+sttTrainVO.getTrainDataPath()+" / sttTrainVO.getModelType : "+sttTrainVO.getModelType()
        +" / sttTrainVO.getResultModelId : "+sttTrainVO.getResultModelId());
        sttTrainService.insert(sttTrainVO);
    }

    private String getResultModelId(SttTrainVO sttTrainVO) {
        String formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        String resultModelId
                = serviceModelService.detailS(sttTrainVO.getServiceModelId() + "").getServiceModelName() 
                + " " + formatDate;

        while (true) {
            SttTrainVO trainVO = sttTrainService.detailByResultModelId(resultModelId);

            if (ObjectUtils.isEmpty(trainVO)) {
                break;
            }

            resultModelId = serviceModelService.detailS(sttTrainVO.getServiceModelId() + "").getServiceModelName() + " " + formatDate;
        }

        return resultModelId;
    }

    private UploadFileResponseDto requestFileUpload(String homePath, String serviceModelCode, SttTrainVO sttTrainVO) throws IOException {
//    	long serviceModelId = sttTrainVO.getServiceModelId();
    	MultipartFile trainDataFile = convertFileToMultipartFile(getClassTxt(homePath, sttTrainVO, serviceModelCode));
        Path uploadPath = Paths.get(homePath, TRAIN_DATA_PATH, serviceModelCode);
        log.info(">>>>> before Files.createDirectories uploadPath : "+uploadPath);
        Files.createDirectories(uploadPath);
        return FileUploadUtil.uploadFile(trainDataFile, uploadPath + "/");
    }

    private File getClassTxt(String homePath, SttTrainVO sttTrainVO,String serviceModelCode) throws IOException {

        //Files.createDirectories(Paths.get(homePath, "upload"));
    	//File file = Paths.get(homePath, "upload", "class.txt").toFile();
    	Files.createDirectories(Paths.get(homePath));
    	File file = null;
    	if(sttTrainVO.getModelType().equals("CLASS")) {
    		log.info(">>>>>> modelType CLASS Paths.get : "+Paths.get(homePath, "class.txt").toString() );
    		file = Paths.get(homePath, "class.txt").toFile();	
        }else if(sttTrainVO.getModelType().equals("SERVICE")) {
        	log.info(">>>>>> modelType SERVICE Paths.get : "+Paths.get(homePath, "e2elm.txt").toString() );
        	file = Paths.get(homePath, "service.txt").toFile();
        }else if(sttTrainVO.getModelType().equals("E2ELM")) {
        	log.info(">>>>>> modelType E2ELM Paths.get : "+Paths.get(homePath, "e2elm.txt").toString() );
        	file = Paths.get(homePath, "e2elm.txt").toFile();
        }
    	

        SttTrainDataSearchCondition searchCondition = SttTrainDataSearchCondition.builder()
                .serviceModelId(serviceModelCode)
                .build();
        int listSize = getTrainDataCountS(serviceModelCode);
        int pageNum = 1;

        while (listSize > 0) {
            PageHelper.startPage(pageNum, MAX_PAGE_SIZE);
            Page<SttTrainDataVO> sttTrainDataVOs = sttTrainDataService.listPage(searchCondition);

            try (FileOutputStream fos = new FileOutputStream(file);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 BufferedWriter writer = new BufferedWriter(osw)) {
                for (SttTrainDataVO sttTrainDataVO : sttTrainDataVOs) {
                    for (int i = 0; i < sttTrainDataVO.getRepeatCount(); i++) {
                        writer.append(sttTrainDataVO.getContents());
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                log.error("[SttTrainApiController.getClassTxt] ERROR {}", e.getMessage());
            }

            pageNum += 1;
            listSize -= MAX_PAGE_SIZE;
        }

        return file;
    }

    private MultipartFile convertFileToMultipartFile(File file) {
        FileItem fileItem = null;

        try {
            fileItem = new DiskFileItem("file"
                    , Files.probeContentType(file.toPath())
                    , false
                    , file.getName()
                    , (int) file.length(),
                    file.getParentFile());

            try (
                    InputStream is = new FileInputStream(file);
                    OutputStream os = fileItem.getOutputStream()
            ) {
                IOUtils.copy(is, os);
            } catch (Exception e) {
                log.error("[convertFileToMultipartFile] error {}", e.getMessage());
            }
        } catch (Exception e) {
            log.error("[convertFileToMultipartFile] error {}", e.getMessage());
        }

        return new CommonsMultipartFile(fileItem);
    }

    private void deleteTrainDataFromNas(SttTrainVO sttTrainVO) {
        try {
            File file = new File(sttTrainVO.getTrainDataPath());

            if (file.exists()) {
                FileUtil.deleteFile(file);
            }
        } catch (Exception e) {
            log.error("[SttTrainApiController.deleteTrainDataFromNas] {}", e.getMessage());
        }
    }

    private void updateLocalSttTrainVO(SttTrainVO sttTrainVO) {
        sttTrainVO.setResultCode("0000");
        sttTrainVO.setStatus("COMPLETE");
        sttTrainVO.setModelType(sttTrainVO.getModelType());
        sttTrainVO.setModelPath("/nas/model/svc/2/model_2.tar.gz");
        sttTrainVO.setModelAuthKey("83df95a61918148768b7c09019dd09eb");
        LocalDateTime currentTime = LocalDateTime.now();
        String formatedNow = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        sttTrainVO.setRequestedAt(formatedNow);
        
        sttTrainService.update(sttTrainVO);
    }

    private SttTrainCallbackResponseDto getSuccessResponse() {
        return SttTrainCallbackResponseDto.builder()
                .resultCode(ResultCode.SUCCESS.getCode())
                .resultMsg(ResultCode.SUCCESS.getDescription())
                .build();
    }

    private SttTrainCallbackResponseDto getDBFailResponse() {
        return SttTrainCallbackResponseDto.builder()
                .resultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode())
                .resultMsg(NO_TRAIN_VO_IN_DB_MESSAGE)
                .build();
    }

    private String getHomePath(HttpServletRequest request) {

        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        ConfigDto config = configService.getByProjectCode(header.getProjectCode());
        // 현재 KT경로에 맞게 임의 수정, 거기서는 프로젝트코드명 폴더 미사용
        return directoryHome;
    }

}
