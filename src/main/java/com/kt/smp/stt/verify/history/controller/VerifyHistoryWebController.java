package com.kt.smp.stt.verify.history.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.Page;
import com.kt.smp.base.annotation.SmpServicePage;
import com.kt.smp.base.func.JsonUtil;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployMngSearchCondition;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployMngVO;
import com.kt.smp.stt.deploy.deploy.service.SttDeployMngService;
import com.kt.smp.stt.verify.data.dto.VerifyDataListDto;
import com.kt.smp.stt.verify.data.service.VerifyDataService;
import com.kt.smp.stt.verify.dataset.dto.VerifyDatasetListDto;
import com.kt.smp.stt.verify.dataset.service.VerifyDatasetService;
import com.kt.smp.stt.verify.history.dto.DeployedModelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("${smp.service.uri.prefix}")
@RequiredArgsConstructor
public class VerifyHistoryWebController {

    @Value("${smp.service.uri.prefix}")
    String smpServiceUriPrefix;
    private final ServiceModelService serviceModelService;
    private final VerifyDatasetService datasetService;
    private final VerifyDataService dataService;
    private final SttDeployMngService deployMngService;

    @SmpServicePage(name = "검증요청 관리", path = "/verify/history", html = "/html/verify/history")
    public String goVerifyHistoryList(Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        return "pages/index";
    }

    @RequestMapping("/html/verify/history")
    public String getVerifyHistoryListHtml(Model model) throws JsonProcessingException {

        List<ServiceModelVO> serviceModelList = serviceModelService.listAll();
        //List<VerifyDatasetListDto> datasetList = datasetService.getAll();
        List<VerifyDataListDto> dataList = dataService.getDatasetGroup();

        model.addAttribute("serviceModelList", JsonUtil.toJson(serviceModelList));
        model.addAttribute("deployedModelList", JsonUtil.toJson(getDeployedModelList(serviceModelList)));
        model.addAttribute("dataList", JsonUtil.toJson(dataList));
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        model.addAttribute("defaultPageSize", PreferenceValueHolder.recordCount.get(TenantContextHolder.getProjectCode()));

        return "stt/verify/historyList";
    }

    private List<DeployedModelDto> getDeployedModelList(List<ServiceModelVO> serviceModelList) {

        List<DeployedModelDto> deployList = new ArrayList<>();
        
        for (ServiceModelVO serviceModel : serviceModelList) {

            SttDeployMngVO deploy = deployMngService.detailLastOne(serviceModel.getServiceCode());
            if (deploy != null) {
                deployList.add(new DeployedModelDto(deploy.getId(), deploy.getResultModelId(), deploy.getServiceModelId()));
            }
        }

        return deployList;

    }
}
