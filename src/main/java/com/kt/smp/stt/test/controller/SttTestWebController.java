package com.kt.smp.stt.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServicePage;
import com.kt.smp.base.func.JsonUtil;
import com.kt.smp.config.CommonConstants;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.common.EngineUrlResolver;
import com.kt.smp.stt.test.domain.SttTestTargetDto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("${smp.service.uri.prefix}")
@RequiredArgsConstructor
public class SttTestWebController {

    private final ServiceModelService serviceModelService;
    private final EngineUrlResolver engineUrlResolver;

    @Value("${smp.service.uri.prefix}")
    String smpServiceUriPrefix;

    @SmpServicePage(name = "단건 테스트", path = "/test", html = "/html/test")
    public String goTest(HttpServletRequest request, Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);

        return "pages/index";
    }

    @RequestMapping("/html/test")
    public String getTestHtml(HttpServletRequest request, Model model) throws JsonProcessingException {
        List<ServiceModelVO> serviceModelList = serviceModelService.listAll();
        Map<String, SttTestTargetDto> testTargetList = getTestTargetList(request);
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        model.addAttribute("serviceModelList", JsonUtil.toJson(serviceModelList));
        model.addAttribute("testTarget", JsonUtil.toJson(testTargetList));
        
        return "stt/test/test";
    }
    
    private Map<String, SttTestTargetDto> getTestTargetList(HttpServletRequest request) {
    	String projectCode = request.getHeader(CommonConstants.X_SMP_PROJECT_CODE);
    	String testServer1 = engineUrlResolver.resolve(projectCode);
        String testServer2 = engineUrlResolver.resolveSub(projectCode);
        
        Map<String, SttTestTargetDto> result = new HashMap<>();
        
        result.put("host", SttTestTargetDto.builder().host(testServer1).propertyName("host").build());
        if (testServer2 != null && !testServer2.equals(testServer1)) {
        	result.put("hostDeploy", SttTestTargetDto.builder().host(testServer2).propertyName("host-deploy").build());
        }
        
        return result;
    }
}
