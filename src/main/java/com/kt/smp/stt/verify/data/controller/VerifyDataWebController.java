package com.kt.smp.stt.verify.data.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServicePage;
import com.kt.smp.base.func.JsonUtil;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.verify.data.dto.VerifyDataListDto;
import com.kt.smp.stt.verify.data.service.VerifyDataService;
import com.kt.smp.stt.verify.dataset.dto.VerifyDatasetListDto;
import com.kt.smp.stt.verify.dataset.service.VerifyDatasetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("${smp.service.uri.prefix}")
@RequiredArgsConstructor
public class VerifyDataWebController {

    @Value("${smp.service.uri.prefix}")
    String smpServiceUriPrefix;

    private final ServiceModelService serviceModelService;
    private final VerifyDatasetService datasetService;
    private final VerifyDataService dataService;
    
    @Value("${directory.home}")
    private String directoryHome;

    @SmpServicePage(name = "검증데이터", path = "/verify/data", html = "/html/verify/data")
    public String goVerifyDataList(Model model) {

        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        return "pages/index";
    }

    @RequestMapping("/html/verify/data")
    public String getVerifyDataListHtml(Model model) throws JsonProcessingException {

        List<ServiceModelVO> serviceModelList = serviceModelService.listAll();
        //List<VerifyDatasetListDto> datasetList = datasetService.getAll();
        List<VerifyDataListDto> dataList = dataService.getDatasetGroup();
        model.addAttribute("serviceModelList", JsonUtil.toJson(serviceModelList));
        model.addAttribute("dataList", JsonUtil.toJson(dataList));
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        model.addAttribute("directoryHome", directoryHome);
        model.addAttribute("defaultPageSize", PreferenceValueHolder.recordCount.get(TenantContextHolder.getProjectCode()));

        return "stt/verify/dataList";
    }

    @SmpServicePage(name = "검증데이터 상세", path = "/verify/data/{dataId}", html = "/html/verify/data/detail", parent = "/verify/data")
    public String goVerifyData(Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        return "pages/index";
    }

    @RequestMapping("/html/verify/data/detail")
    public String getVerifyDataHtml(Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        model.addAttribute("projectCode", TenantContextHolder.getProjectCode());
        return "stt/verify/dataDetail";
    }
}
