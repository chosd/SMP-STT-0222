package com.kt.smp.stt.train.trainData.dto;

import com.kt.smp.stt.common.ServiceModel;
import com.kt.smp.stt.common.TrainDataType;
import com.kt.smp.stt.common.cipher.db.CipherWrapper;
import com.kt.smp.stt.common.cipher.db.Cipherable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@SuperBuilder
public class SttTrainDataSaveReqDto extends BaseRequestDto implements Cipherable {

	private Long trainDataId;
	private TrainDataType dataType;
	/*private ServiceModel serviceModel;*/
    private Integer serviceModelId;
	private String contents;
	private Integer repeatCount;
	private String description;
	private String uploadedBy;
	private String regIp;

	public SttTrainDataSaveReqDto() {
	}

	/**
	 * 필드 유효성 검사.
	 * @return 필드 유효성 검사 결과
	 */
	public boolean isValid() {
		return ObjectUtils.isNotEmpty(dataType)
			&& ObjectUtils.isNotEmpty(serviceModelId)
			&& StringUtils.isNotEmpty(contents)
			&& ObjectUtils.isNotEmpty(repeatCount);
	}

	@Override
	public void encode(CipherWrapper cipherWrapper) {
		contents = cipherWrapper.encode(contents);
	}

	@Override
	public void decode(CipherWrapper cipherWrapper) {
		contents = cipherWrapper.decode(contents);
	}
}
