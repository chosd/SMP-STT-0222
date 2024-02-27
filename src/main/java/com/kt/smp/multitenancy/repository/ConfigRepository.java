package com.kt.smp.multitenancy.repository;

import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.dto.ConfigSaveDto;
import com.kt.smp.multitenancy.dto.ConfigUpdateDto;
import com.kt.smp.multitenancy.mapper.ConfigMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ConfigRepository {

    private final ConfigMapper mapper;

    public ConfigRepository(@Qualifier("masterSqlSession") SqlSession sqlSession) {
        mapper = sqlSession.getMapper(ConfigMapper.class);
    }

    public List<ConfigDto> findAllConfig() {
        return mapper.findAllConfig();
    }

    public List<ConfigDto> findAllUserDefinedConfig() {
        return mapper.findAllUserDefinedConfig();
    }

    public ConfigDto findByProjectCode(String projectCode) {
        return mapper.findByProjectCode(projectCode);
    }

    public ConfigDto findByEngineParameter(String engineParameter) {
        return mapper.findByEngineParameter(engineParameter);
    }

    public void save(ConfigSaveDto newConfig) {
        mapper.save(newConfig);
    }

    public void update(ConfigUpdateDto modifiedConfig) {
        mapper.update(modifiedConfig);
    }

    public void delete(Integer id) {
        mapper.delete(id);
    }
}
