/**
 * 
 */
package com.kt.smp.stt.comm.preference.service.impl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.kt.smp.stt.comm.preference.dto.AgentConfigDto;
import com.kt.smp.stt.comm.preference.dto.PreferenceValuesDto;
import com.kt.smp.stt.comm.preference.dto.PreferenceCodeDto;
import com.kt.smp.stt.comm.preference.dto.EtcConfigDto;
import com.kt.smp.stt.comm.preference.dto.RemoveListDto;
import com.kt.smp.stt.comm.preference.dto.SchedulerConfigDto;
import com.kt.smp.stt.comm.preference.dto.SchedulerConfigUpdateDto;
import com.kt.smp.stt.comm.preference.dto.ThresholdConfigDto;
import com.kt.smp.stt.comm.preference.enums.DatasToRemove;
import com.kt.smp.stt.comm.preference.enums.FilesToRemove;
import com.kt.smp.stt.comm.preference.service.PreferenceService;

/**
 * DB를 직접 조작하는 테스트이므로 사용 금지 (개발용)
 */
@SpringBootTest(properties = "spring.profiles.active=local")
@ActiveProfiles("local")
public class PreferenceServiceTest {
//	
//	private final PreferenceService preferenceService;
//	
//	@Autowired
//	public PreferenceServiceTest(PreferenceService preferenceService) {
//		this.preferenceService = preferenceService; 
//	}
//	
//	@Test
//	@DisplayName("CODE_KEY 조회")
//	void findCategoryList() {
//		List<PreferenceCodeDto> result = preferenceService.findCategoryList();
//		
//		assertThat(result.size()).isGreaterThan(0);
//	}
//	
//	@Test
//	@DisplayName("환경 설정 모두 조회")
//	void getAllConfigValue() {
//		PreferenceValuesDto result = preferenceService.getAllConfigValue();
//		
//		SchedulerConfigDto schedulerConfig = result.getSchedulerConfig();
//		ThresholdConfigDto thresholdConfig = result.getThresholdConfig();
//		EtcConfigDto etcConfig = result.getEtcConfig();
//		
//		assertThat(schedulerConfig).isNotNull();
//		assertThat(thresholdConfig).isNotNull();
//		assertThat(etcConfig).isNotNull();
//		
//	}
//	
//	@Test
//	@DisplayName("스케쥴러 설정 - 파일 모두 조회")
//	void getRemoveList() {
//		RemoveListDto result = preferenceService.getRemoveList();
//		System.out.println("result >>> " + result.toString());
//		
//		
//		assertThat(result.getFilesToRemoveList()).containsExactlyInAnyOrder(
//				FilesToRemove.AM_TRAIN_DATA
//				, FilesToRemove.CALL_INFO
//				, FilesToRemove.DEPLOY_MODEL
//				, FilesToRemove.TEST
//				, FilesToRemove.VERIFY_DATA
//				);
//		
//		assertThat(result.getDatasToRemoveList()).containsExactlyInAnyOrder(
//				DatasToRemove.AM_TRAIN_DATA
//				, DatasToRemove.CALLINFO
//				, DatasToRemove.DEPLOY_MNG
//				, DatasToRemove.DEPLOY_MODEL
//				, DatasToRemove.LM_TRAIN_DATA
//				, DatasToRemove.TRAIN_MNG
//				, DatasToRemove.VERIFY_DATA
//				, DatasToRemove.VERIFY_MNG
//				, DatasToRemove.STT_RESULT
//				, DatasToRemove.HW_RESOURCE
//				, DatasToRemove.STATISTICS
//				);
//	}
//	
//	@Test
//	@DisplayName("스케쥴러 설정 - 수정")
//	void updateSchedulerValues() {
//		// given
//		SchedulerConfigUpdateDto dto = new SchedulerConfigUpdateDto();
//		
//		List<String> removerFileList = new ArrayList<>();
//		List<String> removerDataList = new ArrayList<>();
//		
//		removerFileList.add(FilesToRemove.AM_TRAIN_DATA.getDescription());
//		removerFileList.add(FilesToRemove.CALL_INFO.getDescription());
//		removerFileList.add(FilesToRemove.VERIFY_DATA.getDescription());
//		
//		removerDataList.add(DatasToRemove.DEPLOY_MNG.getDescription());
//		removerDataList.add(DatasToRemove.VERIFY_DATA.getDescription());
//		removerDataList.add(DatasToRemove.TRAIN_MNG.getDescription());
//		
//		dto.setHwResourceCycle(10);
//		dto.setStatisticsCycle(5);
//		dto.setRemoverStandard(31);
//		dto.setRemoverTime("3");
//		dto.setSchedulerUseYn("N");
//		dto.setRemoverFileList(removerFileList);
//		dto.setRemoverDataList(removerDataList);
//		
//		//when
//		preferenceService.updateSchedulerConfigValues(dto);
//		SchedulerConfigDto result = preferenceService.getSchedulerConfigValues();
//		
//		//then
//		assertThat(result.getHwResourceCycle()).isEqualTo("10");
//		assertThat(result.getStatisticsCycle()).isEqualTo("5");
//		assertThat(result.getRemoverStandard()).isEqualTo("31");
//		assertThat(result.getRemoverTime()).isEqualTo("3");
//		assertThat(result.getSchedulerUseYn()).isEqualTo("N");
//		
//		assertThat(result.getRemoverFileList()).containsExactlyInAnyOrder(
//				FilesToRemove.AM_TRAIN_DATA
//				, FilesToRemove.CALL_INFO
//				, FilesToRemove.VERIFY_DATA
//				);
//		
//		assertThat(result.getRemoverDataList()).containsExactlyInAnyOrder(
//				DatasToRemove.DEPLOY_MNG
//				, DatasToRemove.VERIFY_DATA
//				, DatasToRemove.TRAIN_MNG
//				);
//	}
//	
//	@Test
//	@DisplayName("HW 리소스 임계치 - 조회")
//	void getThresholdValues() {
//		ThresholdConfigDto result = preferenceService.getThresholdConfigValues();
//		assertThat(result.getCpu()).isNotNull();
//		assertThat(result.getMemory()).isNotNull();
//		assertThat(result.getStorage()).isNotNull();
//	}
//	
//	@Test
//	@DisplayName("HW 리소스 임계치 - 수정")
//	void updateThresholdValues() {
//		ThresholdConfigDto request = new ThresholdConfigDto();
//		request.setCpu(60);
//		request.setMemory(41);
//		request.setStorage(30);
//		preferenceService.updateThresholdConfigValues(request);
//		
//		ThresholdConfigDto result = preferenceService.getThresholdConfigValues();
//		assertThat(result.getCpu()).isEqualTo(60);
//		assertThat(result.getMemory()).isEqualTo(41);
//		assertThat(result.getStorage()).isEqualTo(30);
//	}
//	
//	@Test
//	@DisplayName("기타 설정 - 조회")
//	void getEtcConfigValues() {
//		EtcConfigDto result = preferenceService.getEtcConfigConfigValues();
//		
//		// 숫자여야함
//		assertDoesNotThrow(()->{Integer.parseInt(result.getRecordCount());});
//		// NULL이 아니어야함
//		assertThat(result.getRecordCount()).isNotNull();
//	}
//	
//	@Test
//	@DisplayName("기타 설정 - 수정")
//	void updateScreenValues() {
//		EtcConfigDto request = new EtcConfigDto();
//		request.setRecordCount("5");
//		preferenceService.updateEtcConfigValues(request);
//
//		EtcConfigDto result = preferenceService.getEtcConfigConfigValues();
//		assertThat(result.getRecordCount()).isEqualTo("5");
//	}
//	
//	@Test
//	@DisplayName("Agent 설정 - 조회")
//	void getAgentConfigValues() {
//		AgentConfigDto result = preferenceService.getAgentConfigValues();
//                     
//		assertThat(result.getMultipartHostDeploy()).isIn("Y", "N");
//		assertThat(result.getMultipartHostTest()).isIn("Y", "N");
//		assertThat(result.getMultipartSubDeploy()).isIn("Y", "N");
//		assertThat(result.getMultipartSubTest()).isIn("Y", "N");
//		
//	}
//	
//	@Test
//	@DisplayName("Agent 설정 - 수정")
//	void updateAgentConfigValues() {
//		
//		AgentConfigDto request = new AgentConfigDto();
//		request.setMultipartHostDeploy("N");
//		request.setMultipartHostTest("Y");
//		request.setMultipartSubDeploy("Y");
//		request.setMultipartSubTest("N");
//		
//		preferenceService.updateAgentConfigValues(request);
//		
//		
//		AgentConfigDto result = preferenceService.getAgentConfigValues();
//                     
//		assertThat(result.getMultipartHostDeploy()).isEqualTo("N");
//		assertThat(result.getMultipartHostTest()).isEqualTo("Y");
//		assertThat(result.getMultipartSubDeploy()).isEqualTo("Y");
//		assertThat(result.getMultipartSubTest()).isEqualTo("N");
//		
//	}
	
}
