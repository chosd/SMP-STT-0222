package com.kt.smp.stt.common;

import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EngineUrlResolver {

    @Value("${core.stt.protocol}")
    private String protocol;

    @Value("${core.stt.host}")
    private String host;
    
    @Value("${core.stt.host-deploy}")
    private String hostDeploy;

    private final ConfigService configService;

    public String resolve() {
        return resolve(null);
    }
    
    public String resolveSub() {
        return resolveSub(null);
    }
    
    public String resolve(String projectCode) {

        return protocol + "://" + host;

//        if (StringUtils.isBlank(projectCode)) {
//            return protocol + "://" + host;
//        }
//
//        return addParamToUrl(projectCode);
    }
    
    public String resolveSub(String projectCode) {
    	
    	return protocol + "://" + hostDeploy;
    }

    private String addParamToUrl(String projectCode) {

        ConfigDto config = configService.getByProjectCode(projectCode);
        return protocol + "://" + "STT-" + config.getEngineParameter() + "." + host;
    }
}
