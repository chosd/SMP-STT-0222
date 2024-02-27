package com.kt.smp.stt.statistics.repository;

import com.github.pagehelper.Page;
import com.kt.smp.stt.statistics.domain.SttStatisticsErrorVO;
import com.kt.smp.stt.statistics.mapper.SttStatisticsErrorMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class SttStatisticsErrorRepository {

    private final SttStatisticsErrorMapper mapper;

    public SttStatisticsErrorRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(SttStatisticsErrorMapper.class);
    }

    public Page<SttStatisticsErrorVO> list() {
        return mapper.list();
    }

    public int count() {
        return mapper.count();
    }

    public int insert(SttStatisticsErrorVO sttStatisticsErrorVO) {
        return mapper.insert(sttStatisticsErrorVO);
    }
}
