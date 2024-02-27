/******************************************************************************************
 * 본 프로그램소스는 하나은행의 사전승인 없이 임의복제, 복사, 배포할 수 없음
 *
 * Copyright (C) 2018 by co.,Ltd. All right reserved.
 ******************************************************************************************/
package com.kt.smp.stt;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.kt.smp.common.util.EncUtil;
import com.kt.smp.common.util.WaveUtil;
import com.kt.smp.config.CommonConstants;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.common.CallbackUrlResolver;
import com.kt.smp.stt.common.EngineUrlResolver;
import com.kt.smp.stt.common.dto.BaseResultDto;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployRequestDto;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployResponseDto;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployStatusResponseDto;
import com.kt.smp.stt.test.domain.SttTestMultipartRequestDto;
import com.kt.smp.stt.train.train.domain.SttTrainRequestDto;
import com.kt.smp.stt.train.train.domain.SttTrainStatusResponseDto;
import com.kt.smp.stt.verify.request.dto.EngineVerifyRequestDto;
import com.kt.smp.stt.verify.request.dto.VerifyStatusResponseDto;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootTest(properties = "spring.profiles.active:dev")
class ApiTest {

	@Autowired 
	RestTemplate restTemplate;
	@Autowired
	ServiceModelService serviceModelService;
	@Autowired
	EngineUrlResolver engineUrlResolver;
	@Autowired
	ConfigService configService;
	@Autowired
	CallbackUrlResolver callbackUrlResolver;
	
	// Dev DB 콜봇 Service code
	final String TEST_ENABLE_SERVICE_CODE = "2";
	
	String profile = "local";
	String projectCode = "test-project-code";
	String coreUrl;
	
	@BeforeEach()
	void before() {
		coreUrl = profile.equals(CommonConstants.LOCAL_PROFILE) ? engineUrlResolver.resolve() : engineUrlResolver.resolve(projectCode);
	}
	
	@Test
	public void trainStatus() {
		log.info("============================= start trainStatus junit ================================");
		
		List<ConfigDto> configDtos = configService.getAllUserDefined();
    	for (ConfigDto configDto : configDtos) {
    		
    		TenantContextHolder.set(configDto.getProjectCode());	
    		
    	}
		
		String serviceCode = serviceModelService.detail(2).getServiceCode();
		
		log.info("=== serviceCode : " + serviceCode);
		log.info("before coreUrl ==============================");
        coreUrl = coreUrl + "/stt/train/status?serviceCode=" + serviceCode;
        log.info("before api call ============== / "+coreUrl);
        ResponseEntity<SttTrainStatusResponseDto> responseEntity = restTemplate.getForEntity(coreUrl, SttTrainStatusResponseDto.class);
        log.info("after api call ==============");
        String status = (ObjectUtils.isEmpty(responseEntity) || ObjectUtils.isEmpty(responseEntity.getBody())) ? null : responseEntity.getBody().getStatus();
        
        log.info(">>>>>> trainStatus  result : "+responseEntity.getBody());
        // serviceCode='2', status='COMPLETE', modelType='E2ELM', modelPath='/nas/sttAgent/t-agent/train/trainedModel/svc/2/model_2_E2ELM.tar.gz', 
        // modelAuthKey='ff893d6aaeaa862797cf927587146900' 
        // serviceCode='2', status='COMPLETE', modelType='E2ELM', modelPath='/nas/sttAgent/t-agent/train/trainedModel/svc/2/model_2_E2ELM.tar.gz',
        // modelAuthKey='9fab2eba2169b328036c48e7cc30fa4e' 10월20일 버전  248bbdd95f71259476a217373add4d55
		
	}
	
	@Test
	public void train() {
		log.info("=============================== start train junit ==============================");
		List<ConfigDto> configDtos = configService.getAllUserDefined();
    	for (ConfigDto configDto : configDtos) {
    		
    		TenantContextHolder.set(configDto.getProjectCode());	
    		
    	}
		
		
		//log.info(">>>>>>>>>>> callbackUrl : "+absolute);
		log.info(">>>>>>> callbackUrlResolver.trainCallbackUrl() :: "+callbackUrlResolver.trainCallbackUrl());
		List<String> param = new ArrayList<>();
		SttTrainRequestDto requestDto = SttTrainRequestDto.builder()
				//.trainDataPath("C:\\2nas_home\\stt\\test-project-code\\upload\\trainData\\svc\\1\\class.txt")
				.trainDataPath("/nas/sttAgent/t-agent/train/trainedModel/svc/2/model_2_E2ELM.tar.gz")
				.trainE2ESLDataPathList(param)
				.trainE2ESLWavPathList(param)
				.trainE2EUSLWavPathList(param)
                .serviceCode(serviceModelService.detail(2).getServiceCode())
                .callbackUrl(callbackUrlResolver.trainCallbackUrl())
                .modelType("E2ELM")
                .build();
		
		ResponseEntity<BaseResultDto> responseEntity = null;
		log.info("====================== before api call ====================");
        responseEntity = restTemplate.postForEntity(coreUrl + "/stt/train", requestDto, BaseResultDto.class);
        
        log.info(">>>>>>>> train result :: {}", responseEntity.getBody());
		
	}
	
	@Test
	public void trainCallback() {
		log.info("=================================== start train callback junit ======================================");
		
		log.info("================== KT STT agent 다운되어 있어서 postman 으로 임의적으로 호출하여 테스트 =================");
		log.info(">>>>> [호출URL] : http://127.0.0.1:8081/kt-stt/1.0/train/api/callback");
		log.info(">>>>> [Parameter Type] : JSON ");
		log.info(">>>>> [Parameter] : {\n"
				+ "    \"resultCode\" : \"0000\",\n"
				+ "    \"resultMsg\" : \"SUCESS\",\n"
				+ "    \"serviceCode\" : \"4\",\n"
				+ "    \"status\" : \"COMPLETE\",\n"
				+ "    \"lmType\" : \"CLASS\",\n"
				+ "    \"modelPath\" : \"/nas/model/svc/2/model_2.tar.gz\",\n"
				+ "    \"modelAuthKey\" : \"83df95a61918148768b7c09019dd09eb\"\n"
				+ "}");
		
		
	}
	
	@Test
	public void deploy() {
		log.info("======================== start deploy junit ===========================");
		List<ConfigDto> configDtos = configService.getAllUserDefined();
    	for (ConfigDto configDto : configDtos) {
    		
    		TenantContextHolder.set(configDto.getProjectCode());	
    		
    	}
    	log.info(">>>>>>>>> callbackUrlResolver.deployCallbackUrl() :: "+callbackUrlResolver.deployCallbackUrl());
		SttDeployRequestDto requestDto = SttDeployRequestDto.builder()
                .serviceCode(TEST_ENABLE_SERVICE_CODE)
                .modelPath("/nas/model/svc/2/model_2_E2ELM.tar.gz")
                .modelAuthKey("dc8fa759ee1ba6727fe78cd40becb0be")
                .callbackUrl(callbackUrlResolver.deployCallbackUrl())
                .modelType("E2ELM")
                .build();

		ResponseEntity<SttDeployResponseDto> responseEntity = null;
		
		log.info("====================== before api call ====================");
		responseEntity = restTemplate.postForEntity(coreUrl + "/stt/deploy", requestDto, SttDeployResponseDto.class);
		
        log.info(">>>>> deploy result :: {}" , responseEntity.getBody());
	}
	
	@Test
	public void deployCallback() {
		log.info("=================================== start deploy callback junit ======================================");
		
		log.info("================== KT STT agent 다운되어 있어서 postman 으로 임의적으로 호출하여 테스트 =================");
		log.info(">>>>> [호출URL] : http://127.0.0.1:8081/kt-stt/1.0/deploy/api/callback");
		log.info(">>>>> [Parameter Type] : JSON ");
		log.info(">>>>> [Parameter] : {\n"
				+ "    \"resultCode\" : \"0000\",\n"
				+ "    \"resultMsg\": \"SUCESS\",\n"
				+ "    \"serviceCode\": \"4\",\n"
				+ "    \"status\": \"COMPLETE\",\n"
				+ "    \"deployList\": [\n"
				+ "        {\n"
				+ "            \"serverName\": \"E-POD-01\",\n"
				+ "            \"status\": \"comlete\"\n"
				+ "        },\n"
				+ "        {\n"
				+ "            \"serverName\": \"E-POD-02\",\n"
				+ "            \"status\": \"complete\"\n"
				+ "        }\n"
				+ "    ]\n"
				+ "}");
	}
	
	@Test
	public void deployStatus() {
		log.info("============================= start deployStatus junit ================================");
		
		List<ConfigDto> configDtos = configService.getAllUserDefined();
    	for (ConfigDto configDto : configDtos) {
    		
    		TenantContextHolder.set(configDto.getProjectCode());	
    		
    	}

        log.info("============================= before api call ===============================");
        ResponseEntity<SttDeployStatusResponseDto> responseEntity = restTemplate.getForEntity(coreUrl + "/stt/deploy/status?serviceCode=" + TEST_ENABLE_SERVICE_CODE, SttDeployStatusResponseDto.class);
        
		log.info(">>>>> deployStatus result :: {}" , responseEntity.getBody());
		// "serviceCode":"2","status":"READY","deployList":[{"sruName":"1","status":"READY"}]
	}
	
	@Test
	public void verifyStatus() {
		log.info("============================= start verifyStatus junit ================================");

		//ServiceModelVO serviceModel = serviceModelService.getByName(history.getServiceModelName());
		
		log.info("==================== before api call ====================");
		String requestUrl = coreUrl + "/stt/verify/status?serviceCode=" + TEST_ENABLE_SERVICE_CODE;
		VerifyStatusResponseDto response = restTemplate.getForEntity(requestUrl, VerifyStatusResponseDto.class).getBody();
		log.info(">>>>>> verifyStatus result :: {} " , response);
	}
	
	@Test
	public void verify() {
		log.info("================== start verify junit ================");
		
		/* 검증 요청에 필요한 요청값 (검증 wav 폴더경로, 정답지 파일 경로) 확인 후 재작성 */
		
//		String requestUrl = coreUrl + "/stt/verify";
//		EngineVerifyRequestDto engineVerifyRequest = makeEngineVerifyRequest(requestUrl);
//		log.info("==================== before api call ====================");
//		VerifyResponseDto response = restTemplate.postForEntity(requestUrl, engineVerifyRequest, VerifyResponseDto.class).getBody();
	}
	
	@Test
	@DisplayName("검증 (NAS 미사용)")
	public void verifyMultipart() {
//		String requestUrl = coreUrl + "/stt/verify";
//		EngineVerifyRequestDto engineVerifyRequest = makeEngineVerifyRequest(requestUrl);
//		log.info("==================== before api call ====================");
//		VerifyResponseDto response = restTemplate.postForEntity(requestUrl, engineVerifyRequest, VerifyResponseDto.class).getBody();
	}
	
	@Test
	public void verifyCallback() {
		log.info("=================================== start verify callback junit ======================================");
		
		log.info("================== KT STT agent 다운되어 있어서 postman 으로 임의적으로 호출하여 테스트 =================");
		log.info(">>>>> [호출URL] : http://127.0.0.1:8081/kt-stt/1.0/api/verify/callback");
		log.info(">>>>> [Parameter Type] : JSON ");
		log.info(">>>>> [Parameter] : {\n"
				+ "    \"resultCode\":\"0000\",\n"
				+ "    \"resultMsg\":\"SUCESS\",\n"
				+ "    \"serviceCode\":\"4\",\n"
				+ "    \"status\":\"COMPLETE\",\n"
				+ "    \"cer\":\"90.1\",\n"
				+ "    \"wer\":\"80.1\"\n"
				+ "}");
		
	}
	
	@Test
	public void testRequest() {
		/* 테스트할 wav 파일 경로 습득 시 테스트 재작성 필요 */
		
//		log.info("======================= start testRequest junit ======================");
//        String testUrl = coreUrl + "/stt/test";
//        String UUID = "1"; // 단건테스트를 구분짓는 UUID
//        SttTestRequestDto sttTestRequestDto = SttTestRequestDto.builder()
//                .serviceCode(TEST_ENABLE_SERVICE_CODE)
//                .testWavPath("테스트할 wav 파일 경로") // 추후 수정 및 테스트 필요
//                .callbackUrl(callbackUrlResolver.testCallbackUrl(UUID))
//                .build();
//
//        ResponseEntity<SttTestResponseDto> sttTestResponseEntity = restTemplate.postForEntity(testUrl, sttTestRequestDto, SttTestResponseDto.class);
	}
	
	@Test
	@DisplayName("단건 테스트(NAS 미사용)")
	public void testRequestMultipart() {
		/* 테스트할 wav 파일 경로 습득 시 테스트 재작성 필요 */
		
		log.info("======================= start testRequestMultipart junit ======================");
		String testUrl = coreUrl + "/stt/test/multipart";
        String UUID = "1"; // 단건테스트를 구분짓는 UUID
        
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		SttTestMultipartRequestDto sttTestMultipartRequestDto = SttTestMultipartRequestDto.builder()
				.serviceCode(TEST_ENABLE_SERVICE_CODE)
				.callbackUrl(callbackUrlResolver.testCallbackUrl(UUID))
				.build();
		
		byte[] dummyPcm = {1,2};
		byte[] wavFile = WaveUtil.makeWave(1, 8000, 16, dummyPcm);
		
		ByteArrayResource resource = new ByteArrayResource(wavFile) {
			@Override
			public String getFilename() {
				return "test.wav";
			}
		};
		
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("testInfoData", sttTestMultipartRequestDto);
		body.add("testData", resource);
		
		HttpEntity<?> uploadEntity = new HttpEntity<>(body, headers);
		
		ResponseEntity<SttTestMultipartRequestDto> responseEntity = 
				restTemplate.postForEntity(testUrl, uploadEntity, SttTestMultipartRequestDto.class);
	}
	
	private EngineVerifyRequestDto makeEngineVerifyRequest(String requestUrl) {
		return EngineVerifyRequestDto.builder()
				.serviceCode(TEST_ENABLE_SERVICE_CODE)
				.verificationWavPath("") // 23.09.19 검증 wav 폴더 경로 데이터 없음 
				.correctAnswerPath("") // 23.09.19 정답지 파일 저장경로 데이터 없음 --> 추후 추가 후 테스트 요망
				.callbackUrl(callbackUrlResolver.verifyCallbackUrl())
				.build();
	}
	
	@Test
	public void modelNameRemake() {
		
		String receiveModelPath = "/nas/sttAgent/t-agent/train/trainedModel/svc/2/model_2_E2ELM.tar.gz";
    	String[] splitModelPath = receiveModelPath.split("/");
    	String receiveModelName = splitModelPath[splitModelPath.length-1];
    	String[] splitModelName = receiveModelName.split("[.]",2);
    	String originFileName = splitModelName[0];
    	String newFileName = originFileName+"_1."+splitModelName[1];
    	String newModelPath = "";
    	for(int i=1; i<splitModelPath.length-1; i++  ) {
    		newModelPath +="/"+splitModelPath[i]; 
    	}
    	
    	Path downloadPath = Paths.get("/nas", "/trainData/download/" );
    	String myPath = downloadPath.toString();
    	log.info(">>>>>>> my path : "+myPath+"/");
    	String finalModelPath = myPath+"/"+newFileName;
    	log.info(">>>> newModelPath : "+newModelPath);
    	log.info(">>>>> receiveModelName : "+receiveModelName+" / "+splitModelName[0]+" / "+splitModelName[1]+" / "+newFileName);
    	log.info( ">>>>>>> LAST : "+finalModelPath);
		
	}
	
	@Test
	public void encdecTest() {
		EncUtil util = new EncUtil();
		String key = "4F8ABBD4EE68E655F42146E87D6E4022";
		String stm123 = "stm123";
		String stmadm123 = "stmadm123";
		String WEBADMIN123 = "WEBADMIN123";
		String stm123EncResult = util.encAES128(stm123, key);
		String stmadm123EncResult = util.encAES128(stmadm123, key);
		String WEBADMIN123EncResult = util.encAES128(WEBADMIN123, key);
		String qwe = "123qwe";
		String qweEncResult = util.encAES128(qwe, key);
		String dptmxldpa123zhs = "dptmxldpa123zhs";
		String dptmxldpa123zhResult = util.encAES128(dptmxldpa123zhs, key);
		String rapeech123 = "Rapeech123";
		String rapeech123Result = util.encAES128(rapeech123, key);
		
		log.info(">>>> rapeech123Result : "+rapeech123Result);
		log.info(">>>>> dptmxldpa123zhResult : "+dptmxldpa123zhResult);
		log.info(" >>>  qweEncResult : "+qweEncResult);
		log.info(">>> stm123EncResult : "+stm123EncResult);
		log.info(">> stmadm123EncResult : "+stmadm123EncResult);
		log.info(">>> WEBADMIN123EncResult : "+WEBADMIN123EncResult);
		
	}
	
}
