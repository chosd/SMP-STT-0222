package com.kt.smp.stt.common;

import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EngineUrlResolver {

    @Value("${core.stt.protocol}")
    private String protocol;

    @Value("${core.stt.host}")
    private String host;
    
    @Value("${core.stt.host-deploy}")
    private String hostDeploy;
    
    @Value("${core.stt.allow-ssl}")
    private boolean useHttps;

    private final ConfigService configService;
    
    private static final String PROTOCOL_HTTPS = "https://";

    public String resolve() {
        return resolve(null);
    }
    
    public String resolveSub() {
        return resolveSub(null);
    }
    
    public String resolve(String projectCode) {
    	String result = "";
        if (useHttps) {
        	result = PROTOCOL_HTTPS + host;
        }else {
        	result = protocol + "://" + host;
        }
        log.info(">>>>> REQEST resolveURL(host) : "+result);
        return result;

//        if (StringUtils.isBlank(projectCode)) {
//            return protocol + "://" + host;
//        }
//
//        return addParamToUrl(projectCode);
    }
    
    public String resolveSub(String projectCode) {
    	String result = "";
        if (useHttps) {
        	//24.03.20 CSD 스카이라이프 host-deploy 통신은 http로
        	//result = PROTOCOL_HTTPS + hostDeploy;
        	result = protocol + "://" + hostDeploy;
        }else {
        	result = protocol + "://" + hostDeploy;
        }
        log.info(">>>>> REQEST resolveURL(hostDeploy) : "+result);
        return result;
    }

    private String addParamToUrl(String projectCode) {

        ConfigDto config = configService.getByProjectCode(projectCode);
        return protocol + "://" + "STT-" + config.getEngineParameter() + "." + host;
    }
}
