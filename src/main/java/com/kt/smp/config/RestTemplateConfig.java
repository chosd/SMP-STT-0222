package com.kt.smp.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @title RestTemplate 사용을 위한 config
 * @since 2022.02.17
 * @author soohyun
 * @see  <pre><pre>
 */
@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

	private static final int CONNECTION_TIMEOUT = 60 * 1000 * 10;
	private static final int READ_TIMEOUT = 300000;
	private static final int MAX_CONN_TOTAL = 5000;
	private static final int MAX_CONN_PER_ROUTE = 20;

	private final ObjectMapper objectMapper;

	@Value("${core.stt.allow-ssl:false}")
	private boolean ignoreSsl;

	/**
	 * @title RestTemplate bean
	 * @author soohyun
	 * @return RestTemplate
	 * @date 2022.02.17
	 * @see  <pre></pre>
	 */
	@Bean(name = "restTemplate")
	public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

		// HttpComponents 사용하여 pool 설정
		HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpComponentsClientHttpRequestFactory.setConnectTimeout(CONNECTION_TIMEOUT);
		httpComponentsClientHttpRequestFactory.setReadTimeout(READ_TIMEOUT); // 보이스봇 변경

		HttpClient httpClient = null;

		//SSL 무시하는 코드 추가
		if (ignoreSsl) {
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
			SSLContext sslContext = SSLContexts.custom()
				.loadTrustMaterial(null, acceptingTrustStrategy)
				.build();

			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

			httpClient = HttpClientBuilder.create()
				.setMaxConnTotal(MAX_CONN_TOTAL)
				.setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
				.setSSLSocketFactory(csf)
				.build();
		} else {
			httpClient = HttpClientBuilder.create()
				.setMaxConnTotal(MAX_CONN_TOTAL)
				.setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
				.build();
		}

		httpComponentsClientHttpRequestFactory.setHttpClient(httpClient);

		// logging interceptor, error handler 추가
		RestTemplate restTemplate = new RestTemplate(
			new BufferingClientHttpRequestFactory(httpComponentsClientHttpRequestFactory));
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		return restTemplate;
	}
}
