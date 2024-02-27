/**
 * 
 */
package com.kt.smp.stt.statistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kt.smp.common.domain.BaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
*@FileName : SttServerInfoVO.java
@Project : kt-stt-service_r
@Date : 2023. 10. 17.
*@작성자 : rapeech
*@변경이력 :
*@프로그램설명 :
*/
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SttServerInfoVO extends BaseModel {
	@JsonIgnore
	private static final long serialVersionUID = -9203972760520074045L;
	
	private String serverName;
	private Integer cpuUsed;
	private Long maxMemorySize;
	private Long freeMemorySize;
	private Long maxAppStorageSize;
	private Long freeAppStorageSize;
	private Long bps;
	private Long pps;
	private String lastCheckTime;

}