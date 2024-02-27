/**
 * 
 */
package com.kt.smp.stt.comm.preference.dto;

import java.util.List;

import com.kt.smp.stt.comm.preference.enums.DatasToRemove;
import com.kt.smp.stt.comm.preference.enums.FilesToRemove;

import lombok.Data;

@Data
public class RemoveListDto {

	List<DatasToRemove> datasToRemoveList;
	List<FilesToRemove> filesToRemoveList;
}
