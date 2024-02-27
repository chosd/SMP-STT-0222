package com.kt.smp.multitenancy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.smp.multitenancy.config.DynamicRoutingDataSource;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.dto.ConfigSaveDto;
import com.kt.smp.multitenancy.dto.ConfigUpdateDto;
import com.kt.smp.multitenancy.repository.ConfigRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConfigService {

    private final ConfigRepository repository;
    private final DynamicRoutingDataSource routingDataSource;
    private final DatabaseService databaseService;
    
    @Value("${spring.datasource.master.driver-class-name}")
    private String dbDriverClassName;
    
    @Value("${pagehelper.helper-dialect}")
    private String dbname;
    
    @Value("${cipher.secretKey}")
	private String secretKey;
    
    private static List<ConfigDto> CONFIG_LIST = new ArrayList<ConfigDto>();

    public List<ConfigDto> getAll() {
    	if(CONFIG_LIST.isEmpty()) initConfig();
        return CONFIG_LIST;
    }

    public List<ConfigDto> getAllNotMaster() {
    	
    	if(CONFIG_LIST.isEmpty()) initConfig();
    	return CONFIG_LIST.stream()
    			.filter(c->c.getIsDefault().toUpperCase().equals("N"))
    			.collect(Collectors.toList());
    }
    
    public List<ConfigDto> getAllUserDefined() {
//        return repository.findAllUserDefinedConfig();
    	if(CONFIG_LIST.isEmpty()) initConfig();
    	return CONFIG_LIST.stream()
    			.filter(c->c.getIsDefault().equalsIgnoreCase("N"))
    			.collect(Collectors.toList());
    }
    
    public ConfigDto getByProjectCode(String projectCode) {
//        return repository.findByProjectCode(projectCode);
    	if(CONFIG_LIST.isEmpty()) initConfig();
    	return CONFIG_LIST.stream()
    			.filter(c->c.getProjectCode().equals(projectCode))
    			.findFirst().orElse(null);
    }

    @Transactional
    public void save(ConfigSaveDto newConfig) {

        ConfigDto config = repository.findByProjectCode(newConfig.getProjectCode());

        if (config == null) {
            saveConfig(newConfig);
            return;
        }

        updateConfig(config, newConfig);
        initConfig();
    }

    private void saveConfig(ConfigSaveDto newConfig) {

        newConfig.setDbDriverClassName(dbDriverClassName);
        if(databaseService.isMysqlOrMariadb()) {
        	newConfig.addDefaultOption();
        }

        databaseService.create(newConfig);
        repository.save(newConfig);
        routingDataSource.refresh(repository);
        initConfig();
    }

    private void updateConfig(ConfigDto originConfig, ConfigSaveDto newConfig) {

    	if(databaseService.isMysqlOrMariadb()) {
    		newConfig.addDefaultOption();
    	}

        ConfigUpdateDto modifiedConfig = new ConfigUpdateDto(newConfig);
        modifiedConfig.setId(originConfig.getId());

        databaseService.update(originConfig, newConfig);
        repository.update(modifiedConfig);
        routingDataSource.refresh(repository);
//        initConfig();
    }

    @Transactional
    public void delete(String projectCode) {

        ConfigDto config = repository.findByProjectCode(projectCode);
        if (config == null) {
            throw new IllegalArgumentException("등록되지 않은 서비스 설정입니다");
        }

        if (config.isDefault()) {
            return;
        }

        repository.delete(config.getId());
        routingDataSource.refresh(repository);
        initConfig();
    }

    public ConfigDto getByEngineParameter(String engineParameter) {
//        return repository.findByEngineParameter(engineParameter);
    	if(CONFIG_LIST.isEmpty()) initConfig();
    	return CONFIG_LIST.stream()
    			.filter(c->c.getEngineParameter().equals(engineParameter))
    			.findFirst().orElse(null);
    }
    
    private void initConfig() {
    	CONFIG_LIST.clear();
    	CONFIG_LIST = repository.findAllConfig();
    	for(ConfigDto config : CONFIG_LIST) {
    		config.setEncDbPassword(config.getDbPassword());
    	}
    }
}