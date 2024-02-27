package com.kt.smp.stt.dictation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServicePage;
import com.kt.smp.base.func.JsonUtil;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.stt.comm.directory.dto.DirectoryGroupDto;
import com.kt.smp.stt.comm.directory.dto.DirectoryListDto;
import com.kt.smp.stt.comm.directory.service.DirectoryService;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.train.trainData.dto.SttTrainDatasetListDto;
import com.kt.smp.stt.train.trainData.service.SttTrainDataService;
import com.kt.smp.stt.verify.data.dto.VerifyDataListDto;
import com.kt.smp.stt.verify.data.service.VerifyDataService;
import com.kt.smp.stt.verify.dataset.dto.VerifyDatasetListDto;
import com.kt.smp.stt.verify.dataset.service.VerifyDatasetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Slf4j
@Controller
@RequestMapping("${smp.service.uri.prefix}")
public class DictationWebController {

	private static final int TRAIN_DIRECTORY_GROUP_ID = 1;
	
    @Value("${smp.service.uri.prefix}")
    String smpServiceUriPrefix;
    
    @Value("${directory.home}")
    private String directoryHome;
    
	@Autowired
    private ServiceModelService serviceModelService;
	@Autowired
    private VerifyDatasetService verifyDatasetService;
	@Autowired
    private DirectoryService directoryService;
	@Autowired
	private SttTrainDataService sttTrainDataService;
	@Autowired
	private VerifyDataService verifyDataService;

    @SmpServicePage(name = "전사데이터", path = "/dictation", html = "/html/dictation")
    public String goDictationList(Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        return "pages/index";
    }

    @RequestMapping("/html/dictation")
    public String getDictationListHtml(Model model) throws JsonProcessingException {

        List<ServiceModelVO> serviceModelList = serviceModelService.listAll();
        List<VerifyDatasetListDto> verifyDatasetList = verifyDatasetService.getAll();
        //List<DirectoryListDto> directoryList = directoryService.getAll();
        //List<DirectoryListDto> amDirectoryList = directoryService.getTrainList();
        List<DirectoryListDto> amDirectoryList = directoryService.getList(TRAIN_DIRECTORY_GROUP_ID);
        List<SttTrainDatasetListDto> trainDatasetList = sttTrainDataService.amDatasetAll();
        List<VerifyDataListDto> dataList = verifyDataService.getDatasetGroup();

        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        model.addAttribute("serviceModelList", JsonUtil.toJson(serviceModelList));
        model.addAttribute("verifyDatasetList", JsonUtil.toJson(verifyDatasetList));
        //model.addAttribute("directoryList", JsonUtil.toJson(directoryList));
        model.addAttribute("amDirectoryList", JsonUtil.toJson(amDirectoryList));
        model.addAttribute("trainDatasetList", JsonUtil.toJson(trainDatasetList));
        model.addAttribute("defaultPageSize", PreferenceValueHolder.recordCount.get(TenantContextHolder.getProjectCode()));
        model.addAttribute("directoryHome", directoryHome);
        model.addAttribute("dataList", JsonUtil.toJson(dataList));


        return "stt/dictation/dictationList";
    }

    @SmpServicePage(name = "전사데이터 상세", path = "/dictation/{dictationId}", html = "/html/dictation/detail", parent = "/dictation")
    public String goDictationDetail(Model model) {

        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        return "pages/index";
    }

    @RequestMapping("/html/dictation/detail")
    public String getDictationDetailHtml(Model model) throws JsonProcessingException {

        List<VerifyDatasetListDto> verifyDatasetList = verifyDatasetService.getAll();
        List<DirectoryListDto> directoryList = directoryService.getList(TRAIN_DIRECTORY_GROUP_ID);
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        model.addAttribute("verifyDatasetList", JsonUtil.toJson(verifyDatasetList));
        model.addAttribute("directoryList", JsonUtil.toJson(directoryList));
        model.addAttribute("projectCode", TenantContextHolder.getProjectCode());

        return "stt/dictation/dictationDetail";
    }
}
