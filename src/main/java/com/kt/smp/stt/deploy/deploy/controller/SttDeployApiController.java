package com.kt.smp.stt.deploy.deploy.controller;

import static com.kt.smp.stt.common.ResponseMessage.DEPLOY_REQUEST_SUCCESS_MESSAGE;
import static com.kt.smp.stt.common.ResponseMessage.DESCRIPTION_AT_MOST_MESSAGE;
import static com.kt.smp.stt.common.ResponseMessage.DETAIL_FAIL_MESSAGE;
import static com.kt.smp.stt.common.ResponseMessage.NO_DEPLOY_VO_IN_DB_MESSAGE;
import static com.kt.smp.stt.common.ResponseMessage.STATUS_INQUIRY_FAIL_MESSAGE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.common.dto.BaseResponseDto;
import com.kt.smp.common.util.ExternalApiRequester;
import com.kt.smp.config.CommonConstants;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.stt.comm.preference.dto.AgentConfigDto;
import com.kt.smp.stt.comm.preference.service.PreferenceService;
import com.kt.smp.stt.common.CallbackUrlResolver;
import com.kt.smp.stt.common.EngineUrlResolver;
import com.kt.smp.stt.common.ResultCode;
import com.kt.smp.stt.common.component.SttCmsResultStatus;
import com.kt.smp.stt.common.dto.BaseResultDto;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployCallbackRequestDto;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployCallbackResponseDto;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployMngSearchCondition;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployMngVO;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployRequestDto;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployStatus;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployStatusResponseDto;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployStatusType;
import com.kt.smp.stt.deploy.deploy.dto.SttDeploySearchResponseDto;
import com.kt.smp.stt.deploy.deploy.service.SttDeployMngService;
import com.kt.smp.stt.deploy.model.domain.SttDeployModelVO;
import com.kt.smp.stt.deploy.model.service.SttDeployModelService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/deploy/api")
@Tag(name = "STT 배포 이력 관리", description = "STT 배포 이력 관리 API")
public class SttDeployApiController {

    private final static String REG_DT_DESC = "REG_DT DESC";

    private static final String CORE_STT_DEPLOY_URL = "/stt/deploy";
    
    private static final String CORE_STT_DEPLOY_MUL_URL = "/stt/deploy/multipart";

    private static final String STT_DEPLOY_STATUS_URL = "/stt/deploy/status?serviceCode=";

    private final RestTemplate restTemplate;

    private final EngineUrlResolver engineUrlResolver;

    private final SttDeployMngService sttDeployMngService;

    private final SttDeployModelService sttDeployModelService;

    private final CallbackUrlResolver callbackUrlResolver;
    
    private final ExternalApiRequester externalRequestUtil;
    
    private final PreferenceService preferenceService;

    @Value("${spring.profiles.active}")
    private String profile;

    @SmpServiceApi(name = "배포 내역 검색", method = RequestMethod.GET, path = "/listPage", type = "검색", description = "배포 내역 검색")
    public ResponseEntity<SttDeploySearchResponseDto> listPage(
            @ModelAttribute SttDeployMngSearchCondition searchCondition) {
        searchCondition.setOrderBy(REG_DT_DESC);

        PageHelper.startPage(searchCondition.getPage(), searchCondition.getPageSize(), REG_DT_DESC);
        Page<SttDeployMngVO> page = sttDeployMngService.list(searchCondition);
        SttDeploySearchResponseDto searchResponseDto = new SttDeploySearchResponseDto(page);

        return ResponseEntity.ok(searchResponseDto);
    }
    
    @SmpServiceApi(name = "배포대상 서버 검색", method = RequestMethod.GET, path = "/deployTarget", type = "검색", description = "배포대상 서버검색")
    public ResponseEntity<BaseResponseDto<Object>> deployTarget(HttpServletRequest request ) {
    	BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
    	
        try {
        	String projectCode = request.getHeader(CommonConstants.X_SMP_PROJECT_CODE);
        	String deployServer1 = engineUrlResolver.resolve(projectCode);
            String deployServer2 = engineUrlResolver.resolveSub(projectCode);
            SttDeployMngVO resultDeployTarget = new SttDeployMngVO();
            resultDeployTarget.setDeployTarget(deployServer1);
            resultDeployTarget.setDeployTargetSub(deployServer2);
            responseDto.setResult(resultDeployTarget);
            
        } catch (Exception e) {
            log.error("[ERROR] detail : {}", e.getMessage());
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(DETAIL_FAIL_MESSAGE);
        }

        return ResponseEntity.ok(responseDto);
    }

    @SmpServiceApi(name = "배포 내역 상세조회", method = RequestMethod.GET, path = "/{deployId}", type = "상세조회", description = "배포 내역 상세 조회")
    public ResponseEntity<BaseResponseDto<Object>> detail(@PathVariable(name = "deployId") Long deployId) {
        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);

        try {
            SttDeployMngVO detail = sttDeployMngService.detail(deployId);
            responseDto.setResult(detail);
        } catch (Exception e) {
            log.error("[ERROR] detail : {}", e.getMessage());
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(DETAIL_FAIL_MESSAGE);
        }

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 설명 255자 이하여야 등록 가능
     *
     * 엔진에 배포 api 요청
     * 1. 서비스 모델 코드
     * 2. 결과 모델 NAS 경로
     * 3. 결과 모델 파일 검증을 위한 MD5 해시키
     * 4. 배포 결과를 전달 받을 콜백 url
     * 5. LM Type (현재는 CLASS로 고정)
     *
     * @param request
     * @param sttDeployMngVO
     * @return
     * @throws IOException
     */
    @SmpServiceApi(name = "배포 등록", method = RequestMethod.POST, path = "/insert", type = "등록", description = "배포 등록")
    public ResponseEntity<BaseResponseDto<Object>> insert(HttpServletRequest request,
                                                          @RequestBody SttDeployMngVO sttDeployMngVO) throws IOException {
    	AgentConfigDto agentConfig = preferenceService.getAgentConfigValues();

    	BaseResponseDto<Object> responseDto = null;

    	
    	if (sttDeployMngVO.getDeployTarget().equals("host")) {
    		
    		String coreUrl = engineUrlResolver.resolve();
    		sttDeployMngVO.setDeployTargetServer(coreUrl);
    		if ("Y".equals(agentConfig.getMultipartHostDeploy())) {
    	    	responseDto = insertDeployMultipart(request, sttDeployMngVO, coreUrl);
    	    	
    		} else {
    			responseDto = insertDeploy(request, sttDeployMngVO, coreUrl);
    		}
    		
    	} else {
    		
    		String coreUrl = engineUrlResolver.resolveSub();
    		sttDeployMngVO.setDeployTargetServer(coreUrl);
    		if ("Y".equals(agentConfig.getMultipartSubDeploy())) {
    	    	responseDto = insertDeployMultipart(request, sttDeployMngVO, coreUrl);
    		} else {
    			responseDto = insertDeploy(request, sttDeployMngVO, coreUrl);
    		}
    	}

        return ResponseEntity.ok(responseDto);
    }

	private BaseResponseDto<Object> insertDeploy(HttpServletRequest request, SttDeployMngVO sttDeployMngVO, String hostUrl) {
		BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        responseDto.setResultMsg(DEPLOY_REQUEST_SUCCESS_MESSAGE);
        //String serviceModelCode = serviceModelService.detail(sttDeployMngVO.getServiceModelId()).getServiceCode();
        String serviceModelCode = sttDeployMngVO.getServiceModelId()+"";
        //SttTrainVO resultTrainVO = sttTrainService.detailLastOne(serviceModelCode);
        SttDeployModelVO resultModelVO = sttDeployModelService.detailByResultModelId(sttDeployMngVO.getResultModelId());
    	
        if (StringUtils.isNotEmpty(sttDeployMngVO.getDescription())
                && sttDeployMngVO.getDescription().length() > 255) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(DESCRIPTION_AT_MOST_MESSAGE);

            return responseDto;
        }
        
        if (!profile.equals(CommonConstants.LOCAL_PROFILE)) {
            BaseResultDto sttDeployResponseDto = callCoreDeployApi(request, sttDeployMngVO, resultModelVO.getModelType(), hostUrl);

            String resultDescription = ResultCode.findByCode(sttDeployResponseDto.getResultCode()).getDescription();
            
            responseDto.setResultCode(sttDeployResponseDto.getResultCode());
            responseDto.setResultMsg(resultDescription);
            if (ObjectUtils.isEmpty(sttDeployResponseDto)) {
                responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
                responseDto.setResultMsg(ResultCode.INTERNAL_SERVER_ERROR.getDescription());
            }else if(!sttDeployResponseDto.getResultCode().equals(ResultCode.SUCCESS.getCode())) {
            	responseDto.setResultCode(sttDeployResponseDto.getResultCode());
                responseDto.setResultMsg(resultDescription);
            }
            insertSttDeployMngVO(request, sttDeployMngVO);
        } else {
            insertSttDeployMngVO(request, sttDeployMngVO);
            SttDeployMngVO newSttDeployMngVO = sttDeployMngService.detailByResultModelId(sttDeployMngVO.getResultModelId());
            updateLocalSttDeployMngVO(newSttDeployMngVO);
        }
		return responseDto;
	}
    
    @SmpServiceApi(name = "배포 등록(NAS미사용)", method = RequestMethod.POST, path = "/deployMultipart", type = "등록", description = "배포 등록(NAS미사용)")
    public ResponseEntity<BaseResponseDto<Object>> deployMultipart(HttpServletRequest request,@RequestBody SttDeployMngVO sttDeployMngVO) throws IOException {
        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);

        return ResponseEntity.ok(responseDto);
    }

	private BaseResponseDto<Object> insertDeployMultipart(HttpServletRequest request, SttDeployMngVO sttDeployMngVO,
			String hostUrl) {
		BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
		responseDto.setResultMsg(DEPLOY_REQUEST_SUCCESS_MESSAGE);
    	//고정값 modelType CLASS 가 아닌 학습등록시 입력한 값 가져오기
        //String serviceModelCode = serviceModelService.detail(sttDeployMngVO.getServiceModelId()).getServiceCode();
        String serviceModelCode = sttDeployMngVO.getServiceModelId()+"";
        //SttTrainVO resultTrainVO = sttTrainService.detailLastOne(serviceModelCode);
        SttDeployModelVO resultModelVO = sttDeployModelService.detailByResultModelId(sttDeployMngVO.getResultModelId());
    	String deployModelPath = sttDeployModelService.getModelPath(sttDeployMngVO.getResultModelId());
    	log.info(">>>>> deployModelPath : "+deployModelPath);
    	File file = new File(deployModelPath);
    	
        if (StringUtils.isNotEmpty(sttDeployMngVO.getDescription())
                && sttDeployMngVO.getDescription().length() > 255) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(DESCRIPTION_AT_MOST_MESSAGE);

            return responseDto;
        }
        
        if (!profile.equals(CommonConstants.LOCAL_PROFILE)) {
        	try {
        		BaseResultDto apiDeployMultipartResult = callCoreDeployApiMultipart(request, sttDeployMngVO, resultModelVO.getModelType(), file, hostUrl);;
        		String resultDescription = ResultCode.findByCode(apiDeployMultipartResult.getResultCode()).getDescription();
                
                responseDto.setResultCode(apiDeployMultipartResult.getResultCode());
                responseDto.setResultMsg(resultDescription);
                
                if(apiDeployMultipartResult.getResultCode().equals(ResultCode.SUCCESS.getCode())) {
                    insertSttDeployMngVO(request, sttDeployMngVO);
                }
                
        	} catch(IOException e) {
        		responseDto.setResultCode(SttCmsResultStatus.NO_FILE.getResultCode());
        		responseDto.setResultMsg(SttCmsResultStatus.NO_FILE.getDescription());
        	}

        } else {
            insertSttDeployMngVO(request, sttDeployMngVO);
            SttDeployMngVO newSttDeployMngVO = sttDeployMngService.detailByResultModelId(sttDeployMngVO.getResultModelId());
            updateLocalSttDeployMngVO(newSttDeployMngVO);
        }
        return responseDto;
	}

    /**
     * 배포와 같은 프로세스
     * 학습과 달리 재배포는 최근 배포 상태와 상관없이 요청 가능
     *
     * @param request
     * @param sttDeployMngVO
     * @return
     * @throws IOException
     */
    @SmpServiceApi(name = "재배포", method = RequestMethod.POST, path = "/update", type = "재배포", description = "재배포 등록")
    public ResponseEntity<BaseResponseDto<Object>> update(HttpServletRequest request,
                                                          @RequestBody SttDeployMngVO sttDeployMngVO) throws IOException {
        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
    	AgentConfigDto agentConfig = preferenceService.getAgentConfigValues();

    	if (sttDeployMngVO.getDeployTarget().equals("host")) {
    		
    		String coreUrl = engineUrlResolver.resolve();
    		sttDeployMngVO.setDeployTargetServer(coreUrl);
    		if ("Y".equals(agentConfig.getMultipartHostDeploy())) {
    	    	responseDto = updateDeployMultipart(request, sttDeployMngVO, coreUrl);

    		} else {
    			responseDto = updateDeploy(request, sttDeployMngVO, coreUrl);
    		}
    		
    	} else {
    		
    		String coreUrl = engineUrlResolver.resolveSub();
    		sttDeployMngVO.setDeployTargetServer(coreUrl);
    		if ("Y".equals(agentConfig.getMultipartSubDeploy())) {
    	    	responseDto = updateDeployMultipart(request, sttDeployMngVO, coreUrl);
    		} else {
    			responseDto = updateDeploy(request, sttDeployMngVO, coreUrl);

    		}
    	}

        return ResponseEntity.ok(responseDto);
    }

	private BaseResponseDto<Object> updateDeploy(HttpServletRequest request, SttDeployMngVO sttDeployMngVO,
			 String hostUrl) {
		BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
		responseDto.setResultMsg(DEPLOY_REQUEST_SUCCESS_MESSAGE);
        //고정값 modelType CLASS 가 아닌 학습등록시 입력한 값 가져오기
        //String serviceModelCode = serviceModelService.detail(sttDeployMngVO.getServiceModelId()).getServiceCode();
        String serviceModelCode = sttDeployMngVO.getServiceModelId()+"";
        //SttTrainVO resultTrainVO = sttTrainService.detailLastOne(serviceModelCode);
        SttDeployModelVO resultModelVO = sttDeployModelService.detailByResultModelId(sttDeployMngVO.getResultModelId());
        
        if (StringUtils.isNotEmpty(sttDeployMngVO.getDescription())
                && sttDeployMngVO.getDescription().length() > 255) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(DESCRIPTION_AT_MOST_MESSAGE);

            return responseDto;
        }

        if (!profile.equals(CommonConstants.LOCAL_PROFILE)) {
            BaseResultDto sttDeployResponseDto = callCoreDeployApi(request, sttDeployMngVO, resultModelVO.getModelType(), hostUrl);
            
            String resultDescription = ResultCode.findByCode(sttDeployResponseDto.getResultCode()).getDescription();
            responseDto.setResultCode(sttDeployResponseDto.getResultCode());
            responseDto.setResultMsg(resultDescription);
            
            if(sttDeployResponseDto.getResultCode().equals(ResultCode.SUCCESS.getCode())) {
                insertSttDeployMngVO(request, sttDeployMngVO);
            }
        } else {
            SttDeployMngVO newSttDeployMngVO = sttDeployMngService.detailByResultModelId(sttDeployMngVO.getResultModelId());

            updateLocalSttDeployMngVO(newSttDeployMngVO);
        }
        
        return responseDto;
	}
    
    @SmpServiceApi(name = "재배포(NAS미사용)", method = RequestMethod.POST, path = "/updateMultipart", type = "재배포", description = "재배포(NAS미사용)")
    public ResponseEntity<BaseResponseDto<Object>> updateMultipart(HttpServletRequest request,
                                                          @RequestBody SttDeployMngVO sttDeployMngVO) throws IOException {

        return ResponseEntity.ok(null);
    }

	private BaseResponseDto<Object> updateDeployMultipart(HttpServletRequest request, SttDeployMngVO sttDeployMngVO, String hostUrl) {
		BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        responseDto.setResultMsg(DEPLOY_REQUEST_SUCCESS_MESSAGE);
        //고정값 modelType CLASS 가 아닌 학습등록시 입력한 값 가져오기
        //String serviceModelCode = serviceModelService.detail(sttDeployMngVO.getServiceModelId()).getServiceCode();
        String serviceModelCode = sttDeployMngVO.getServiceModelId()+"";
        //SttTrainVO resultTrainVO = sttTrainService.detailLastOne(serviceModelCode);
        SttDeployModelVO resultModelVO = sttDeployModelService.detailByResultModelId(sttDeployMngVO.getResultModelId());
        String deployModelPath = sttDeployModelService.getModelPath(sttDeployMngVO.getResultModelId());
    	log.info(">>>>> deployModelPath : "+deployModelPath);
    	File file = new File(deployModelPath);
        
        if (StringUtils.isNotEmpty(sttDeployMngVO.getDescription())
                && sttDeployMngVO.getDescription().length() > 255) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(DESCRIPTION_AT_MOST_MESSAGE);

            return responseDto;
        }

        if (!profile.equals(CommonConstants.LOCAL_PROFILE)) {
			try {
				BaseResultDto sttDeployResponseDto = callCoreDeployApiMultipart(request, sttDeployMngVO, resultModelVO.getModelType(), file, hostUrl);
				String resultDescription = ResultCode.findByCode(sttDeployResponseDto.getResultCode()).getDescription();

	            responseDto.setResultCode(sttDeployResponseDto.getResultCode());
	            responseDto.setResultMsg(resultDescription);
	            
	            if (sttDeployResponseDto.getResultCode().equals(ResultCode.SUCCESS.getCode())) {
	                insertSttDeployMngVO(request, sttDeployMngVO);
	            }
			} catch (IOException e) {
				responseDto.setResultCode(SttCmsResultStatus.NO_FILE.getResultCode());
        		responseDto.setResultMsg(SttCmsResultStatus.NO_FILE.getDescription());
			}
            
            	
        } else {
            SttDeployMngVO newSttDeployMngVO = sttDeployMngService.detailByResultModelId(sttDeployMngVO.getResultModelId());
        	log.info(">>>>>> before updateLocalSttDeployMngVO {} ", newSttDeployMngVO);
            updateLocalSttDeployMngVO(newSttDeployMngVO);
        }
		return responseDto;
	}

    /**
     * 엔진에 배포 상태 조회 api 호출 후 상태를 반환
     *
     * @param id
     * @return
     */
    @SmpServiceApi(name = "배포 상태 조회", method = RequestMethod.GET, path = "/status", type = "조회", description = "배포 상태 조회")
    public ResponseEntity<BaseResponseDto<Object>> status(HttpServletRequest request, @RequestParam Long id) {
        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);

        if (profile.equals(CommonConstants.LOCAL_PROFILE)) {
            responseDto.setResult("test");

            return ResponseEntity.ok(responseDto);
        }

        SttDeployMngVO sttDeployMngVO = sttDeployMngService.detail(id);

        if (org.apache.commons.lang3.ObjectUtils.isEmpty(sttDeployMngVO)) {
            throw new NoSuchElementException(NO_DEPLOY_VO_IN_DB_MESSAGE);
        }

        //String serviceModelCode = serviceModelService.detail(sttDeployMngVO.getServiceModelId()).getServiceCode();
        String serviceModelCode = sttDeployMngVO.getServiceModelId()+"";
        
        String finalDeployServer = sttDeployMngVO.getDeployUrl();
        

        String coreUrl = profile.equals(CommonConstants.LOCAL_PROFILE) ? engineUrlResolver.resolve() : finalDeployServer;
        log.info(">>>>> before deploy status api : "+serviceModelCode);
        if(coreUrl.contains("https")) {
		    ignoreSSL();
		}
        ResponseEntity<SttDeployStatusResponseDto> responseEntity = restTemplate.getForEntity(coreUrl + STT_DEPLOY_STATUS_URL + serviceModelCode, SttDeployStatusResponseDto.class);
        String status = (org.apache.commons.lang3.ObjectUtils.isEmpty(responseEntity) || org.apache.commons.lang3.ObjectUtils.isEmpty(responseEntity.getBody()))
                ? null : responseEntity.getBody().getStatus();
        log.info(">>>>> deploy status : " + status);

        if (status == null) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(STATUS_INQUIRY_FAIL_MESSAGE);

            ResponseEntity.ok(responseDto);
        }

        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(status)
                && !sttDeployMngVO.getStatus().equals(status)) {
            sttDeployMngVO.setStatus(status);
            sttDeployMngService.update(sttDeployMngVO);
        }

        responseDto.setResult(status);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 엔진에 배포/재배포 요청 시 전달한 callback url
     * 배포/재배포 결과를 엔진으로부터 수신
     * 따라서, @SmpServiceApi 어노테이션을 붙이지 않음
     *
     * @param requestDto
     * @return
     */
    @PostMapping("/callback")
    public ResponseEntity<SttDeployCallbackResponseDto> deployApiCallback(@RequestBody SttDeployCallbackRequestDto requestDto) {
        log.info(">>>>>>>> [Callback Stt Deploy] {}", requestDto);
        SttDeployMngVO sttDeployMngVO
                = sttDeployMngService.detailLastOne(requestDto.getServiceCode());

        if (org.apache.commons.lang3.ObjectUtils.isEmpty(sttDeployMngVO)) {
            return ResponseEntity.ok().body(getDBFailResponse());
        }

        updateSttDeployMngVO(requestDto, sttDeployMngVO);

        return ResponseEntity.ok().body(getSuccessResponse());
    }

    private BaseResultDto callCoreDeployApi(HttpServletRequest request, SttDeployMngVO sttDeployMngVO, String modelType, String hostUrl) {
        SttDeployModelVO deployModelVO = sttDeployModelService.detailByResultModelId(sttDeployMngVO.getResultModelId());
        String apiModelAuthKey = "";
        String apiModelPath = "";
    	apiModelAuthKey = deployModelVO.getModelAuthKey();
    	apiModelPath = deployModelVO.getModelPath();
        SttDeployRequestDto requestDto = SttDeployRequestDto.builder()
                .serviceCode(Long.toString(sttDeployMngVO.getServiceModelId()))
                .modelPath(apiModelPath)
                .modelAuthKey(apiModelAuthKey)
                .callbackUrl(callbackUrlResolver.deployCallbackUrl())
                .modelType(modelType)
                .build();
        
        log.info(">>>>>> sttDeployMngVO.getDeployTarget : "+sttDeployMngVO.getDeployTargetServer());
        String finalDeployServer = sttDeployMngVO.getDeployTargetServer();
        
        BaseResultDto responseEntity = null;

        try {
        		log.info(">>>>> before deploy api | requestDto.getCallbackUrl : "+requestDto.getCallbackUrl()+" / requestDto.getModelAuthKey : "+requestDto.getModelAuthKey()
        		+" / requestDto.getModelPath : "+requestDto.getModelPath()+" / requestDto.getModelType : "+requestDto.getModelType());
               
        		responseEntity = externalRequestUtil.requestPostWithSavingError(CORE_STT_DEPLOY_URL, hostUrl, requestDto);
            
        } catch (Exception e) {
            log.error("[callCoreDeployApi ERROR] {}", e.getMessage());
        }

        return responseEntity;
    }
    
    private BaseResultDto callCoreDeployApiMultipart(HttpServletRequest request
    		, SttDeployMngVO sttDeployMngVO
    		, String modelType
    		, File file
    		, String hostUrl) throws IOException{
    	log.info(">>>>>> receive modelType : "+modelType);
    	SttDeployModelVO deployModelVO = sttDeployModelService.detailByResultModelId(sttDeployMngVO.getResultModelId());
        String apiModelAuthKey = "";
        String apiModelPath = "";
    	apiModelAuthKey = deployModelVO.getModelAuthKey();
    	apiModelPath = deployModelVO.getModelPath();
        
        SttDeployRequestDto requestDto = SttDeployRequestDto.builder()
                .serviceCode(Long.toString(sttDeployMngVO.getServiceModelId()))
                .modelAuthKey(apiModelAuthKey)
                .modelType(modelType)
                .callbackUrl(callbackUrlResolver.deployCallbackUrl())
                .build();
        String projectCode = request.getHeader(CommonConstants.X_SMP_PROJECT_CODE);
        
        String deployServer1 = engineUrlResolver.resolve(projectCode);
        String deployServer2 = engineUrlResolver.resolveSub(projectCode);
        String finalDeployServer = sttDeployMngVO.getDeployTargetServer();
        MultipartFile multipartFile = convertFileToMultipartFile(file);
        
        HttpHeaders headers = new HttpHeaders();
        BaseResultDto responseEntity = null;
        HttpEntity<MultiValueMap<String, Object>> requestEntity = null;
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
        	headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            log.info(">>>>>>>> after convert multipartFile byte : "+multipartFile.getBytes());
        	ByteArrayResource fileResource = new ByteArrayResource(multipartFile.getBytes()) {
            	@Override
            	public String getFilename() throws IllegalStateException {
            		return multipartFile.getOriginalFilename();
            	}
            };
            body.add("modelInfoData", requestDto);
            body.add("modelData", fileResource);
            requestEntity = new HttpEntity<>(body, headers);
            
        	log.info(">>>>> before deploy(non nas) api | requestDto.getCallbackUrl : "+requestDto.getCallbackUrl()+
        			" / requestDto.getModelAuthKey : "+requestDto.getModelAuthKey()+" / requestDto.getModelType : "+requestDto.getModelType());
        	String coreUrl = profile.equals(CommonConstants.LOCAL_PROFILE) ? engineUrlResolver.resolve() : finalDeployServer;
        	
        	responseEntity = externalRequestUtil.requestPostWithSavingError(CORE_STT_DEPLOY_MUL_URL, hostUrl, requestEntity);
        	
            log.info(">>>> result deploy api(non nas)" , responseEntity);
        } catch (Exception e) {
            log.error("[callCoreDeployApiMultipart ERROR] {}", e.getMessage());
        }

        return responseEntity;
    }
    
    private MultipartFile convertFileToMultipartFile(File file) throws IOException {
    	FileItem fileItem=null;
		try {
			fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int)file.length(), file.getParentFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try (InputStream is = new FileInputStream(file);
				OutputStream os = fileItem.getOutputStream();){
			IOUtils.copy(is, os);
		} catch (IOException e) {
			throw e;
		}
    	
    	MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
    	log.info("========== return multipartFile ==========");
    	return multipartFile;
    }

    private void insertSttDeployMngVO(HttpServletRequest request, SttDeployMngVO sttDeployMngVO) {

        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        String name = header.getUserId();
        String ip = request.getRemoteAddr();

        sttDeployMngVO.setCreatedBy(name);
        sttDeployMngVO.setRegId(name);
        sttDeployMngVO.setRegIp(ip);
        sttDeployMngVO.setUpdId(name);
        sttDeployMngVO.setUpdIp(ip);
        sttDeployMngVO.setStatus(SttDeployStatusType.DEPLOYING.getCode());
        sttDeployMngVO.setDeployUrl(sttDeployMngVO.getDeployTargetServer());
        log.info(">>>> before insert | sttDeployMngVO.getResultModelId : "+sttDeployMngVO.getResultModelId()+" / sttDeployMngVO.getStatus :"+sttDeployMngVO.getStatus()
        +" / sttDeployMngVO.getDeployList : "+sttDeployMngVO.getDeployList().toString());
        sttDeployMngService.insert(sttDeployMngVO);
    }

    private void updateLocalSttDeployMngVO(SttDeployMngVO sttDeployMngVO) {
        sttDeployMngVO.setResultCode(ResultCode.SUCCESS.getCode());
        sttDeployMngVO.setStatus("COMPLETE");
        sttDeployMngVO.setDeployList(getDummySttDeployStatuses());

        sttDeployMngService.update(sttDeployMngVO);
    }

    private List<SttDeployStatus> getDummySttDeployStatuses() {
        SttDeployStatus status = SttDeployStatus.builder()
                .serverName("sru01")
                .status("complete")
                .build();
        SttDeployStatus status2 = SttDeployStatus.builder()
                .serverName("sru02")
                .status("fail")
                .build();

        List<SttDeployStatus> deployList = Arrays.asList(status, status2);
        return deployList;
    }

    private void updateSttDeployMngVO(SttDeployCallbackRequestDto requestDto, SttDeployMngVO sttDeployMngVO) {
        sttDeployMngVO.setResultCode(requestDto.getResultCode());
        
        sttDeployMngVO.setResultMsg(requestDto.getResultMsg());

        sttDeployMngVO.setStatus(requestDto.getStatus());
        sttDeployMngVO.setDeployList(requestDto.getDeployList());
        
        log.info(">>>> before update | " + sttDeployMngVO.toString());
        sttDeployMngService.update(sttDeployMngVO);
    }

    private SttDeployCallbackResponseDto getSuccessResponse() {
        return SttDeployCallbackResponseDto.builder()
                .resultCode(ResultCode.SUCCESS.getCode())
                .resultMsg(ResultCode.SUCCESS.getDescription())
                .build();
    }

    private SttDeployCallbackResponseDto getDBFailResponse() {
        return SttDeployCallbackResponseDto.builder()
                .resultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode())
                .resultMsg(NO_DEPLOY_VO_IN_DB_MESSAGE)
                .build();
    }
    
    private void ignoreSSL() {

	    TrustManager[] trustAllCerts = new TrustManager[] {
	        new X509TrustManager() {

	            @Override
	            public void checkClientTrusted(X509Certificate[] chain, String authType) {

	            }

	            @Override
	            public void checkServerTrusted(X509Certificate[] chain, String authType) {

	            }

	            @Override
	            public X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	        }
	    };

	    try {
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	    } catch (Exception e) {
	        log.error("[ERROR] ignoreSSL : {}", e.getMessage());
	    }

	}
}
