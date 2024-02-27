package com.kt.smp.stt.comm.preference.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServicePage;
import com.kt.smp.base.func.JsonUtil;
import com.kt.smp.stt.comm.preference.component.PreferenceDtoConverter;
import com.kt.smp.stt.comm.preference.dto.RemoveListDto;
import com.kt.smp.stt.comm.preference.service.PreferenceService;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.dictation.dto.ConfidenceConfigDto;
import com.kt.smp.stt.dictation.service.DictationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}")
public class PreferenceWebController {
	
	private final PreferenceService preferenceService;
	
	private final PreferenceDtoConverter preferenceDtoConverter;
	
	private final ServiceModelService serviceModelService;
	
	private final DictationService dictationService;
	
    @Value("${smp.service.uri.prefix}")
    String smpServiceUriPrefix;

    @SmpServicePage(name = "STT 환경설정", path = "/preference", html = "/html/preference")
    public String goPreference(HttpServletRequest request, Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);

        return "pages/index";
    }

    @RequestMapping("/html/preference")
    public String getPreferenceHtml(HttpServletRequest request, Model model) throws JsonProcessingException {
        List<ServiceModelVO> serviceModelList = serviceModelService.listAll();
    	RemoveListDto removeList = preferenceService.getRemovableList();
    	List<ConfidenceConfigDto> confidenceList = new ArrayList<>();
    	for (ServiceModelVO vo : serviceModelList) {
    		confidenceList.add(dictationService.getConfidenceValue(vo.getServiceCode()));
    	}
    	
    	model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        model.addAttribute("serviceModelList", JsonUtil.toJson(serviceModelList));
        model.addAttribute("confidenceList", JsonUtil.toJson(confidenceList));
        model.addAttribute("preferenceCategoryList", preferenceService.findCategoryList());
        model.addAttribute("removeListAll", preferenceDtoConverter.convertRemoveListToJson(removeList));
        
        return "stt/comm/preference";
    }
    
}
