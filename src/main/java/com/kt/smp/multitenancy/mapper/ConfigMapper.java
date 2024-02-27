package com.kt.smp.multitenancy.mapper;

import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.dto.ConfigSaveDto;
import com.kt.smp.multitenancy.dto.ConfigUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ConfigMapper {
    List<ConfigDto> findAllConfig();

    List<ConfigDto> findAllUserDefinedConfig();

    ConfigDto findByProjectCode(String projectCode);

    ConfigDto findByEngineParameter(String engineParameter);

    void save(ConfigSaveDto newConfig);

    void update(ConfigUpdateDto modifiedConfig);

    void delete(Integer id);
}
