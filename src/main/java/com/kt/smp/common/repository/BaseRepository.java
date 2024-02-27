package com.kt.smp.common.repository;

import com.kt.smp.common.domain.BaseModel;
import com.kt.smp.common.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BaseRepository {

	private final BaseMapper mapper;

	public BaseRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
		this.mapper = sqlSession.getMapper(BaseMapper.class);
	}

	public List<BaseModel> empList() {
		return mapper.empList();
	}
	
}
