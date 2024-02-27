/**
 * 
 */
package com.kt.smp.stt.reprocess.dto;

import lombok.Builder;
import lombok.Data;

/**
* @FileName : SttSemiRealTimeRequestDto.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 23.
* @작성자 : rapeech
* @변경이력 :
* @프로그램설명 :
*/
@Data
@Builder
public class SttSemiRealTimeRequestDto {
	private Integer fileSendType;
	private String serviceCode;
	private String callKey;
	private Integer txRxType; // 0: TX(상담사), 1: RX(고객), 2: MONO(보이스봇, 콜봇)
	private String callbackUrl;
}
