package com.kt.smp.stt.error.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServicePage;
import com.kt.smp.base.func.JsonUtil;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.error.dto.SttErrorTypeDto;
import com.kt.smp.stt.error.service.SttErrorService;

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
public class SttErrorWebController {

    private final SttErrorService sttErrorService;

    @Value("${smp.service.uri.prefix}")
    String smpServiceUriPrefix;

    @SmpServicePage(name = "장애 이력 조회", path = "/error", html = "/html/error")
    public String goErrorList(HttpServletRequest request, Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);

        return "pages/index";
    }

    @RequestMapping("/html/error")
    public String getErrorListHtml(HttpServletRequest request, Model model) throws JsonProcessingException {
        List<SttErrorTypeDto> typeList = sttErrorService.selectType();
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        model.addAttribute("typeList", JsonUtil.toJson(typeList));
        model.addAttribute("defaultPageSize", PreferenceValueHolder.recordCount.get(TenantContextHolder.getProjectCode()));

        return "stt/error/error";
    }
}
