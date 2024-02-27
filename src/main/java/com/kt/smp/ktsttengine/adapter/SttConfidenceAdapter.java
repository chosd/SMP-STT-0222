/**
 * Master SMP TTS Service
 * @author AICC 기술개발 박수현
 * @since 2022.10.27
 * @version 1.0
 */
package com.kt.smp.ktsttengine.adapter;

import static com.kt.smp.ktsttengine.constant.SttEngineConstants.*;

import java.io.File;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.kt.smp.common.util.HttpClientUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * SMP --> TCS 상용구 API 요청
 *
 * @since 2022.11.30
 * @author soohyun
 * @see <pre></pre>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SttConfidenceAdapter {

	private final RestTemplate restTemplate;

}
