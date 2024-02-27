package com.kt.smp.multitenancy.config;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.kt.smp.common.util.EncUtil;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.repository.ConfigRepository;

public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
	String secretKey = "";
	
	@Override
	protected Object determineCurrentLookupKey() {
		return TenantContextHolder.getProjectCode();
	}
	
	private void init(ConfigRepository configRepository) {
		List<ConfigDto> configList = configRepository.findAllConfig();
		for(ConfigDto configDto : configList) {
			decDbPw(configDto);
		}
		setDefaultDataSource(configList);
		setDataSources(configList);
		for(ConfigDto configDto : configList) {
			configDto.setDbPassword(configDto.getEncDbPassword());
		}
	}

	public void init(ConfigRepository configRepository, String secretKey) {
		this.secretKey = secretKey;
		init(configRepository);
	}

	private void setDefaultDataSource(List<ConfigDto> configList) {
		ConfigDto defaultConfig = configList.stream()
				.filter(ConfigDto::isDefault)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("기본 데이터소스가 존재하지 않습니다"));
		
		setDefaultTargetDataSource(defaultConfig.buildDatasource());
	}

	private void setDataSources(List<ConfigDto> configList) {
		Map<Object, Object> dataSourceMap = configList.stream()
				.collect(Collectors.toMap(ConfigDto::getProjectCode, ConfigDto::buildDatasource));

		setTargetDataSources(dataSourceMap);
	}

	public void refresh(ConfigRepository configRepository) {
		init(configRepository);
		afterPropertiesSet();
	}
	
	private void decDbPw(ConfigDto configDto) {
		String dbPw = configDto.getDbPassword();
		configDto.setEncDbPassword(dbPw);
		String decPw = dbPw;
		String decUser = configDto.getDbUsername();
		
		try {
			if(secretKey.equals("opaque")) {
				byte[] byteStr = Base64.decodeBase64(dbPw.getBytes("UTF-8"));
				decPw = new String(byteStr, "UTF-8");
				
				byteStr = Base64.decodeBase64(dbPw.getBytes("UTF-8"));
				decUser = new String(byteStr, "UTF-8");
			}
			else {
				decPw = EncUtil.decAES128(dbPw, secretKey);
			}
//			System.out.println(">>> tenant pw : " + dbPw + ", dec : " + decPw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		configDto.setDbUsername(decUser);
		configDto.setDbPassword(decPw);
	}
}
