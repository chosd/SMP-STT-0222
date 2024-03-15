package com.kt.smp.stt.callinfo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;

import com.github.pagehelper.Page;
import com.kt.smp.common.util.CommUtil;
import com.kt.smp.common.util.crypto.AudioCrypto;
import com.kt.smp.common.util.crypto.TextCrypto;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.callinfo.dto.CallInfoLogVO;
import com.kt.smp.stt.callinfo.dto.CallInfoSearchConditionDto;
import com.kt.smp.stt.callinfo.dto.StreamUrlParamsVO;
import com.kt.smp.stt.callinfo.dto.SttCallInfoDto;
import com.kt.smp.stt.callinfo.enums.CallInfoError;
import com.kt.smp.stt.callinfo.exception.CallInfoException;
import com.kt.smp.stt.callinfo.repository.SttCallInfoRepository;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* @FileName : SttCallInfoServcieImpl.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 10.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 : 상담 내역 청취 서비스
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class SttCallInfoServcieImpl implements SttCallInfoService{

	@Value("${smp.service.uri.prefix}")
    private String smpServiceUriPrefix;
	
	@Value("${callinfo.wav-download-path}")
    private String SAVE_FOLDER_PATH;
	
	@Value("${callinfo.web-url}")
    private String WAV_WEB_URL;
	
	@Value("${callinfo.stream-mode}")
    private boolean isStreamMode;
	
	@Value("${callinfo.stream-url}")
    private String streamUrl;
	
	@Value("${callinfo.text.decode}")
    private boolean textDecodeRequired;
	
	@Value("${callinfo.text.masking}")
    private boolean textMaskingRequired;
	
	@Value("${callinfo.text.masking_url}")
    private String maskingRequestUrl;
		
	@Value("${callinfo.audio.ext}")
    private String AUDIO_EXT;
	
	private final RestTemplate restTemplate;
	private final SttCallInfoRepository callInfoRepository;
	private final ServiceModelService serviceModelService;
	private final ConfigService configService;
	private final AudioCrypto audioCrypto;
	private final TextCrypto textCrypto;
	private final RestApiMaskingService restMaskServie;
	
	private static final String PATH_DELIMITER = "/";
	
	@Override
	public Page<SttCallInfoDto> search(CallInfoSearchConditionDto callInfoSearchConditionDto) {
		setTenantContext();
    	
    	Page<SttCallInfoDto> result = callInfoRepository.search(callInfoSearchConditionDto);
    	List<SttCallInfoDto> resultList = result.getResult();
    	
    	for (SttCallInfoDto dto : resultList) {
    		setWavFileName(dto);
    	}
    	
		return result;
	}
	
	/**
	* @MethodName : setWavFileName
	* @작성일 : 2023. 11. 15.
	* @작성자 : rapeech
	* @변경이력 :
	* @Method설명 :
	* @param dto
	*/
	private void setWavFileName(SttCallInfoDto dto) {
		String wavFilePath = dto.getWavFilePath();
		if (wavFilePath != null && !wavFilePath.equals("")) {
			dto.setWavFileName(extractFileName(wavFilePath));
		}
	}

	@Override
	public SttCallInfoDto getCallInfoByCallId(String callId) {
        SttCallInfoDto result = null;
        List<CallInfoLogVO> callInfoLogList = null;

        try {
            result = callInfoRepository.getCallInfoByCallId(callId);

            callInfoLogList = callInfoRepository.getCallInfoLogListByApplicationId(result.getApplicationId());
            result.setConversationList(callInfoLogList);

            ServiceModelVO serviceModel = serviceModelService.detailByServiceCode(result.getServiceCode());
            result.setServiceModelName(serviceModel.getServiceModelName());

        } catch (Exception e) {
            throw new CallInfoException(CallInfoError.DB_IO_ERROR, e);
        }

        // 문자열 디코딩
        if (textDecodeRequired) {
            decodeCallInfoLogText(callInfoLogList);
        }

        // 문자열 마스킹 ( 기존 masking -> restAPI masking 호출로 변경 2324.03.14 )
        if (textMaskingRequired) {
//            maskCallInfoLogText(callInfoLogList);
            maskingRequestAndResponse(callInfoLogList);
            
        }

        return result;
	}
	
	@Override
	public SttCallInfoDto getCallInfoByApplicationId(String applicationId) {
		
		setTenantContext();
		
		SttCallInfoDto result = callInfoRepository.getCallInfoByApplicationId(applicationId);
		List<CallInfoLogVO> callInfoLogList = callInfoRepository.getCallInfoLogListByApplicationId(applicationId);

		
		String callStartTime = result.getCallStartTime();

		try {
			setComputedTimeStamp(callInfoLogList, callStartTime);
		} catch (ParseException e) {
			throw new CallInfoException(CallInfoError.TIME_FORMAT_IS_WRONG, e);
		}

		result.setConversationList(callInfoLogList);

		ServiceModelVO serviceModel = serviceModelService.detailByServiceCode(result.getServiceCode());
		result.setServiceModelName(serviceModel.getServiceModelName());

        // 문자열 디코딩
        if (textDecodeRequired) {
            decodeCallInfoLogText(callInfoLogList);
        }

        // 문자열 마스킹 ( 기존 masking -> restAPI masking 호출로 변경 2324.03.14 )
        if (textMaskingRequired) {
//            maskCallInfoLogText(callInfoLogList);
            maskingRequestAndResponse(callInfoLogList);
        }

        return result;
	}

	private void setComputedTimeStamp(List<CallInfoLogVO> callInfoLogList, String callStartTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		long callStartTimeMs = sdf.parse(callStartTime).getTime();
		
		for (CallInfoLogVO vo : callInfoLogList) {
			Date epdStartTimeDate = sdf.parse(vo.getStartTime());
			Date epdEndTimeDate = sdf.parse(vo.getEndTime());
			long newStartTimeStamp = epdStartTimeDate.getTime() - callStartTimeMs;
			long newEndTimeStamp = epdEndTimeDate.getTime() - callStartTimeMs;
			vo.setStartTimeStamp((int) newStartTimeStamp);
			vo.setEndTimeStamp((int) newEndTimeStamp);
		}
	}
	

	/**
	* @MethodName : makeAudioFilePath
	* @작성일 : 2023. 10. 19.
	* @작성자 : homin.lee
	* @변경이력 :
	* @Method설명 :
	*/
	private String makeAudioFilePath(String fileName) {
		 // WAV_WEB_URL's value example : /waves/** (asterisk for Interceptor)
        String resourceUrl = WAV_WEB_URL.substring(0, WAV_WEB_URL.indexOf("*"));
        String result = resourceUrl + fileName;

        if (isStreamMode) {
//        	result += "." + AUDIO_EXT;
					StringBuffer sb = new StringBuffer(result);
					sb.append(".");
					sb.append(AUDIO_EXT);
        }

        return result;
	}

	/**
	* @MethodName : decodeCallInfoLogText
	* @작성일 : 2023. 10. 12.
	* @작성자 : homin.lee
	* @변경이력 :
	* @Method설명 : 콜 정보 대화록 Text 복호화 메서드
	* @param callInfoLogList
	*/
	private void decodeCallInfoLogText(List<CallInfoLogVO> callInfoLogList) {
		for (CallInfoLogVO callInfoLog : callInfoLogList) {
			String decodedText = textCrypto.decrypt(callInfoLog.getSttText());
			callInfoLog.setSttText(decodedText);
		}
	}

	/**
	* @MethodName : maskingCallInfoLogText
	* @작성일 : 2023. 10. 12.
	* @작성자 : homin.lee
	* @변경이력 :
	* @Method설명 : 콜 정보 대화록 Text 마스킹
	* @param callInfoLogList
	*/
	private void maskCallInfoLogText(List<CallInfoLogVO> callInfoLogList) {
		for (CallInfoLogVO callInfoLog : callInfoLogList) {
			String decodedText = CommUtil.maskPhoneNumberInText(callInfoLog.getSttText());
			callInfoLog.setSttText(decodedText);
		}
	}
	
	/**
	 * 인식결과 Text Masking 요청.
	 * @author JangJoongHwan.
	 * @param callInfoLogList iaap_stt_call_info 테이블 VO List
	 * */
	private void maskingRequestAndResponse(List<CallInfoLogVO> callInfoLogList) {
		for (CallInfoLogVO callInfoLog : callInfoLogList) {
			String maskingData = null;
			
			maskingData = restMaskServie.requestMask(maskingRequestUrl, callInfoLog.getSttText());
			callInfoLog.setSttText(maskingData);
		}
	}

	/**
	* @MethodName : requestWavFileDownload
	* @작성일 : 2023. 10. 10.
	* @작성자 : homin.lee
	* @변경이력 :
	* @Method설명 : 음원 파일 다운로드 전처리 후 요청하는 메서드
	* @param callId 콜키
	* @return 음원 파일 경로 (wavFilePath)
	*/
	private byte[] requestWavFileDownload(StreamUrlParamsVO streamingUrlParams) {
        String fileDownUrl = makeWavFileDownUrl(streamingUrlParams);
        String fileName = streamingUrlParams.getApplicationId();
        return downloadFileByUrl(fileDownUrl, fileName);
	}
	
	/**
	* @MethodName : makeFileDownUrl
	* @작성일 : 2023. 10. 10.
	* @작성자 : homin.lee
	* @변경이력 :
	* @Method설명 : 음원 파일 다운로드 URL 생성
	* @param streamingUrlParams
	* @return 음원 파일 다운로드 URL
	*/
	private String makeWavFileDownUrl(StreamUrlParamsVO streamingUrlParams) {
		String applicationId = streamingUrlParams.getApplicationId();
		String callStartTime = streamingUrlParams.getCallStartTime();
		return streamUrl + "rec_datm=" + callStartTime + "&rec_keycode=" + applicationId;
	}

	/**
	* @MethodName : getStreamingUrlParamsByCallId
	* @작성일 : 2023. 10. 10.
	* @작성자 : homin.lee
	* @변경이력 :
	* @Method설명 : 스트리밍 ( 음원파일 다운로드 ) 시에 필요한 데이터 조회
	* @param callId
	* @return StreamUrlParamsVO ( APPLICATION_ID, CALL_START_TIME )
	*/
	private StreamUrlParamsVO getStreamingUrlParamsByCallId(String callId) {
		return callInfoRepository.getStreamingUrlParamsByCallId(callId);
	}
	
	/**
	* @MethodName : getStreamingUrlParamsByApplicationId
	* @작성일 : 2023. 10. 10.
	* @작성자 : homin.lee
	* @변경이력 : 
	* @Method설명 : 스트리밍 ( 음원파일 다운로드 ) 시에 필요한 데이터 조회
	* @param callId
	* @return StreamUrlParamsVO ( APPLICATION_ID, CALL_START_TIME )
	*/
	private StreamUrlParamsVO getStreamingUrlParamsByApplicationId(String applicationId) {
		return callInfoRepository.getStreamingUrlParamsByApplicationId(applicationId);
	}
	
	/**
	* @MethodName : downloadFileByUrl
	* @작성일 : 2023. 10. 10.
	* @작성자 : homin.lee
	* @변경이력 :
	* @Method설명 :
	* @param fileUrl 파일 다운로드 URL
	* @param fileName 파일 명
	* @param ext 확장자
	* @return 저장된 파일 경로
	* @throws IOException
	*/
	private byte[] downloadFileByUrl(String fileUrl, String applicationId) {
        
		String filePath = SAVE_FOLDER_PATH + applicationId + "." + AUDIO_EXT;

        createNewFolder(SAVE_FOLDER_PATH);

        Path targetPath = Path.of(filePath);

        ResponseEntity<byte[]> res = restTemplate.getForEntity(fileUrl, byte[].class);

		// 초기에는 녹취서버에서 redirectURL을 줌
        byte[] buffer = res.getBody();
		if (buffer == null) {
			throw new CallInfoException(CallInfoError.FAIL_TO_LINK);
		}

		if(buffer[0] == 60){
			throw new CallInfoException(CallInfoError.FAIL_TO_LINK);
		}

		String redirectUrl = "";
		try{
			redirectUrl = new String(buffer, "UTF-8");
		}catch(Exception e){
			log.error("redirectUrl[redirectUrl cannot convert String] : " + redirectUrl);
			throw new CallInfoException(CallInfoError.FAIL_TO_LINK);
		}

		// redirectURL을 http~로 주는 지 여부 체크
		log.debug("redirectUrl : " + redirectUrl);
		if(!"h".equals(redirectUrl.substring(0,1))){
			log.error("redirectUrl[No Http Url] : " + redirectUrl);
			throw new CallInfoException(CallInfoError.FAIL_TO_LINK);
		}

		// redirectURL을 받아서 다운로드 시도
		res = restTemplate.getForEntity(redirectUrl, byte[].class);

		buffer = res.getBody();
		if (buffer == null) {
			throw new CallInfoException(CallInfoError.FAIL_TO_LINK);
		}

		if(buffer[0] == 60){
			throw new CallInfoException(CallInfoError.FAIL_TO_LINK);
		}

        if (buffer.length > 0) {
            /*
             * 녹취 서버에 파일이 존재하지 않을 시에 HttpStatus 200, Response로 HTML 태그가 넘어온다.
             * 즉, 파일이 존재하든 하지않든 HttpStatus는 200으로 응답이 오기 때문에 예외가 발생하지 않는다.
             * 따라서, 파일 존재 유무의 플래그 값으로 파일 미존재 시 Response의 가장 첫 문자(<)의 ASCII인 60을 기준으로 한다.
             * */
            if (buffer[0] == 60) {
                throw new CallInfoException(CallInfoError.NO_FILE_IN_LINK_SERVER);
            }
        }

        try {
            FileCopyUtils.copy(buffer, targetPath.toFile());
        } catch (IOException e) {
            throw new CallInfoException(CallInfoError.FAIL_TO_DOWNLOAD, e);
        }
        
	    // 음원 파일 경로 DB에 저장
	    updateWavFilePath(applicationId, filePath);
	    
        return buffer;
	}
	
	private void updateWavFilePath(String applicationId, String filePath) {
		 Map<String, String> updateWavFileMap = new HashMap<>();
		 updateWavFileMap.put("applicationId", applicationId);
		 updateWavFileMap.put("wavFilePath", filePath);
		 callInfoRepository.updateWavFilePath(updateWavFileMap);
	}

	/**
	* @MethodName : createNewFolder
	* @작성일 : 2023. 10. 17.
	* @작성자 : homin.lee
	* @변경이력 :
	* @Method설명 : 폴더 존재 여부 확인 후 없다면 생성
	* @param path
	*/
	private void createNewFolder(String path) {
		File Folder = new File(path);
    	
    	if (!Folder.exists()) {
			try{
			    Folder.mkdirs();
		    } catch(Exception e){
			    throw new CallInfoException(CallInfoError.FAIL_TO_CREATE_FOLDER, e);
			}
		}
	}
	
	/**
	* @MethodName : extractWavFileName
	* @작성일 : 2023. 10. 10.
	* @작성자 : homin.lee
	* @변경이력 :
	* @Method설명 : 음원 파일 경로에서 음원 파일 명 추출
	* @param wavFilePath
	*/
	private String extractFileName(String filePath) {
		String[] fileNameArr = filePath.split(PATH_DELIMITER);
		String fileName = fileNameArr[fileNameArr.length-1];
		
		return fileName;
	}

	@Override
	public String getCallStatusByApplicationId(String applicationId) {
		try {
			return callInfoRepository.getCallStatusByApplicationId(applicationId);
		} catch (Exception e) {
			throw new CallInfoException(CallInfoError.FAIL_TO_GET_CALL_STATUS, e);
		}
	}

    @Override
    public byte[] getCallInfoWavFile(String applicationId) {
        byte[] wavByte = null;
        SttCallInfoDto callInfo = getCallInfoByApplicationId(applicationId);
        String filePath = callInfo.getWavFilePath();
        
		if ((filePath != null && !filePath.equals("")) || !isStreamMode) {
			return filePathToByteArray(filePath);
		}
	    
		try {
			StreamUrlParamsVO streamingUrlParams = getStreamingUrlParamsByApplicationId(applicationId);
			wavByte = requestWavFileDownload(streamingUrlParams);
		} catch (CallInfoException e) {
			log.error("requestWavFileDownload Error -- {}", e);
		} catch (Throwable e) {
			log.error("requestWavFileDownload Error -- {}", e);
		}

        return wavByte;
    }
	
    private byte[] filePathToByteArray(String filePath) {

        Path wavFilePath = Paths.get(filePath);
        
        try {
        	byte[] wavByte = Files.readAllBytes(wavFilePath);
            boolean isWavEncrypted = PreferenceValueHolder.wavEncrypt.get(TenantContextHolder.getProjectCode());
           
            // 음원 디코딩 ( 암호화 환경설정이 'Y'일 때만 디코딩)
    	    if(audioCrypto.isEncrypted(filePath) && isWavEncrypted) {
    	    	wavByte = audioCrypto.getDecryptedByteArray(filePath);
    	    }
    	    
            return wavByte;
        } catch (IOException e) {
            throw new CallInfoException(CallInfoError.NO_FILE_IN_FILE_PATH, e);
        }
    }
	
    private void setTenantContext() {
		List<ConfigDto> configDtos = configService.getAllUserDefined();
		
    	for (ConfigDto configDto : configDtos) {
    		TenantContextHolder.set(configDto.getProjectCode());	
    	}
	}
}
