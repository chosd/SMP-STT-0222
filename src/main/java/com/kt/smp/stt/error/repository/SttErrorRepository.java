package com.kt.smp.stt.error.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.kt.smp.stt.error.dto.SttErrorInsertDto;
import com.kt.smp.stt.error.dto.SttErrorListDto;
import com.kt.smp.stt.error.dto.SttErrorSearchDto;
import com.kt.smp.stt.error.dto.SttErrorTypeDto;
import com.kt.smp.stt.error.mapper.SttErrorMapper;

@Repository
public class SttErrorRepository {

    private SttErrorMapper mapper;
    
    public SttErrorRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(SttErrorMapper.class);
    }

    public int insert(List<SttErrorInsertDto> dto) {
        return mapper.insert(dto);
    }
    
    public List<SttErrorTypeDto> selectType() {
        return mapper.selectType();
    }
    
    public Page<SttErrorListDto> list(SttErrorSearchDto sttErrorSearchDto) {
        return mapper.list(sttErrorSearchDto);
    }
}
