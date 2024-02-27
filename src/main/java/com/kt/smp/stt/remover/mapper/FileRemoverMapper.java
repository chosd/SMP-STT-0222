/**
 * 
 */
package com.kt.smp.stt.remover.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kt.smp.stt.remover.dto.RemoveAmDataDirectoryDto;

public interface FileRemoverMapper {

	List<RemoveAmDataDirectoryDto> findRemoveAmDataDirectoryList(int removerStandard);

	List<String> findRemoveCallInfoDirectoryList(int removerStandard);

	List<String> findRemoveDeployModelDirectoryList(int removerStandard);

	List<String> findRemoveVerifyDataDirectoryList(int removerStandard);

	void removeFileRelatedData(@Param("removerStandard") int removerStandard, @Param("datasToRemove") List<String> datasToRemove);

}
