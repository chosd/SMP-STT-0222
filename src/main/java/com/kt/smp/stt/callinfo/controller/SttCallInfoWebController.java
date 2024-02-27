package com.kt.smp.stt.callinfo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServicePage;
import com.kt.smp.base.func.JsonUtil;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("${smp.service.uri.prefix}")
@RequiredArgsConstructor
public class SttCallInfoWebController {
	
	@Value("${smp.service.uri.prefix}")
    String smpServiceUriPrefix;
	
	@Value("${callinfo.stream-mode}")
    String isStreamMode;
	
	private final ServiceModelService serviceModelService;
	
	@SmpServicePage(name = "상담 내역 청취 리스트", path = "/callinfo", html = "/html/callinfo")
	public String goCallInfo(Model model) {
	       model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
	       return "pages/index";
	}
	
	@RequestMapping("/html/callinfo")
	public String getCallInfo(Model model) throws JsonProcessingException {
	 List<ServiceModelVO> serviceModelList = serviceModelService.listAll();
	       
		model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
    	model.addAttribute("serviceModelList", JsonUtil.toJson(serviceModelList));
    	model.addAttribute("isStreamMode", isStreamMode);
        model.addAttribute("defaultPageSize", PreferenceValueHolder.recordCount.get(TenantContextHolder.getProjectCode()));

	   return "stt/operate/callinfo";
	}
	
	@SmpServicePage(name = "상담 내역 청취 상세", path = "/callinfo/{callKey}", html = "/html/callinfo/detail", parent = "/callinfo")
	public String goCallInfoDetail(Model model) {
	       model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
	       return "pages/index";
	}
	
	@RequestMapping("/html/callinfo/detail")
	public String getCallInfoDetail(Model model) throws JsonProcessingException {
	 List<ServiceModelVO> serviceModelList = serviceModelService.listAll();
	       
		model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
    	model.addAttribute("serviceModelList", JsonUtil.toJson(serviceModelList));
    	
	   return "stt/callinfo/callInfoDetail";
	}
	
//	@SmpServicePage(name = "상담 내역 청취 상세", path = "/callinfoV2/{callKey}", html = "/html/callinfoV2/detail", parent = "/callinfo")
	public String goCallInfoDetailV2(Model model) {
	       model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
	       return "pages/index";
	}
	
//	@RequestMapping("/html/callinfoV2/detail")
	public String getCallInfoDetailV2(Model model) throws JsonProcessingException {
	 List<ServiceModelVO> serviceModelList = serviceModelService.listAll();
	       
		model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
    	model.addAttribute("serviceModelList", JsonUtil.toJson(serviceModelList));
    	
	   return "stt/callinfo/callInfoDetailV2";
	}
}
