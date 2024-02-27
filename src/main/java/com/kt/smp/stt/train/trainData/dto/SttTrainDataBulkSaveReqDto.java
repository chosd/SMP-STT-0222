package com.kt.smp.stt.train.trainData.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @title STT 학습데이터 대량 등록 요청 DTO
 * @since 2022.05.12
 * @author jieun.chang
 * @see  <pre><pre>
 */

@Getter
@Setter
@SuperBuilder
public class SttTrainDataBulkSaveReqDto extends BaseRequestDto {

	private String dataType;
	private String serviceModel;
	private String contents;
	private String repeatCount;
	private String description;
	private String uploadedBy;
	private String regIp;

	private String failReason; // 저장 실패 이유

	public SttTrainDataBulkSaveReqDto() {
	}

	public boolean isValidForInsert() {
		return ObjectUtils.isNotEmpty(dataType)
			&& ObjectUtils.isNotEmpty(serviceModel)
			&& StringUtils.isNotEmpty(contents)
			&& StringUtils.isNotEmpty(repeatCount);
	}

	public static Map<String, Type> getColumnMap() {
		Map<String, Type> map = new LinkedHashMap<>();
		map.put("dataType", String.class);
		map.put("serviceModel", String.class);
		map.put("contents", String.class);
		map.put("repeatCount", String.class);
		map.put("description", String.class);
		map.put("uploadedBy", String.class);

		return map;
	}
}
