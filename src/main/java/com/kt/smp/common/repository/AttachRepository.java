package com.kt.smp.common.repository;

import com.kt.smp.common.domain.AttachVO;
import com.kt.smp.common.mapper.AttachMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class AttachRepository {

	private final AttachMapper mapper;

	public AttachRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
		this.mapper = sqlSession.getMapper(AttachMapper.class);
	}

	public String getAtSeq() {
		return mapper.getAtSeq();
	}

	public List<AttachVO> list(AttachVO vo) {
		return mapper.list(vo);
	}

	public AttachVO detail(AttachVO vo) {
		return mapper.detail(vo);
	}

	public int insert(AttachVO vo) {
		return mapper.insert(vo);
	}

	public int update(AttachVO vo) {
		return mapper.update(vo);
	}

	public int delete(AttachVO vo) {
		return mapper.delete(vo);
	}

}
