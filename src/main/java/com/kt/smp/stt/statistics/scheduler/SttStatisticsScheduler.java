package com.kt.smp.stt.statistics.scheduler;

import static com.kt.smp.stt.comm.preference.PreferenceValueHolder.intervalRequestStat;
import static com.kt.smp.stt.comm.preference.PreferenceValueHolder.intervalSystemStatus;
import static com.kt.smp.stt.comm.preference.PreferenceValueHolder.removerTime;
import static com.kt.smp.stt.comm.preference.PreferenceValueHolder.schedulerActive;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.kt.smp.common.util.JacksonUtil;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.common.EngineUrlResolver;
import com.kt.smp.stt.remover.FileRemover;
import com.kt.smp.stt.reprocess.service.SttReprocessService;
import com.kt.smp.stt.statistics.domain.SttStatisticsCallInfo;
import com.kt.smp.stt.statistics.domain.SttStatisticsDetailSearchConditionDto;
import com.kt.smp.stt.statistics.domain.SttStatisticsErrorVO;
import com.kt.smp.stt.statistics.domain.SttStatisticsResponseDto;
import com.kt.smp.stt.statistics.domain.SttStatisticsServerInfoVO;
import com.kt.smp.stt.statistics.domain.SttStatisticsVO;
import com.kt.smp.stt.statistics.service.SttStatisticsErrorService;
import com.kt.smp.stt.statistics.service.SttStatisticsService;
import com.kt.smp.stt.statistics.service.SttSystemStatusService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

/**
 * @author jaime
 * @title SttStatisticsScheduler
 * @see\n <pre>
 * </pre>
 * @since 2022-07-22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SttStatisticsScheduler {

    private static final String CORE_STATISTICS_URL = "/stt/statistics/info/all?date={date}";

    private static final String _SCHEDULER = "_SCHEDULER";

    private final RestTemplate restTemplate;

    private final ConfigService configService;

    private final EngineUrlResolver engineUrlResolver;

    private final SttStatisticsService sttStatisticsService;

    private final SttStatisticsErrorService sttStatisticsErrorService;
    
    private final SttSystemStatusService sttSystemStatusService;
    
    private final SttReprocessService sttReprocessService;
    
    private final FileRemover fileRemover;

    @Value("${spring.profiles.active}")
    private String profile;
	@Value("${scheduler.reprocess.hour}")
	private int reprocessHour;
	
	private static int LAST_SYSTEM_STATUS_CHECK_TIME = -1;	
	
	/**
	*@MethodName : startScheduler4Seconds
	*@작성일 : 2023.10.13
	*@작성자 : 심수연
	*@변경이력 :
	*@Method설명 : HW 리소스 조회 스케쥴러
	*/
	@Scheduled(cron = "* * * * * ?") // * * * * * ? : 매 초마다 실행
    // @SchedulerLock(name = "SttStatisticsScheduler_startScheduler4Seconds", lockAtLeastFor = "PT1S", lockAtMostFor = "PT1S")
	public void startScheduler4Seconds() {
		for (ConfigDto tenentDto : configService.getAllNotMaster()) {
			String projectCode = tenentDto.getProjectCode();
			TenantContextHolder.set(projectCode);
			if(projectCode != null && schedulerActive.get(projectCode) && intervalSystemStatus.get(projectCode) != 0) getSystemStatus(projectCode);
		}
	}
	
	@Scheduled(cron = "0 0 * * * ?") // 0 0 * * * ? : 매 시간마다 실행
	@SchedulerLock(name = "SttStatisticsScheduler_startSchedulerHour", lockAtLeastFor = "PT59M", lockAtMostFor = "PT59M")
	public void startSchedulerHour() {
		for (ConfigDto tenentDto : configService.getAllNotMaster()) {
			String projectCode = tenentDto.getProjectCode();
			TenantContextHolder.set(projectCode);
			if(projectCode != null && schedulerActive.get(projectCode)) {
				generateSystemStatusStatHour();
				
				startReprocessScheduler();
				startRemoveOldFiles(projectCode);
			}
		}
	}
	
	private void startReprocessScheduler() {
		if (!isHourToReprocess()) return;
		
		try {
			sttReprocessService.autoReprocess();
		} catch(Exception e) {
			log.error("Fail to auto reprocess -- {}", e);
		}
	}
	
	private boolean isHourToReprocess() {
		LocalTime now = LocalTime.now();
		return now.getHour() == reprocessHour;
	}
	
	private void startRemoveOldFiles(String projectCode) {
		if (!isHourToRemoveOldFiles(projectCode)) return;
		
		try {
			fileRemover.run(projectCode);
		} catch(Exception e) {
			log.error("Fail to remove old files -- {}", e);
		}
	}
	
	private boolean isHourToRemoveOldFiles(String projectCode) {
		LocalTime now = LocalTime.now();

		return now.getHour() == removerTime.get(projectCode);
	}

	/**
	*@MethodName : getSystemStatus
	*@작성일 : 2023.10.13
	*@작성자 : 심수연
	*@변경이력 :
	*@Method설명 : HW 리소스 조회
	*/
	public void getSystemStatus(String projectCode) {
		LocalDateTime nowTime = LocalDateTime.now();
		int second = nowTime.getSecond();
		boolean inTime = false;

		if(LAST_SYSTEM_STATUS_CHECK_TIME == -1) LAST_SYSTEM_STATUS_CHECK_TIME = second;
		else {
			if(second >= LAST_SYSTEM_STATUS_CHECK_TIME) {
				if(second - LAST_SYSTEM_STATUS_CHECK_TIME >= intervalSystemStatus.get(projectCode)) {
					LAST_SYSTEM_STATUS_CHECK_TIME = second;
					inTime = true;
				}
			}
			else {
				if(second + 60 - LAST_SYSTEM_STATUS_CHECK_TIME >= intervalSystemStatus.get(projectCode)) {
					LAST_SYSTEM_STATUS_CHECK_TIME = second;
					inTime = true;
				}
			}
		}
		
		if(inTime) {
				sttSystemStatusService.getSystemStatus(projectCode);
		}
	}
	
	public void generateSystemStatusStatHour() {

		sttSystemStatusService.generateSystemStatusStatHour();
			
	}
    // 통계 데이터 가져오는 스케줄러(임시로 막음)
//    @Scheduled(fixedRate = 60 * 1000)
	
	@Scheduled(cron = "0 * * * * ?") // 0 * * * * ? : 매 분의 00초마다 실행
    @SchedulerLock(name = "SttStatisticsScheduler_startSchedulerMinutes", lockAtLeastFor = "PT59S", lockAtMostFor = "PT59S")
		public void startSchedulerMinutes() throws UnknownHostException {
		
		for (ConfigDto configDto : configService.getAllNotMaster()) {
			String projectCode = configDto.getProjectCode();
			log.info(">>> [SttStatisticsScheduler.startSchedulerMinutes] projectCode : {} ", projectCode);
			
			// projectCode 가 NULL 일 수 있는데 무조건 호출하면 안 됨
			// JHIL 2024.02.06
			if(projectCode != null  
					&& schedulerActive.get(projectCode) 
					&& intervalRequestStat.get(projectCode) != 0) callServerInfo(projectCode);
		}

	}
	
    public void callServerInfo(String projectCode) throws UnknownHostException {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String prevDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        if (date.startsWith("0000", 8)) {
            insertStatisticsInfo(prevDate, projectCode);
        } else {
            insertStatisticsInfo(date.substring(0, 8), projectCode);
        }
    }

    public void insertStatisticsInfo(String date, String projectCode) throws UnknownHostException {

    	SttStatisticsResponseDto responseDto = callApi(date, projectCode);
    	
    	log.info("responseDto >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(responseDto));
        if (ObjectUtils.isEmpty(responseDto)) {
            log.error("[SttStatisticsScheduler Scu#1] Api Call Unsuccessful");
            return;
        }

        if (apiCallNotSuccessful(responseDto)) {
            log.error("[SttStatisticsScheduler Scu#1 ERROR] {}", responseDto.getResultMsg());
            return;
        }
        
        TenantContextHolder.set(projectCode);

        String ip = InetAddress.getLocalHost().getHostAddress();
        
        for (SttStatisticsServerInfoVO serverInfo : responseDto.getServerInfo()) {
        	// 서비스 로직 파악 필요
//                if (serverInfo.getSupportSvcList().size() != serverInfo.getCompleteSvcList().size()) {
//                    insertStatisticsError(responseDto, ip);
//                }

            boolean differentDay = responseDto.getResDateTime().startsWith("0001", 8);
            log.info("differentDay >>> {}", differentDay);
            for (SttStatisticsCallInfo callInfo : serverInfo.getCallInfo()) {
                SttStatisticsVO prevMinuteStatisticsVO
                        = getPrevMinuteStatisticsVO(responseDto.getResDateTime(), serverInfo.getServerName(), callInfo.getServiceCode());
                log.info("prevMinuteStatisticsVO >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(prevMinuteStatisticsVO));
                String regDt = getRegDt(responseDto.getResDateTime());
                int requestCount = callInfo.getRequestCount();
                int completeCount = callInfo.getCompleteCount();
                int failCount = callInfo.getFailCount();
                // busyCallCount와 processingCount는 같은 뜻으로 쓴다.
                int busyCallCount = callInfo.getProcessingCount();
                if(!differentDay && prevMinuteStatisticsVO != null) {
                	if(requestCount > prevMinuteStatisticsVO.getRequestCount()) {
                		requestCount = requestCount - prevMinuteStatisticsVO.getRequestCount();
                	} else requestCount = 0;
                	if(completeCount > prevMinuteStatisticsVO.getCompleteCount()) {
                		completeCount = completeCount - prevMinuteStatisticsVO.getCompleteCount();
                	} else completeCount = 0;
                	if(failCount > prevMinuteStatisticsVO.getFailCount()) {
                		failCount = failCount - prevMinuteStatisticsVO.getFailCount();

                	} else failCount = 0;
                	if(busyCallCount > prevMinuteStatisticsVO.getBusyCallCount()) {
                		busyCallCount = busyCallCount - prevMinuteStatisticsVO.getBusyCallCount();
                	} else busyCallCount = 0;
                }
                
                SttStatisticsVO sttStatisticsVO = SttStatisticsVO.builder()
                        .resDateTime(responseDto.getResDateTime())
                        .supportSvcList(serverInfo.getSupportSvcList())
                        .serverName(serverInfo.getServerName())
                        .serviceCode(callInfo.getServiceCode())
                        .requestCount(requestCount)
                        .completeCount(completeCount)
                        .failCount(failCount)
                        .busyCallCount(busyCallCount)
                        .regDt(regDt)
                        .regId(serverInfo.getServerName())
                        .regIp(ip)
                        .updDt(regDt)
                        .updId(serverInfo.getServerName())
                        .updIp(ip)
                        .totalServerCnt(responseDto.getTotalServerCnt())
                        .completeServerCnt(responseDto.getCompleteServerCnt())
                        .build();
                log.info("sttStatisticsVO >>> {}",JacksonUtil.objectToJsonWithPrettyPrint(sttStatisticsVO));
                try {
                    TenantContextHolder.set(projectCode);

                    int exists = sttStatisticsService.exists(SttStatisticsDetailSearchConditionDto.builder()
                            .regDt(regDt)
                            .serviceCode(callInfo.getServiceCode())
                            .serverName(serverInfo.getServerName())
                            .resDateTime(responseDto.getResDateTime())
                            .build());

                    if (exists < 1) {
                        sttStatisticsService.insert(sttStatisticsVO);
                    }
                } catch (Exception e) {
                	log.error("error : {}",e);
                    log.debug("[SttStatisticsScheduler Scu#1] duplicate info inserted");
                }
            }
        }
    }
    
    // postman으로 테스트하기 위한 서비스(데이터 저장용)
    public void insertStatisticsInfo(SttStatisticsResponseDto responseDto) throws UnknownHostException {
        List<ConfigDto> configDtos = configService.getAllUserDefined();
        for (ConfigDto configDto : configDtos) {
        	TenantContextHolder.set(configDto.getProjectCode());

            String ip = InetAddress.getLocalHost().getHostAddress();
            for (SttStatisticsServerInfoVO serverInfo : responseDto.getServerInfo()) {

                boolean differentDay = responseDto.getResDateTime().startsWith("0001", 8);

                for (SttStatisticsCallInfo callInfo : serverInfo.getCallInfo()) {
                    SttStatisticsVO prevMinuteStatisticsVO
                            = getPrevMinuteStatisticsVO(responseDto.getResDateTime(), serverInfo.getServerName(), callInfo.getServiceCode());
                    String regDt = getRegDt(responseDto.getResDateTime());
                    int requestCount = callInfo.getRequestCount();
                    int completeCount = callInfo.getCompleteCount();
                    int failCount = callInfo.getFailCount();
                    int processingCount = callInfo.getProcessingCount();

                    SttStatisticsVO sttStatisticsVO = SttStatisticsVO.builder()
                            .resDateTime(responseDto.getResDateTime())
                            .supportSvcList(serverInfo.getSupportSvcList())
                            .serverName(serverInfo.getServerName())
                            .serviceCode(callInfo.getServiceCode())
                            .requestCount(differentDay || prevMinuteStatisticsVO == null
                                    ? requestCount : requestCount - prevMinuteStatisticsVO.getRequestCount())
                            .completeCount(differentDay || prevMinuteStatisticsVO == null
                                    ? completeCount : completeCount - prevMinuteStatisticsVO.getCompleteCount())
                            .failCount(differentDay || prevMinuteStatisticsVO == null
                                    ? failCount : failCount - prevMinuteStatisticsVO.getFailCount())
                            .busyCallCount(processingCount)
                            .regDt(regDt)
                            .regId(serverInfo.getServerName())
                            .regIp(ip)
                            .updDt(regDt)
                            .updId(serverInfo.getServerName())
                            .updIp(ip)
                            .totalServerCnt(responseDto.getTotalServerCnt())
                            .completeServerCnt(responseDto.getCompleteServerCnt())
                            .build();
                    log.info("sttStatisticsVO >>> {}",JacksonUtil.objectToJsonWithPrettyPrint(sttStatisticsVO));
//                    try {
//                        TenantContextHolder.set(configDto.getProjectCode());

                        int exists = sttStatisticsService.exists(SttStatisticsDetailSearchConditionDto.builder()
                                .regDt(regDt)
                                .serviceCode(callInfo.getServiceCode())
                                .serverName(serverInfo.getServerName())
                                .resDateTime(responseDto.getResDateTime())
                                .build());
                        log.info("exists >>> {}",exists);
                        if (exists < 1) {
                            sttStatisticsService.insert(sttStatisticsVO);
                        }
//                    } catch (Exception e) {
//                        log.debug("[SttStatisticsScheduler Scu#1] duplicate info inserted");
//                    }
                }
            }
        }
    }

    public SttStatisticsResponseDto callApi(String date, String projectCode) {
//        String url = profile.equals(CommonConstants.LOCAL_PROFILE) ? engineUrlResolver.resolve() : engineUrlResolver.resolve(projectCode);
    	String url = engineUrlResolver.resolve(null);
        String statisticsUrl = url + CORE_STATISTICS_URL;
        ResponseEntity<SttStatisticsResponseDto> responseEntity = null;
        if(url.contains("https")) {
		    ignoreSSL();
		}
        try {
            responseEntity = restTemplate.getForEntity(statisticsUrl
                    , SttStatisticsResponseDto.class
                    , date);
        } catch (Exception e) {
            log.error("[SttStatisticsScheduler ERROR] {}", e.getMessage());
        }

        return ObjectUtils.isNotEmpty(responseEntity) ? responseEntity.getBody() : null;
    }

    private boolean apiCallNotSuccessful(SttStatisticsResponseDto responseDto) {
        return !responseDto.getResultCode().equals("0000");
    }

    private void insertStatisticsError(SttStatisticsResponseDto responseDto, String ip) {
        for (SttStatisticsServerInfoVO serverInfo : responseDto.getServerInfo()) {
            for (String serviceCode : serverInfo.getSupportSvcList()) {
//                if (scuInfo.getCompleteSvcList().contains(serviceCode)) {
//                    continue;
//                }

                String regDt = getRegDt(responseDto.getResDateTime());

                SttStatisticsErrorVO sttStatisticsErrorVO = SttStatisticsErrorVO.builder()
                        .serviceCode(serviceCode)
                        .resDateTime(responseDto.getResDateTime())
                        .serverName(serverInfo.getServerName())
                        .regDt(regDt)
                        .regId(serverInfo.getServerName() + _SCHEDULER)
                        .regIp(ip)
                        .updDt(regDt)
                        .updId(serverInfo.getServerName() + _SCHEDULER)
                        .updIp(ip)
                        .build();

                try {
                    sttStatisticsErrorService.insert(sttStatisticsErrorVO);
                } catch (Exception e) {
                	log.error("error : {}",e);
                    log.debug("[SttStatisticsErrorService Scu#1] duplicate info inserted");
                }
            }
        }
    }

    private SttStatisticsVO getPrevMinuteStatisticsVO(String resDateTime, String serverName, String serviceCode) {
        String dateTime = getPrevMinute(resDateTime).format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        SttStatisticsDetailSearchConditionDto searchCondition = SttStatisticsDetailSearchConditionDto.builder()
                .from(dateTime.substring(0, 8) + "0001")
                .to(dateTime)
                .serverName(serverName)
                .serviceCode(serviceCode)
                .build();

        return sttStatisticsService.detail(searchCondition);
    }

    private String getRegDt(String resDateTime) {
        LocalDateTime prevMinute = getPrevMinute(resDateTime);

        return prevMinute.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    private LocalDateTime getPrevMinute(String resDateTime) {
        int year = Integer.valueOf(resDateTime.substring(0, 4));
        int month = Integer.valueOf(resDateTime.substring(4, 6));
        int day = Integer.valueOf(resDateTime.substring(6, 8));
        int hour = Integer.valueOf(resDateTime.substring(8, 10));
        int minute = Integer.valueOf(resDateTime.substring(10, 12));
        LocalDateTime prevMinute = LocalDateTime.of(year, month, day, hour, minute, 0, 0).minusMinutes(1);

        return prevMinute;
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
