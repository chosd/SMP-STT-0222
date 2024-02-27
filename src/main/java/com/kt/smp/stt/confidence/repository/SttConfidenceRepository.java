package com.kt.smp.stt.confidence.repository;

import com.kt.smp.stt.confidence.domain.SttConfidenceSearchCondition;
import com.kt.smp.stt.confidence.domain.SttConfidenceVO;
import com.kt.smp.stt.confidence.mapper.SttConfidenceMapper;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class SttConfidenceRepository {

    private final SttConfidenceMapper mapper;

    public SttConfidenceRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(SttConfidenceMapper.class);
    }

    public List<SttConfidenceVO> confidenceChartData(SttConfidenceSearchCondition searchCondition) {
        return mapper.confidenceChartData(searchCondition);
    }
}
