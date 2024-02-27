package com.kt.smp.stt.recognition.repository;

import com.kt.smp.stt.recognition.dto.SttRecognitionDetailVO;
import com.kt.smp.stt.recognition.dto.SttRecognitionSearchCondition;
import com.kt.smp.stt.recognition.dto.SttRecognitionVO;
import com.kt.smp.stt.recognition.mapper.SttRecognitionMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SttRecognitionRepository {

    private final SttRecognitionMapper mapper;

    public SttRecognitionRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(SttRecognitionMapper.class);
    }

    public List<SttRecognitionVO> recognitionChartData(SttRecognitionSearchCondition searchCondition) {
        return mapper.recognitionChartData(searchCondition);
    }

    public List<SttRecognitionDetailVO> getRecognitionDetail(SttRecognitionSearchCondition searchParamDto) {
        return mapper.getRecognitionDetail(searchParamDto);
    }
    
}
