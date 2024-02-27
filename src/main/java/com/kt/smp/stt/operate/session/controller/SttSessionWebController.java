package com.kt.smp.stt.operate.session.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServicePage;
import com.kt.smp.base.func.JsonUtil;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.operate.session.enums.SessionSortCondition;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
*@FileName : SttSessionWebController.java
@Project : kt-stt-service_r
@Date : 2023. 9. 19.
*@작성자 : wonyoung.ahn
*@변경이력 :
*@프로그램설명 :
*/
@Controller
@RequestMapping("${smp.service.uri.prefix}")
@RequiredArgsConstructor
public class SttSessionWebController {

    private final ServiceModelService serviceModelService;

    @Value("${smp.service.uri.prefix}")
    String smpServiceUriPrefix;

    @SmpServicePage(name = "실시간 세션정보", path = "/session", html = "/html/session")
    public String goTrainList(HttpServletRequest request, Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);

        return "pages/index";
    }

    @RequestMapping("/html/session")
    public String getTrainListHtml(HttpServletRequest request, Model model) throws JsonProcessingException {
        List<ServiceModelVO> serviceModelList = serviceModelService.listAll();
        
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        model.addAttribute("serviceModelList", JsonUtil.toJson(serviceModelList));
        model.addAttribute("sessionSortConditionList", JsonUtil.toJson(SessionSortCondition.getMapList()));
        return "stt/operate/session";
    }
}
