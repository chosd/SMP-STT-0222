/**
 * 
 */
package com.kt.smp.stt.comm.preference.dto;

import java.util.List;

import lombok.Data;

@Data
public class RemoveStringListDto {
	
	List<String> datasToRemoveList;
	List<String> filesToRemoveList;

}
