package com.kt.smp.stt.deploy.deploy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.kt.smp.base.annotation.SmpServicePage;
import com.kt.smp.base.func.JsonUtil;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.deploy.model.service.SttDeployModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("${smp.service.uri.prefix}")
@RequiredArgsConstructor
public class SttDeployWebController {

    private final ServiceModelService serviceModelService;

    private final SttDeployModelService sttDeployModelService;
    
    @Value("${smp.service.uri.prefix}")
    String smpServiceUriPrefix;

    @SmpServicePage(name = "배포요청", path = "/deploy", html = "/html/deploy")
    public String goDeployList(HttpServletRequest request, Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);

        return "pages/index";
    }

    @RequestMapping("/html/deploy")
    public String getDeployListHtml(HttpServletRequest request, Model model) throws JsonProcessingException {
        List<ServiceModelVO> serviceModelList = serviceModelService.listAll();
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        model.addAttribute("serviceModelList", JsonUtil.toJson(serviceModelList));
        model.addAttribute("resultModelId2Descriptions", new Gson().toJson(sttDeployModelService.getDeployMngModels().getResultModelId2Descriptions()));
        model.addAttribute("serviceCode2ResultModelIds", new Gson().toJson(sttDeployModelService.getDeployMngModels().getServiceCode2ResultModelIds()));
        model.addAttribute("resultModelId2ModelTypes", new Gson().toJson(sttDeployModelService.getDeployMngModels().getResultModelId2ModelTypes()));
        model.addAttribute("defaultPageSize", PreferenceValueHolder.recordCount.get(TenantContextHolder.getProjectCode()));

        return "stt/deploy/deploy";
    }
}
