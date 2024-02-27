/**
 * 
 */
package com.kt.smp.stt.comm.preference.dto;

import lombok.Data;

@Data
public class ThresholdConfigDto {
	
	private int cpu;			// CPU 사용률 임계치
	
	private int memory;			// 여유 메모리 임계치
	
	private int storage;		// 여유 디스크 저장소 임계치
	
	public ThresholdConfigDto() {}
	
	public ThresholdConfigDto(int cpu, int memory, int storage) {
		this.cpu = cpu;
		this.memory = memory;
		this.storage = storage;
	}
}
