package com.kt.smp.stt.operate.session.service;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.kt.smp.common.util.JacksonUtil;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.common.EngineUrlResolver;
import com.kt.smp.stt.operate.session.dto.ServerInfoDto;
import com.kt.smp.stt.operate.session.dto.ServerSessionInfoDto;
import com.kt.smp.stt.operate.session.dto.SessionInfoDto;
import com.kt.smp.stt.operate.session.dto.SessionRequestDto;
import com.kt.smp.stt.operate.session.dto.SttServerInfoDto;
import com.kt.smp.stt.operate.session.dto.SttServerSessionInfoDto;
import com.kt.smp.stt.operate.session.dto.SttSessionInfoDto;
import com.kt.smp.stt.operate.session.dto.SttSessionResponseDto;
import com.kt.smp.stt.operate.session.enums.SessionSortCondition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
*@FileName : SttSuhyupSessionService.java
@Project : kt-stt-service_r
@Date : 2023. 9. 19.
*@작성자 : wonyoung.ahn
*@변경이력 : 구현체에서 인터페이스 추출 (homin.lee) 2023. 11. 17
*@프로그램설명 :
*/
@Slf4j
@Service("sttSuhyupSessionService")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SttSuhyupSessionService implements SttSessionService {

    private static final String CORE_STT_CHANNEL_INFO_URL = "/stt/session/info/all";
    private final RestTemplate restTemplate;
    private final EngineUrlResolver engineUrlResolver;
    
    @Value("${core.stt.channel.delay}")
    private String delay;

    public List<ServerInfoDto> convertData(List<SttServerInfoDto> serverInfoList, SessionRequestDto sessionRequestDto) {
        List<ServerInfoDto> resServerInfoList = new ArrayList<>();

        if (serverInfoList == null) {
            return resServerInfoList;
        }

        if (serverInfoList.isEmpty())  {
            return resServerInfoList;
        }

        for (SttServerInfoDto serverInfo : serverInfoList) {
            ServerInfoDto resServerInfo = new ServerInfoDto();
            List<ServerSessionInfoDto> resServerSessionInfoList = new ArrayList<>();
            if (!ObjectUtils.isEmpty(serverInfo)) {
            	resServerInfo.setServerName(serverInfo.getServerName());
            	resServerInfo.setConnectionStatus(serverInfo.getConnectionStatus());
                if (serverInfo.getConnectionStatus() == true) {
                    if (!ObjectUtils.isEmpty(serverInfo.getServerSessionInfo())) {
                        for (SttServerSessionInfoDto serverSessionInfo : serverInfo.getServerSessionInfo()) {
                        	ServerSessionInfoDto resServerSessionInfo = new ServerSessionInfoDto();
                        	resServerSessionInfo.setServiceCode(serverSessionInfo.getServiceCode());
                        	resServerSessionInfo.setSessionMaxCnt(serverSessionInfo.getSessionMaxCnt());
                            List<SessionInfoDto> sessionInfoList = new ArrayList<>();
                            for(int i=0; i<serverSessionInfo.getSessionMaxCnt(); i++) {
                            	if(serverSessionInfo.getSessionInfo() != null && serverSessionInfo.getSessionInfo().size() > i) {
                            		SttSessionInfoDto sttSessionInfo = serverSessionInfo.getSessionInfo().get(i);
                            		SessionInfoDto sessionInfo = new SessionInfoDto();
                            		
                            		if (sttSessionInfo.getInUse() == 0) {
                            			sessionInfo.setStatus("delay");
										sessionInfo.setSessionIdx(sttSessionInfo.getSessionIdx());
										sessionInfo.setResponseTime(sttSessionInfo.getResponseTime());
										sessionInfo.setCallKey(sttSessionInfo.getCallKey());
										sessionInfo.setSessionStartTime(sttSessionInfo.getSessionStartTime());
										sessionInfo.setSessionHoldingTime(getSessionHoldingTime(sttSessionInfo.getSessionStartTime()));
										sessionInfo.setDeviceId(sttSessionInfo.getDeviceId());
                            		} else if(sttSessionInfo.getInUse() == 1) {
										sessionInfo.setStatus("inuse");
										sessionInfo.setSessionIdx(sttSessionInfo.getSessionIdx());
										sessionInfo.setResponseTime(sttSessionInfo.getResponseTime());
										sessionInfo.setCallKey(sttSessionInfo.getCallKey());
										sessionInfo.setSessionStartTime(sttSessionInfo.getSessionStartTime());
										sessionInfo.setSessionHoldingTime(getSessionHoldingTime(sttSessionInfo.getSessionStartTime()));
										sessionInfo.setDeviceId(sttSessionInfo.getDeviceId());
                            		} else if(sttSessionInfo.getInUse() == 2) {
                            			sessionInfo.setStatus("stop");
										sessionInfo.setSessionIdx(sttSessionInfo.getSessionIdx());
										sessionInfo.setResponseTime(sttSessionInfo.getResponseTime());
										sessionInfo.setCallKey(sttSessionInfo.getCallKey());
										sessionInfo.setSessionStartTime(sttSessionInfo.getSessionStartTime());
										sessionInfo.setSessionHoldingTime(getSessionHoldingTime(sttSessionInfo.getSessionStartTime()));
										sessionInfo.setDeviceId(sttSessionInfo.getDeviceId());
                            		} else {
                            			sessionInfo.setStatus("ready");
										sessionInfo.setSessionIdx(sttSessionInfo.getSessionIdx());
										sessionInfo.setResponseTime(sttSessionInfo.getResponseTime());
										sessionInfo.setCallKey(sttSessionInfo.getCallKey());
										sessionInfo.setSessionStartTime(sttSessionInfo.getSessionStartTime());
										sessionInfo.setSessionHoldingTime(getSessionHoldingTime(sttSessionInfo.getSessionStartTime()));
										sessionInfo.setDeviceId(sttSessionInfo.getDeviceId());
                            		}
                                  sessionInfoList.add(sessionInfo);
                            	}else {
                            		SessionInfoDto sessionInfo = new SessionInfoDto();
                            		sessionInfo.setStatus("ready");
                            		sessionInfoList.add(sessionInfo);	
                            	}
                        	}
                            sortSessionInfo(sessionInfoList, sessionRequestDto);
                            resServerSessionInfo.setSessionInfo(sessionInfoList);
                            resServerSessionInfoList.add(resServerSessionInfo);
                        }
                    }
                    resServerInfo.setServerSessionInfo(resServerSessionInfoList);
                }

            }
            resServerInfoList.add(resServerInfo);
        }
        return resServerInfoList;
    }
    
    private Long getSessionHoldingTime(Long sessionStartTime) {
    	
    	Long now = System.currentTimeMillis();
    	
    	return now - sessionStartTime;
    }
    
    public String setDelay() {
    	return delay;
    }

    /**
     * 서버별 세션 정보 콜 유지시간 기준 내림차순 정렬
     */
	private void sortSessionInfo(List<SessionInfoDto> sessionInfoList, SessionRequestDto sessionRequestDto) {
		
		SessionSortCondition firstCondition = sessionRequestDto.getFirstCondition();
		SessionSortCondition secondCondition = sessionRequestDto.getSecondCondition();				
		
		Collections.sort(sessionInfoList, firstCondition.getComparator().thenComparing(secondCondition.getComparator()));
		
	}
    
    public SttSessionResponseDto getCoreChannelApi() {

    	String projectCode = TenantContextHolder.getProjectCode();
    	String coreUrl;
    	if ("host".equals(PreferenceValueHolder.sessionTarget.get(projectCode))){
            coreUrl = engineUrlResolver.resolve(null);
    	} else {
    		coreUrl = engineUrlResolver.resolveSub(null);	
    	}
    	if(coreUrl.contains("https")) {
		    ignoreSSL();
		}
    	
        ResponseEntity<SttSessionResponseDto> responseEntity = null;
        List<SttServerInfoDto> array = new ArrayList<>();
        SttSessionResponseDto entity = SttSessionResponseDto
                .builder()
                .resultCode("9990")
                .resultMsg("STT 엔진과의 통신이 원활하지 않습니다. 다시 시도해주세요.")
                .serverInfo(array)
                .build();

        try {
            responseEntity = restTemplate.getForEntity(coreUrl + CORE_STT_CHANNEL_INFO_URL, SttSessionResponseDto.class);

        } catch (Exception e) {
            log.error("[ERROR] {}", e.getMessage());
        }

        return ObjectUtils.isEmpty(responseEntity) ? entity : responseEntity.getBody();
    }

    public SttSessionResponseDto getDummyCoreChannelApi() {

        // 연동 전 dummy 데이터
        List<SttServerSessionInfoDto> serverSessionInfoList = new ArrayList<>();
        List<SttServerInfoDto> serverInfoList = new ArrayList<>();

        serverInfoList.add(SttServerInfoDto
                .builder()
                .serverName("c-pod-01")
                .connectionStatus(true)
                .supportSvcList(Arrays.asList("2"))
                .build());
        serverInfoList.add(SttServerInfoDto
                .builder()
                .serverName("c-pod-02")
                .connectionStatus(true)
                .supportSvcList(Arrays.asList("2"))
                .build());

        SttSessionResponseDto sttSessionResponseDto = SttSessionResponseDto
                .builder()
                .resultCode("0000")
                .resultMsg("success")
                .serverInfo(serverInfoList)
                .build();

        for (SttServerInfoDto sttServerInfo : serverInfoList) {

            if (sttServerInfo.getConnectionStatus() == true) {

                List<SttSessionInfoDto> sessionInfoList = new ArrayList<>();

                serverSessionInfoList.add(SttServerSessionInfoDto
                        .builder()
                        .serviceCode("2")
                        .sessionMaxCnt(60)
                        .sessionCnt(30)
                        .build());

                sttServerInfo.setServerSessionInfo(serverSessionInfoList);

                for (Integer i = 0; i < 15; i++) {
                    SttSessionInfoDto sessionInfoObj = SttSessionInfoDto
                            .builder()
                            .sessionIdx(i)
                            .inUse(0)
                            .responseTime(20)
                            .callKey(i.toString())
                            .sessionStartTime(1699315200000L) // 2023-11-07 09:00:00
                            .sessionHoldingTime(getSessionHoldingTime(1699315200000L))
                            .deviceId("4885_20193333")
                            .build();
                    sessionInfoList.add(0, sessionInfoObj);
                }

                for (Integer i = 0; i < 30; i++) {
                	SttSessionInfoDto sessionInfoObj = SttSessionInfoDto
                            .builder()
                            .sessionIdx(i)
                            .inUse(1)
                            .responseTime(2000)
                            .callKey(i.toString())
                            .sessionStartTime(1699315200000L) // 2023-11-07 09:00:00
                            .sessionHoldingTime(getSessionHoldingTime(1699315200000L))
                            .deviceId("4885_20193333")
                            .build();
                    sessionInfoList.add(0, sessionInfoObj);
                }

                for (Integer i = 0; i < 15; i++) {
                	SttSessionInfoDto sessionInfoObj = SttSessionInfoDto
                            .builder()
                            .sessionIdx(i)
                            .inUse(2)
                            .responseTime(40)
                            .callKey(i.toString())
                            .sessionStartTime(1699315200000L) // 2023-11-07 09:00:00
                            .sessionHoldingTime(getSessionHoldingTime(1699315200000L))
                            .deviceId("4885_20193333")
                            .build();
                    sessionInfoList.add(0, sessionInfoObj);
                }

                // total 120, inuse:30 , delay:60 , ready: 30
                for (SttServerSessionInfoDto serverSessionInfo : serverSessionInfoList) {
                	serverSessionInfo.setSessionInfo(sessionInfoList);
                }
            } else {
            	sttServerInfo.setServerSessionInfo(null);
            }
        }
        return sttSessionResponseDto;
    }
    
    
    /**
    *@MethodName : getSampleData
    *@작성일 : 2023. 9. 21.
    *@작성자 : wonyoung.ahn
    *@변경이력 :
    *@Method설명 :샘플데이터
    *@return
    */
    public SttSessionResponseDto getSampleData() {
    	String jsonString = "{\n"
    			+ "    \"resultCode\": \"0000\",\n"
    			+ "    \"resultMsg\": \"success\",\n"
    			+ "    \"serverInfo\": [\n"
    			+ "        {\n"
    			+ "            \"serverName\": \"sb.cpod.1\",\n"
    			+ "            \"supportSvcList\": [\n"
    			+ "                \"2\",\n"
    			+ "                \"3\"\n"
    			+ "            ],\n"
    			+ "            \"connectionStatus\": true,\n"
    			+ "            \"serverSessionInfo\": [\n"
    			+ "                {\n"
    			+ "                    \"serviceCode\": \"2\",\n"
    			+ "                    \"sessionMaxCnt\": 300,\n"
    			+ "                    \"sessionCnt\": 60,\n"
    			+ "                    \"sessionInfo\": [\n"
    			+ "                        {\n"
    			+ "                            \"sessionIdx\": 0,\n"
    			+ "                            \"inUse\": 0,\n"
    			+ "                            \"responseTime\": 20,\n"
    			+ "                            \"callKey\": \"call_20230517235949_2134_0\",\n"
    			+ "                            \"sessionStartTime\": 1697008660000,\n"
    			+ "                            \"deviceId\": \"4885_20193333\"\n"
    			+ "                        }\n"
    			+ "                    ]\n"
    			+ "                }\n"
    			+ "            ]\n"
    			+ "        },\n"
    			+ "        {\n"
    			+ "            \"serverName\": \"sb.cpod.2\",\n"
    			+ "            \"supportSvcList\": [\n"
    			+ "                \"2\",\n"
    			+ "                \"3\"\n"
    			+ "            ],\n"
    			+ "            \"connectionStatus\": false\n"
    			+ "        }\n"
    			+ "    ]\n"
    			+ "}";
    	return JacksonUtil.jsonToObject(jsonString, SttSessionResponseDto.class);
    }

	public void setEachServerSessionMaxCount(List<SttServerInfoDto> serverInfoList) {
		Integer eachSessionMaxCount = 0;
		
		for (SttServerInfoDto serverInfo : serverInfoList) {
			
			if (serverInfo == null) continue;
			
			List<SttServerSessionInfoDto> serverSessionInfoList = serverInfo.getServerSessionInfo();
			
			if (serverSessionInfoList == null) continue;

			for ( SttServerSessionInfoDto serverSessionInfo : serverSessionInfoList) {
				if (serverSessionInfo == null) continue;
//				if (eachSessionMaxCount == 0 ) {
					eachSessionMaxCount = serverSessionInfo.getSessionMaxCnt() / serverInfoList.size();
//				}
				serverSessionInfo.setSessionMaxCnt(eachSessionMaxCount);
			}
		}
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
