package com.kt.smp.stt.test.controller;

import static com.kt.smp.stt.common.ResponseMessage.FILE_TOO_BIG_MESSAGE;
import static com.kt.smp.stt.common.ResponseMessage.FILE_TYPE_MISMATCH_MESSAGE;
import static com.kt.smp.stt.common.ResponseMessage.FILE_UPLOAD_FAIL_MESSAGE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.common.dto.BaseResponseDto;
import com.kt.smp.common.util.ExternalApiRequester;
import com.kt.smp.common.util.FileUtil;
import com.kt.smp.common.util.JacksonUtil;
import com.kt.smp.config.CommonConstants;
import com.kt.smp.fileutil.constant.ExcelConstants;
import com.kt.smp.fileutil.dto.UploadFileResponseDto;
import com.kt.smp.fileutil.util.FileUploadUtil;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.comm.directory.service.HomePathResolver;
import com.kt.smp.stt.comm.preference.dto.AgentConfigDto;
import com.kt.smp.stt.comm.preference.service.PreferenceService;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.common.CallbackUrlResolver;
import com.kt.smp.stt.common.EngineUrlResolver;
import com.kt.smp.stt.common.ErrorTypeCode;
import com.kt.smp.stt.common.ResultCode;
import com.kt.smp.stt.common.component.SttCmsResultStatus;
import com.kt.smp.stt.common.dto.BaseResultDto;
import com.kt.smp.stt.error.service.SttErrorService;
import com.kt.smp.stt.test.domain.SttTestCallbackResponseDto;
import com.kt.smp.stt.test.domain.SttTestCallbackResult;
import com.kt.smp.stt.test.domain.SttTestCallbackVO;
import com.kt.smp.stt.test.domain.SttTestMultipartRequestDto;
import com.kt.smp.stt.test.domain.SttTestRequestDto;
import com.kt.smp.stt.test.domain.SttTestResponseDto;
import com.kt.smp.stt.test.domain.SttTestResult;
import com.kt.smp.stt.test.domain.SttTestVO;
import com.kt.smp.stt.test.service.SttTestService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/sttTest/api")
@Tag(name = "STT 단건 테스트", description = "STT 단건 테스트 API")
public class SttTestApiController {

    private static long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private static final String CORE_TEST_URL = "/stt/test";
    
    private static final String CORE_TEST_MULTIPART_URL = "/stt/test/multipart";

    private static final String SINGLE_ACTION_TEST_PATH = "/singleActionTest/svc/";

    private final SttTestService sttTestService;
    
    private final ServiceModelService serviceModelService;

    private final HomePathResolver homePathResolver;

    private final ConfigService configService;

    private final CallbackUrlResolver callbackUrlResolver;
    
    private final ExternalApiRequester externalRequestUtil;
    
    private final EngineUrlResolver engineUrlResolver;
    
	private final PreferenceService preferenceService;

    @Value("${spring.profiles.active}")
    private String profile;

    /**
     *
     * wav file 규격 제한
     * 1. 10MB 이하
     * 2. 16bit 8000Hz mono
     *
     * wav file 규격이 맞을 경우 api 요청
     * 1. 서비스모델 코드
     * 2. wav 파일이 저장된 nas 경로
     * 3. 콜백 api를 받을 url 주소
     *
     * 콜백 응답이 올 때까지 client에서는 2초마다 polling
     * 응답이 오면 nas 내 wav 파일을 제거하고 응답을 내려줌
     *
     * 서버 오류 혹은 클라이언트가 새로고침하여 polling 응답을 못 받을 경우를 대비하여
     * 세션에 wav 파일 경로를 저장하고 세션 타임아웃 시 삭제하도록 처리
     *
     * @param request
     * @param modelFile
     * @param sttTestVO
     * @return
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    @SmpServiceApi(name = "단건 테스트 등록", method = RequestMethod.POST
            , path = "/insert", type = "등록"
            , description = "단건 테스트 등록", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponseDto<Object>> insert(HttpServletRequest request,
                                                          @RequestPart("modelFile") MultipartFile modelFile,
                                                          @RequestPart("properties") SttTestVO sttTestVO)
            throws IOException, UnsupportedAudioFileException {
    	
    	AgentConfigDto agentConfig = preferenceService.getAgentConfigValues();
    	BaseResponseDto<Object> responseDto = null;
    	if (sttTestVO.getTestTarget().equals("host")) {
    		
    		String coreUrl = engineUrlResolver.resolve();
    		
    		if ("Y".equals(agentConfig.getMultipartHostTest())) {
    	    	responseDto = insertMultipartTest(modelFile, sttTestVO, coreUrl);
    		} else {
    			responseDto = insertTest(modelFile, sttTestVO, coreUrl);
    		}
    		
    	} else {
    		
    		String coreUrl = engineUrlResolver.resolveSub();

    		if ("Y".equals(agentConfig.getMultipartSubTest())) {
    	    	responseDto = insertMultipartTest(modelFile, sttTestVO, coreUrl);
    		} else {
    			responseDto = insertTest(modelFile, sttTestVO, coreUrl);
    		}
    	}

        return ResponseEntity.ok(responseDto);
    }

	private BaseResponseDto<Object> insertTest(MultipartFile modelFile, SttTestVO sttTestVO, String coreUrl)
			throws IOException, UnknownHostException {
		BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);

		if (modelFile.getSize() > MAX_FILE_SIZE) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(FILE_TOO_BIG_MESSAGE);
            return responseDto;
        }

        if (!isWavFileCorrect(modelFile)) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(FILE_TYPE_MISMATCH_MESSAGE);
            return responseDto;
        }

        UploadFileResponseDto fileResponseDto = uploadWavFile(modelFile, sttTestVO.getServiceCode());

        if (org.apache.commons.lang3.ObjectUtils.isEmpty(fileResponseDto)
                || org.apache.commons.lang3.ObjectUtils.isEmpty(fileResponseDto.isUploadSuccess())
                || !fileResponseDto.isUploadSuccess()) {
        	responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResult(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(FILE_UPLOAD_FAIL_MESSAGE);

            return responseDto;
        }

        String filePath = fileResponseDto.getFilePath();
        log.info(">>> filePath : " + filePath);
        //saveFilePath(request, filePath);
        if (!profile.equals(CommonConstants.LOCAL_PROFILE)) {
            	BaseResultDto sttTestResultDto
                = requestSttTestResponseDtoResponseEntity(sttTestVO, fileResponseDto, coreUrl);
		        
            	responseDto.setResultCode(sttTestResultDto.getResultCode());
            	responseDto.setResultMsg(sttTestResultDto.getResultMsg());

	            log.info(">> Request Test Response : " +  sttTestResultDto.toString());
        } else {
            createDummySttTestResult(sttTestVO);
        }
		return responseDto;
	}

	private BaseResponseDto<Object> insertMultipartTest(MultipartFile modelFile, SttTestVO sttTestVO, String coreUrl)
			throws IOException, UnknownHostException {
		List<ConfigDto> configDtos = configService.getAllUserDefined();
    	for (ConfigDto configDto : configDtos) {
    		TenantContextHolder.set(configDto.getProjectCode());	
    	}
    	
    	BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);

    	if (isInvalidFile(modelFile, responseDto)) {
    		return responseDto;
    	};

    	if (!profile.equals(CommonConstants.LOCAL_PROFILE)) {
    		BaseResultDto sttTestResultDto
            = requestSttTestMultipartResponseDtoResponseEntity(sttTestVO, modelFile, coreUrl);
	        
        	responseDto.setResultCode(sttTestResultDto.getResultCode());
        	responseDto.setResultMsg(sttTestResultDto.getResultMsg());
        } else {
            createDummySttTestResult(sttTestVO);
        }
		return responseDto;
	}
    
    @SmpServiceApi(name = "단건 테스트 폴링", method = RequestMethod.GET, path = "/poll/{uuid}", type = "조회", description = "단건 테스트 폴링")
    public ResponseEntity<BaseResponseDto<Object>> pollResponse(@PathVariable("uuid") String uuid) {
    	BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
    	SttTestCallbackVO requestDto = sttTestService.detail(uuid);
    	
    	if (ObjectUtils.isEmpty(requestDto) ) {
    		responseDto.setResultCode(ResultCode.FILE_NOT_FOUND.getCode());
    		
    		return ResponseEntity.ok(responseDto);
    	} else {
    		sttTestService.delete(uuid);
    	}
    	
    	responseDto.setResult(SttTestCallbackResult.builder()
    	.resultMsg(ResultCode.findByCode(requestDto.getResultCode()).getDescription())
    	.serviceCode(serviceModelService.detailByServiceCode(requestDto.getServiceCode()).getServiceModelName())
    	.sttResult(requestDto.getSttResult())
    	.build());
    	
    	return ResponseEntity.ok(responseDto);
    }

    @PostMapping(value = "/callback/{uuid}")
    public ResponseEntity<SttTestCallbackResponseDto> callback(@PathVariable String uuid
            , @RequestBody SttTestCallbackVO requestDto) {
        log.info("[Callback SttTest] {}", requestDto);

        requestDto.setUuid(uuid);
        sttTestService.insert(requestDto);

        return ResponseEntity.ok().body(getSuccessResponse());
    }

    private SttTestCallbackResponseDto getSuccessResponse() {
        return SttTestCallbackResponseDto.builder()
                .resultCode(ResultCode.SUCCESS.getCode())
                .resultMsg(ResultCode.SUCCESS.getDescription())
                .build();
    }

    private UploadFileResponseDto uploadWavFile(MultipartFile modelFile
            , String serviceModelCode ) throws IOException {

        String homePath = homePathResolver.resolveSuhyup();

        String path = homePath + SINGLE_ACTION_TEST_PATH + serviceModelCode + "/";
        log.info(">> uploadWavFile path : " + path);
        log.info(">> modelFile : " + modelFile.getOriginalFilename());
        Files.createDirectories(Paths.get(path));
        
        return FileUploadUtil.uploadFile(modelFile, path);
    }

    private BaseResultDto requestSttTestResponseDtoResponseEntity(
            SttTestVO sttTestVO
            , UploadFileResponseDto uploadFileResponseDto
            , String coreUrl) throws UnknownHostException {
        
        SttTestRequestDto sttTestRequestDto = SttTestRequestDto.builder()
                .serviceCode(sttTestVO.getServiceCode())
                .testWavPath(uploadFileResponseDto.getFilePath())
                .callbackUrl(callbackUrlResolver.testCallbackUrl(sttTestVO.getUuid()))
                .build();
        
        BaseResultDto sttTestResultDto = null;
        
        try {
            sttTestResultDto = externalRequestUtil.requestPostWithSavingError(CORE_TEST_URL, coreUrl, sttTestRequestDto);
            
        } catch (Exception e) {
            log.error("[SttTestController.testRestTemplate] ERROR {}", e.getMessage());
            throw e;
        }

        return sttTestResultDto;
    }
    
    private BaseResultDto requestSttTestMultipartResponseDtoResponseEntity(
            SttTestVO sttTestVO
            , MultipartFile file
            , String coreUrl) throws UnknownHostException, IOException {

    	HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

		SttTestMultipartRequestDto sttTestMultipartRequestDto = SttTestMultipartRequestDto.builder()
				.serviceCode(sttTestVO.getServiceCode())
				.callbackUrl(callbackUrlResolver.testCallbackUrl(sttTestVO.getUuid()))
				.build();
		
		log.info("callback url >> " + callbackUrlResolver.testCallbackUrl(sttTestVO.getUuid()));
		
		ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
			@Override
			public String getFilename() {
				return file.getOriginalFilename();
			}
		}; 
		
		body.add("testInfoData", sttTestMultipartRequestDto);
		body.add("testData", fileResource);
		
		BaseResultDto testResultDto = null;
		HttpEntity<?> uploadEntity = new HttpEntity<>(body, headers);

        try {
        	testResultDto = externalRequestUtil.requestPostWithSavingError(CORE_TEST_MULTIPART_URL, coreUrl, uploadEntity);
        } catch (Exception e) {
            log.error("[SttTestController.testRestTemplate] ERROR {}", e.getMessage());
            throw e;
        }

        return testResultDto;
    }

    private void createDummySttTestResult(SttTestVO sttTestVO) {
        List<SttTestResult> sttTestResults = Arrays.asList(new SttTestResult("안녕하세요. 반갑습니다", "0.890857")
                , new SttTestResult("여보세요 네 여보세요", "0.8903757")
                , new SttTestResult("데리고 오겠습니다 네네", "0.926800"));

        SttTestCallbackVO requestDto = SttTestCallbackVO.builder()
                .resultCode("0000")
                .resultMsg("성공")
                .serviceCode("2")
                .sttResult(sttTestResults)
                .build();
        requestDto.setUuid(sttTestVO.getUuid());

        sttTestService.insert(requestDto);
    }

    private boolean isSttTestResponseEntityEmpty(ResponseEntity<SttTestResponseDto> sttTestResponseEntity) {
        return ObjectUtils.isEmpty(sttTestResponseEntity)
                || ObjectUtils.isEmpty(sttTestResponseEntity.getBody());
    }
    
    private void saveFilePath(HttpServletRequest request, String filePath) {
        HttpSession session = request.getSession();
        Set<String> filePaths = (Set<String>) session.getAttribute(ExcelConstants.SESSION_KEY);

        if (org.apache.commons.lang3.ObjectUtils.isEmpty(filePaths)) {
            filePaths = new HashSet<>();
        }

        filePaths.add(filePath);
        session.setAttribute(ExcelConstants.SESSION_KEY, filePaths);
    }

    private boolean isWavFileCorrect(MultipartFile file) throws IOException {
        File tempFile = new File(file.getOriginalFilename());
        tempFile.createNewFile();

        try (
                FileOutputStream fos = new FileOutputStream(tempFile);
        ) {
            fos.write(file.getBytes());
        } catch (Exception e) {
            log.error("[convert] ERROR {}", e.getMessage());
        }

        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(tempFile);

            if (ObjectUtils.isEmpty(fileFormat)) {
                log.error("[isWavFileCorrect] can't access to AudioFileFormat");

                return false;
            }

            int sampleSizeInBits = fileFormat.getFormat().getSampleSizeInBits();
            float sampleRate = fileFormat.getFormat().getSampleRate();

            log.info("[isWavFileCorrect] sampleSizeInBits: {}, sampleRate: {}, fileFormat: {}"
                    , sampleSizeInBits, sampleRate, fileFormat.getFormat().toString());

            return checkAudioFormat(fileFormat
                    , sampleSizeInBits
                    , sampleRate
                    , 8000);
        } catch (Exception e) {
            log.error("[isWavFileCorrect] error message: {}", e.getMessage());

            return false;
        } finally {
            FileUtil.deleteFile(tempFile);
        }
    }

    private boolean checkAudioFormat(AudioFileFormat fileFormat
            , int sampleSizeInBits
            , float sampleRate
            , int acceptableSampleRate) {
        return (sampleSizeInBits == 16)
                && ((int) Math.floor(sampleRate) == acceptableSampleRate)
                && fileFormat.getFormat().toString().contains("mono");
    }
    
	private Boolean isInvalidFile(MultipartFile modelFile, BaseResponseDto<Object> responseDto) throws IOException {
		if (modelFile.getSize() > MAX_FILE_SIZE) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(FILE_TOO_BIG_MESSAGE);
            return true;
        }

        if (!isWavFileCorrect(modelFile)) {
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(FILE_TYPE_MISMATCH_MESSAGE);
            return true;
        }
		return false;
	}
}
