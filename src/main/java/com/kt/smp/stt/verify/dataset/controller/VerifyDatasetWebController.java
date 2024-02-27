package com.kt.smp.stt.verify.dataset.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServicePage;
import com.kt.smp.base.func.JsonUtil;
import com.kt.smp.stt.comm.directory.dto.DirectoryListDto;
import com.kt.smp.stt.comm.directory.service.DirectoryService;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("${smp.service.uri.prefix}")
@RequiredArgsConstructor
public class VerifyDatasetWebController {

    @Value("${smp.service.uri.prefix}")
    String smpServiceUriPrefix;

    private final ServiceModelService serviceModelService;
    private final DirectoryService directoryService;

    @SmpServicePage(name = "검증데이터셋", path = "/verify/dataset", html = "/html/verify/dataset")
    public String goVerifyDatasetList(Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        return "pages/index";
    }

    @RequestMapping("/html/verify/dataset")
    public String getVerifyDatasetListHtml(Model model) throws JsonProcessingException {
        List<ServiceModelVO> serviceModelList = serviceModelService.listAll();
        List<DirectoryListDto> directoryList = directoryService.getAll();

        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        model.addAttribute("serviceModelList", JsonUtil.toJson(serviceModelList));
        model.addAttribute("directoryList", JsonUtil.toJson(directoryList));
        return "stt/verify/datasetList";
    }
}
