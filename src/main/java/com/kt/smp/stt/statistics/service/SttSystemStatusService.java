/**
 * 
 */
package com.kt.smp.stt.statistics.service;

import java.util.ArrayList;
import java.util.List;

import static com.kt.smp.stt.comm.preference.PreferenceValueHolder.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.kt.smp.config.CommonConstants;
import com.kt.smp.ktsttengine.adapter.SttSystemStatusAdapter;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.common.ErrorTypeCode;
import com.kt.smp.stt.error.dto.SttErrorInsertDto;
import com.kt.smp.stt.error.service.SttErrorService;
import com.kt.smp.stt.monitoring.dto.SystemStatusSearchRequestDto;
import com.kt.smp.stt.monitoring.dto.SystemStatusSearchResult;
import com.kt.smp.stt.statistics.domain.SttServerInfoVO;
import com.kt.smp.stt.statistics.dto.SttSystemStatusResponseDto;
import com.kt.smp.stt.statistics.repository.SttSystemStatusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
*@FileName : SttSystemStatus.java
@Project : kt-stt-service_r
@Date : 2023. 10. 13.
*@작성자 : 심수연
*@변경이력 :
*@프로그램설명 :
*/
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SttSystemStatusService {
	
	private final SttSystemStatusAdapter sttSystemStatusAdapter;
	private final SttSystemStatusRepository sttSystemStatusRepository;
	private final SttErrorService sttErrorService;
	private final ConfigService configService;
//	
//	@Value("${threshold.error.cpu-used}")
//	private int cpuUsedThreshold;
//	
//	@Value("${threshold.error.free-memory-size}")
//	private int freeMemorySizeThreshold;
//	
//	@Value("${threshold.error.free-app-storage-size}")
//	private int freeAppStorageSizeThreshold;
//	
	/**
	 * HW 리소스 조회
	 * @param 
	 * @return SttSystemResponseDto
	 * @author 심수연
	 * @since 2023.10.13
	 */
	public SttSystemStatusResponseDto getSystemStatus(String projectCode) {
		List<ConfigDto> configDtos = configService.getAllUserDefined();
		
    	for (ConfigDto configDto : configDtos) {
    		TenantContextHolder.set(configDto.getProjectCode());	
    	}
		int count = 0;
		SttSystemStatusResponseDto apiResult = sttSystemStatusAdapter.getSystemStatus();	
		try {
			for(SttServerInfoVO serverInfo : apiResult.getServerInfo()) {
				count = sttSystemStatusRepository.exists(serverInfo);

				if (count < 1) {
					serverInfo.setRegId(CommonConstants.REG_ID);
					serverInfo.setRegIp(CommonConstants.LOCAL_IP);
					sttSystemStatusRepository.insert(serverInfo);
					
					List<SttErrorInsertDto> sttErrorInsertDtoList =loadCheck(serverInfo, projectCode);
					if (sttErrorInsertDtoList.size() > 0 ) {
						sttErrorService.insert(sttErrorInsertDtoList);
					}
				}
			}
		} catch (Exception e) {
			log.error("[ERROR] getSttSystemStatus - Insert stt request system status error : {}",
					e.getMessage());
			e.printStackTrace();
		}
		return apiResult;
	}
	
	
	/**
	*@MethodName : loadCheck
	*@작성일 : 2023. 11. 1.
	*@작성자 : wonyoung.ahn
	*@변경이력 :
	*@Method설명 : 부하여부 확인
	*@param serverInfo
	*@return
	*/
	public List<SttErrorInsertDto> loadCheck(SttServerInfoVO serverInfo, String projectCode) {
		List<SttErrorInsertDto> sttErrorInsertDtoList = new ArrayList<>();
		if(serverInfo.getCpuUsed() > cpuUsedThreshold.get(projectCode)) {
			log.info("[SttSystemStatusService.loadCheck] cpu used : {}%, threshold: {}", serverInfo.getCpuUsed(), cpuUsedThreshold.get(projectCode));
			SttErrorInsertDto sttErrorInsertDto = new SttErrorInsertDto();
			sttErrorInsertDto.setType(ErrorTypeCode.SYSTEM);
			sttErrorInsertDto.setServerId(serverInfo.getServerName());
			sttErrorInsertDto.setErrorPoint(ErrorTypeCode.CPU);
			sttErrorInsertDto.setThreshold(cpuUsedThreshold.get(projectCode));
			sttErrorInsertDto.setStatusValue(serverInfo.getCpuUsed());
			sttErrorInsertDtoList.add(sttErrorInsertDto);
		}
		if(serverInfo.getFreeMemorySize()/serverInfo.getMaxMemorySize() > freeMemorySizeThreshold.get(projectCode)) {
			log.info("[SttSystemStatusService.loadCheck] free memory : {}%, threshold: {}", serverInfo.getFreeMemorySize()/serverInfo.getMaxMemorySize(), freeMemorySizeThreshold.get(projectCode));
			SttErrorInsertDto sttErrorInsertDto = new SttErrorInsertDto();
			sttErrorInsertDto.setType(ErrorTypeCode.SYSTEM);
			sttErrorInsertDto.setServerId(serverInfo.getServerName());
			sttErrorInsertDto.setErrorPoint(ErrorTypeCode.MEMORY);
			sttErrorInsertDto.setThreshold(freeMemorySizeThreshold.get(projectCode));
			sttErrorInsertDto.setStatusValue((serverInfo.getFreeMemorySize()/serverInfo.getMaxMemorySize())*100);
			sttErrorInsertDtoList.add(sttErrorInsertDto);
		}
		if(serverInfo.getFreeAppStorageSize()/serverInfo.getMaxAppStorageSize() > freeAppStorageSizeThreshold.get(projectCode)) {
			log.info("[SttSystemStatusService.loadCheck] free storage : {}%, threshold: {}", serverInfo.getFreeAppStorageSize()/serverInfo.getMaxAppStorageSize(), freeAppStorageSizeThreshold.get(projectCode));
			SttErrorInsertDto sttErrorInsertDto = new SttErrorInsertDto();
			sttErrorInsertDto.setType(ErrorTypeCode.SYSTEM);
			sttErrorInsertDto.setServerId(serverInfo.getServerName());
			sttErrorInsertDto.setErrorPoint(ErrorTypeCode.STORAGE);
			sttErrorInsertDto.setThreshold(freeAppStorageSizeThreshold.get(projectCode));
			sttErrorInsertDto.setStatusValue((serverInfo.getFreeAppStorageSize()/serverInfo.getMaxAppStorageSize())*100);
			sttErrorInsertDtoList.add(sttErrorInsertDto);
		}
		return sttErrorInsertDtoList;
	}
	
	public Page<SystemStatusSearchResult> listPage(SystemStatusSearchRequestDto searchCondition){
		
		//페이지 조회
		Page<SystemStatusSearchResult> page = null;
		Integer total;
		
		if(searchCondition.getSearchUnit().equals("RAW")) {
			page = sttSystemStatusRepository.listPage(searchCondition);
			total = sttSystemStatusRepository.count(searchCondition);
		} else {
			page = sttSystemStatusRepository.listByDate(searchCondition);
			total = sttSystemStatusRepository.countByDate(searchCondition);			
		}
		page.pageNum(searchCondition.getPage());
		page.pageSize(searchCondition.getPageSize());
		page.setTotal(total);
		
		return page;
	}
	
	public int generateSystemStatusStatHour() {
		return sttSystemStatusRepository.generateSystemStatusStatHour();
	}
	
	public List<SystemStatusSearchResult> lastData() {
		return sttSystemStatusRepository.lastData();
	}

	public List<SystemStatusSearchResult> getRealtimeChart() {
		return sttSystemStatusRepository.getRealtimeChart();
	}
	
}
