package com.kt.smp.multitenancy.controller;

import com.kt.smp.base.annotation.SmpServicePage;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("${smp.service.uri.prefix}")
@RequiredArgsConstructor
public class ConfigWebController {

    @Value("${smp.service.uri.prefix}")
    String smpServiceUriPrefix;

    @SmpServicePage(name = "STT 서비스 인증 설정", path = "/config", html = "/html/config")
    public String goConfig(Model model) {

        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        return "pages/index";
    }

    @RequestMapping("/html/config")
    public String getConfigHtml(HttpServletRequest request, Model model) {

        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);

        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        model.addAttribute("siteCode", header.getSiteCode());
        model.addAttribute("projectCode", header.getProjectCode());
        model.addAttribute("userId", header.getUserId());
        model.addAttribute("userName", header.getUserName());

        return "stt/comm/serviceConfig";
    }
}
