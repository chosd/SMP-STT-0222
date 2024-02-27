package com.kt.smp.stt.comm.serviceModel.dto;

import com.kt.smp.stt.train.trainData.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;

@Getter
@Setter
@SuperBuilder
public class ServiceModelSaveReqDto extends BaseRequestDto {

	private Long serviceModelId;
	private String serviceModelName;
	private String serviceCode;

	private String description;

	private String uploadedBy;

	private String regIp;

	public ServiceModelSaveReqDto() {
	}

	/**
	 * 필드 유효성 검사.
	 * @return 필드 유효성 검사 결과
	 */
	public boolean isValid() {
		return ObjectUtils.isNotEmpty(serviceModelName)
			&& ObjectUtils.isNotEmpty(serviceCode);
	}
}
