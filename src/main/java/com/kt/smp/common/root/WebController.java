/**
 * Master SMP STT Service
 *
 * @author AICC 기술개발 장지은
 * @version 1.0
 * @since 2022.11.29
 */
package com.kt.smp.common.root;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

import com.kt.smp.base.annotation.SmpServicePage;
import com.kt.smp.base.annotation.SmpServiceWidget;
import com.kt.smp.base.func.JsonUtil;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("${smp.service.uri.prefix}")
@RequiredArgsConstructor
public class WebController {

	@Value("${smp.service.uri.prefix}")
	String smpServiceUriPrefix;

	@Value("${directory.home}")
	private String directoryHome;
	
    private final ServiceModelService serviceModelService;

	/*LM 학습데이터*/
	@SmpServicePage(name = "LM 학습데이터", path = "/trainData", html = "/html/trainData")
	public String goTrainDataList(HttpServletRequest request, Model model) {
		model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
		return "pages/index";
	}

	@RequestMapping("/html/trainData")
	public String getTrainDataListHtml(HttpServletRequest request, Model model) {
		MasterSmpRequestHeaderDto headerDto = MasterSmpRequestHeaderDto.generate(request);
		
		model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
		model.addAttribute("siteCode", headerDto != null ? headerDto.getSiteCode() : "");
		model.addAttribute("projectCode", headerDto != null ? headerDto.getProjectCode() : "");
        model.addAttribute("defaultPageSize", PreferenceValueHolder.recordCount.get(TenantContextHolder.getProjectCode()));

		return "stt/train/trainData";
	}

	
	/**
	 *@MethodName : goAnswerTrainDataList
	 *@작성일 : 2023. 9. 26.
	 *@작성자 : munho.jang
	 *@변경이력 : 
	 *@Method설명 : 신규화면기획서(AMP 신규기능 UI 기획서_v0.5)에 따라 메뉴추가
	 * @param request
	 * @param model
	 * @return
	 */
	@SmpServicePage(name = "AM 학습데이터", path = "/answerTrainData", html = "/html/answerTrainData")
	public String goAnswerTrainDataList(HttpServletRequest request, Model model) {
		model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
		return "pages/index";
	}

	@RequestMapping("/html/answerTrainData")
	public String getAnswerTrainDataListHtml(HttpServletRequest request, Model model) {
		MasterSmpRequestHeaderDto headerDto = MasterSmpRequestHeaderDto.generate(request);
		
		model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
		model.addAttribute("siteCode", headerDto != null ? headerDto.getSiteCode() : "");
		model.addAttribute("projectCode", headerDto != null ? headerDto.getProjectCode() : "");
		model.addAttribute("basePath", directoryHome);
        model.addAttribute("defaultPageSize", PreferenceValueHolder.recordCount.get(TenantContextHolder.getProjectCode()));

		return "stt/train/answerTrainData";
	}

	/*서비스 모델*/
	@SmpServicePage(name = "서비스 모델", path = "/serviceModel", html = "/html/serviceModel")
	public String goServiceModelList(HttpServletRequest request, Model model) {
		model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
		return "pages/index";
	}

	@RequestMapping("/html/serviceModel")
	public String getListHtml(HttpServletRequest request, Model model) {
		model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        model.addAttribute("defaultPageSize", PreferenceValueHolder.recordCount.get(TenantContextHolder.getProjectCode()));

		return "stt/comm/serviceModel";
	}

    // @SmpServicePage(name = "실시간 채널정보 ", path = "/channel", html = "/html/channel")
    public String goChannelList(HttpServletRequest request, Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        return "pages/index";
    }

    @RequestMapping("/html/channel")
    public String getChannelHtml(HttpServletRequest request, Model model) {
        model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
        model.addAttribute("defaultPageSize", PreferenceValueHolder.recordCount.get(TenantContextHolder.getProjectCode()));

        return "stt/operate/channel";
    }

	@RequestMapping(name = "에러페이지", path = "/error/{statusCode}")
	public String getErrorHtml(@PathVariable("statusCode") String statusCode) {
		return "error/" + statusCode;
	}
	
	@SmpServiceWidget(name = "STT HW 리소스 위젯", path = "/widget/hwResource", image = "/kt-stt/1.0/test_widget_example.png")
	public String widgetHwResouce(HttpServletRequest request, Model model) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
		MasterSmpRequestHeaderDto headerDto = MasterSmpRequestHeaderDto.generate(request);
		
		model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
		model.addAttribute("siteCode", headerDto != null ? headerDto.getSiteCode() : "");
		model.addAttribute("projectCode", headerDto != null ? headerDto.getProjectCode() : "");
		
		try {
            return "stt/widget/hwResource";
        } catch (HttpClientErrorException e) {
            return "error/403";
        }
	}
	
	@SmpServiceWidget(name = "STT 요청 통계 위젯", path = "/widget/requestWidget", image = "/kt-stt/1.0/test_widget_example.png")
	public String widgetStatistics(HttpServletRequest request, Model model) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
		MasterSmpRequestHeaderDto headerDto = MasterSmpRequestHeaderDto.generate(request);
        List<ServiceModelVO> serviceModelList = serviceModelService.listAll();
		model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
		model.addAttribute("siteCode", headerDto != null ? headerDto.getSiteCode() : "");
		model.addAttribute("projectCode", headerDto != null ? headerDto.getProjectCode() : "");
        model.addAttribute("serviceModelList", JsonUtil.toJson(serviceModelList));
		try {
            return "stt/widget/requestWidget";
        } catch (HttpClientErrorException e) {
            return "error/403";
        }
	}
}
