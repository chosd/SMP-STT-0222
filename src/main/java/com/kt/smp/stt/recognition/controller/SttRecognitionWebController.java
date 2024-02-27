/**
 * 
 */
package com.kt.smp.stt.recognition.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServicePage;
import com.kt.smp.base.func.JsonUtil;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;

import lombok.RequiredArgsConstructor;

/**
* @FileName : SttRecognitionWebController.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 25.
* @작성자 : rapeech
* @변경이력 :
* @프로그램설명 :
*/
@Controller
@RequestMapping("${smp.service.uri.prefix}")
@RequiredArgsConstructor
public class SttRecognitionWebController {
	
    private final ServiceModelService serviceModelService;
    
    @Value("${smp.service.uri.prefix}")
    String smpServiceUriPrefix;

    @SmpServicePage(name = "인식률 차트", path = "/recognition", html = "/html/recognition")
    public String goTrainList(HttpServletRequest request, Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);

        return "pages/index";
    }

    @RequestMapping("/html/recognition")
    public String getTrainListHtml(HttpServletRequest request, Model model) throws JsonProcessingException {
        List<ServiceModelVO> serviceModelList = serviceModelService.listAll();
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        model.addAttribute("serviceModelList", JsonUtil.toJson(serviceModelList));

        return "stt/recognition/recognition";
    }
}
