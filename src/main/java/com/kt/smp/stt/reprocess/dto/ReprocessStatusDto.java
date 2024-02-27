/**
 * 
 */
package com.kt.smp.stt.reprocess.dto;

import com.kt.smp.stt.reprocess.enums.ReprocessStatus;

import lombok.Data;

/**
* @FileName : SttReprocessStatusDto.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 18.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 :
*/
@Data
public class ReprocessStatusDto {
	private ReprocessStatus reprocessStatus;
	private Integer reprocessStatusCode;
}
