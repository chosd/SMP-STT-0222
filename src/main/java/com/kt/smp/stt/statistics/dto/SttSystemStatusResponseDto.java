/**
 * 
 */
package com.kt.smp.stt.statistics.dto;


import java.util.List;
import com.kt.smp.stt.common.dto.BaseResultDto;
import com.kt.smp.stt.statistics.domain.SttServerInfoVO;

import lombok.Getter;
import lombok.Setter;

/**
*@FileName : SttSystemResponseDto.java
@Project : kt-stt-service_r
@Date : 2023. 10. 16.
*@작성자 : 심수연
*@변경이력 :
*@프로그램설명 :
*/
@Getter
@Setter
public class SttSystemStatusResponseDto extends BaseResultDto {

	public List<SttServerInfoVO> serverInfo; 
	
}

