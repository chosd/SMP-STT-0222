package com.kt.smp.common.root;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.base.annotation.SmpServicePage;
import com.kt.smp.base.annotation.SmpServiceWidget;
import com.kt.smp.base.dto.HttpResponse;
import com.kt.smp.base.dto.SmpServiceApiDto;
import com.kt.smp.base.dto.SmpServiceDto;
import com.kt.smp.base.dto.SmpServicePageDto;
import com.kt.smp.base.dto.SmpServiceWidgetDto;
import com.kt.smp.base.func.JsonUtil;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.multitenancy.service.ConfigService;

import lombok.RequiredArgsConstructor;

/**
 * Master SMP의 Base(Root) Controller로 공통으로 사용되는 내용들을 제공하기 위한 Controller
 * @author AICC 기술개발 지관욱
 * @since 2022.10.12
 * @version 1.0
 *
 * <<개정 이력>>
 * 수정일 / 수정자 / 수정내용
 * 2022.10.12 / 지관욱 / 초기 생성
 */
@Controller
@RequiredArgsConstructor
public class RootController {
	@Value("${smp.service.uri.prefix}")
	private String smpServiceUriPrefix;

	private final ConfigService configService;

	/**
	 * SPA 기본 화면 접속
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(path="/", method = RequestMethod.GET)
	public String goRoot(HttpServletRequest request, Model model){
		model.addAttribute("smpServiceUriPrefix", smpServiceUriPrefix);
		return "pages/index";
	}

	/**
	 * 서비스 정보 제공 API
	 * @param request
	 * @param model
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(path="/", method = RequestMethod.POST)
	@ResponseBody
	public String getServiceInterworking(HttpServletRequest request, Model model,
										 @Value("${smp.service.name}") String smpServiceName,
										 @Value("${smp.service.version}") String smpServiceVersion,
										 @Value("${smp.service.description}") String smpServiceDescription,
										 @Value("${smp.service.uri.prefix}") String smpServiceUriPrefix,
										 @Value("${smp.service.lastupdatedat}") String smpServiceLastUpdatedAt) throws JsonProcessingException, ParseException {
		List<SmpServiceApiDto> apis = new ArrayList<>();
		List<SmpServicePageDto> pages = new ArrayList<>();
		List<SmpServiceWidgetDto> widgets = new ArrayList<>();

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] rs;
		String classAntPattern = "classpath*:com/kt/smp/**/*.class";

		try {
			rs = resolver.getResources(classAntPattern);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		for(Resource r : rs) {
			MetadataReader mr;
			try {
				mr = new SimpleMetadataReaderFactory().getMetadataReader(r);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			boolean hasAnnotation = mr.getAnnotationMetadata().hasAnnotation(Controller.class.getName()) ||
								    mr.getAnnotationMetadata().hasAnnotation(RestController.class.getName());


			if (hasAnnotation) {
				AnnotationMetadata metadatas = mr.getAnnotationMetadata();

				// SmpServiceApi mapping
				try {
					String annotationName = SmpServiceApi.class.getName();


					// method 정보 확인
					Set<MethodMetadata> smpServiceMetadatas = metadatas.getAnnotatedMethods(annotationName);
					for (MethodMetadata methodMetadata : smpServiceMetadatas) {
						SmpServiceApiDto api = new SmpServiceApiDto();
						Map<String, Object> meta = methodMetadata.getAnnotationAttributes(annotationName);

						api.setName((String)meta.get("name"));
						api.setUris((String[])meta.get("path"));
						api.setMethods((RequestMethod[])meta.get("method"));
						api.setType((String)meta.get("type"));
						api.setDescription((String)meta.get("description"));
						apis.add(api);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}

				// SmpServicePage mapping
				try {
					String annotationName = SmpServicePage.class.getName();

					// method 정보 확인
					Set<MethodMetadata> smpServiceMetadatas = metadatas.getAnnotatedMethods(annotationName);
					for (MethodMetadata methodMetadata : smpServiceMetadatas) {
						SmpServicePageDto page = new SmpServicePageDto();
						Map<String, Object> meta = methodMetadata.getAnnotationAttributes(annotationName);

						page.setName((String)meta.get("name"));
						page.setParent((meta.get("parent") == null) ? "" : (String)meta.get("parent"));
						page.setUris((String[])meta.get("path"));
						page.setHtml((String)meta.get("html"));
						page.setDescription((String)meta.get("description"));
						pages.add(page);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
				
				// SmpServiceWidget mapping
				try {
					String annotationName = SmpServiceWidget.class.getName();

					// method 정보 확인
					Set<MethodMetadata> smpServiceMetadatas = metadatas.getAnnotatedMethods(annotationName);
					for (MethodMetadata methodMetadata : smpServiceMetadatas) {
						SmpServiceWidgetDto widget = new SmpServiceWidgetDto();
						Map<String, Object> meta = methodMetadata.getAnnotationAttributes(annotationName);

						widget.setName((String)meta.get("name"));
						widget.setUris((String[])meta.get("path"));
						widget.setImagePath((String)meta.get("image"));
						widget.setDescription((String)meta.get("description"));
						widgets.add(widget);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}

		SmpServiceDto service = new SmpServiceDto();
		service.setName(smpServiceName);
		service.setVersion(smpServiceVersion);
		service.setDescription(smpServiceDescription);
		service.setPrefix(smpServiceUriPrefix);
		service.setLastUpdatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(smpServiceLastUpdatedAt));
		service.setApis(apis);
		service.setPages(pages);
		service.setWidgets(widgets);

		return JsonUtil.toJson(HttpResponse.onSuccess(service));
	}

	/**
	 * 서비스 연동 disconnect 시 처리
	 * NOTE :: Master SMP에서 서비스 연동 해지 시 요청됨
	 * @param request
	 * @return
	 */
	@RequestMapping(path="/disconnect", method = RequestMethod.POST)
	@ResponseBody
	public String disconnect(HttpServletRequest request) throws JsonProcessingException {

		try {

			MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
			configService.delete(header.getProjectCode());

			return JsonUtil.toJson(HttpResponse.onSuccess(true));

		} catch (Throwable ex) {
			ex.printStackTrace();
			return JsonUtil.toJson(HttpResponse.onFailure(ex.getMessage()));
		}
	}
}
