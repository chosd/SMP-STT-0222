package com.kt.smp.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.kt.smp.config.CommonConstants;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.service.ConfigService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    private final ConfigService configService;
    private final String serviceUriPrefix;
    private String profile;

    public TenantInterceptor(ConfigService configService, String serviceUriPrefix, String profile) {
        this.configService = configService;
        this.serviceUriPrefix = serviceUriPrefix;
        this.profile = profile;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {

    	if (isSwaggerRequests(request)) {
			log.debug("[REQ] Swagger Request : {}", request.getRequestURI());
			return true;
		}
    	
        String projectCode = extractProjectCode(request);

        if (requireAuthentication(request)) {
            return applyProjectCode(projectCode, response);
        }

        TenantContextHolder.set(projectCode);
        return true;

    }
    
    private boolean isSwaggerRequests(HttpServletRequest request) {
		if(!profile.equals(CommonConstants.LOCAL_PROFILE)) return false;
		
		return request.getRequestURI().contains("/swagger-ui");
	}

    private boolean applyProjectCode(String projectCode, HttpServletResponse response) throws IOException {

        if (isValidProjectCode(projectCode)) {
            ConfigDto config = getConfigByProject(projectCode);
            TenantContextHolder.set(config.getProjectCode());
            return true;
        }

        response.sendRedirect(serviceUriPrefix + "/error/" + 403);
        return false;
    }

    private ConfigDto getConfigByProject(String projectCode) {

        ConfigDto configOfProjectCode = configService.getByProjectCode(projectCode);
        if (configOfProjectCode != null) {
            return configOfProjectCode;
        }

        return configService.getByEngineParameter(projectCode);
    }

    private boolean requireAuthentication(HttpServletRequest request) {
        return pageRequest(request) && notConfigPageRequest(request);
    }

    private boolean isValidProjectCode(String projectCode) {

        ConfigDto configOfProjectCode = configService.getByProjectCode(projectCode);
        if (configOfProjectCode != null) {
            return true;
        }

        ConfigDto configOfEngineParam = configService.getByEngineParameter(projectCode);
        return configOfEngineParam != null;
    }

    private boolean pageRequest(HttpServletRequest request) {

        return request.getRequestURI().contains("html");
    }

    private boolean notConfigPageRequest(HttpServletRequest request) {

        return !request.getRequestURI().contains("config");
    }

    private String extractProjectCode(HttpServletRequest request) {

        String fromHeader = request.getHeader(CommonConstants.X_SMP_PROJECT_CODE);
        String fromParameter = request.getParameter(CommonConstants.X_SMP_PROJECT_CODE);

        if (StringUtils.isNotBlank(fromHeader)) {
            return fromHeader;
        }

        return fromParameter;
    }

}
