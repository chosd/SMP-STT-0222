/**
 * 
 */
package com.kt.smp.stt.reprocess.dto;

import com.kt.smp.stt.reprocess.enums.ReprocessStatus;

import lombok.Data;

/**
* @FileName : ReprocessLogDto.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 16.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 :
*/
@Data
public class ReprocessLogDto {
	private String sttId;
	private String recId;
	private String applicationId;
	private String callKey;
	private ReprocessStatus reprocessStatus;
	private String serviceCode;
	private String recStarttime;
	private String sttResults;
}
