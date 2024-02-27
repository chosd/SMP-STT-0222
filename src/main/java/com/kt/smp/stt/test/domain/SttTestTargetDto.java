/**
 * 
 */
package com.kt.smp.stt.test.domain;

import lombok.Builder;
import lombok.Data;

/**
* @FileName : SttTestTargetDto.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 31.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 :
*/
@Data
@Builder
public class SttTestTargetDto {
	
	private final String host;
	private final String propertyName;
}
