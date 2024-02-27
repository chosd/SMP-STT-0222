package com.kt.smp.config;

import com.kt.smp.interceptor.TenantInterceptor;
import com.kt.smp.multitenancy.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	private final ConfigService configService;
	@Value("${smp.service.uri.prefix}")
	private String serviceUriPrefix;
	@Value("${callinfo.web-url}")
	private String webUrl;
	@Value("${callinfo.wav-download-path}")
	private String downloadPath;
	@Value("${spring.profiles.active}")
	private String profile;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(new TenantInterceptor(configService, serviceUriPrefix, profile))
				.excludePathPatterns("/smp/assets/**", "/smp/libs/**", webUrl);
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(webUrl).addResourceLocations("file://" + downloadPath);
	}
	
	@Bean
	public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
		return customizer -> customizer.addConnectorCustomizers(
				connector -> {connector.setAllowTrace(true);}
			); 
	}
	
	/*
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// TODO Auto-generated method stub
		registry.addMapping("/api/**")
		.allowedOrigins("*").allowedMethods("GET").allowedHeaders("*");
	} */
}
