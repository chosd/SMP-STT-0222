package com.kt.smp.common.mapper;

import com.kt.smp.common.domain.BaseModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaseMapper {
	
	/**
	 * 사용자 리스트 조회
	 * @return
	 */
	List<BaseModel> empList();

}
