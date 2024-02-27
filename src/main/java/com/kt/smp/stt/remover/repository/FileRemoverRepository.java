/**
 * 
 */
package com.kt.smp.stt.remover.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.kt.smp.stt.remover.dto.RemoveAmDataDirectoryDto;
import com.kt.smp.stt.remover.mapper.FileRemoverMapper;

@Repository
public class FileRemoverRepository {
	
	private final FileRemoverMapper mapper;
	
	public FileRemoverRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(FileRemoverMapper.class);
    }

	
	public List<RemoveAmDataDirectoryDto> findRemoveAmDataDirectoryList(int removerStandard) {
		return mapper.findRemoveAmDataDirectoryList(removerStandard);
	}

	public List<String> findRemoveCallInfoDirectoryList(int removerStandard) {
		return mapper.findRemoveCallInfoDirectoryList(removerStandard);
	}

	public List<String> findRemoveDeployModelDirectoryList(int removerStandard) {
		return mapper.findRemoveDeployModelDirectoryList(removerStandard);
	}

	public List<String> findRemoveVerifyDataDirectoryList(int removerStandard) {
		return mapper.findRemoveVerifyDataDirectoryList(removerStandard);
	}
	
	public void removeFileRelatedData(int removerStandard, List<String> datasToRemove) {
		mapper.removeFileRelatedData(removerStandard, datasToRemove);
	}

}
