package com.kt.smp.stt.train.train.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.kt.smp.stt.train.train.domain.SttTrainSearchCondition;
import com.kt.smp.stt.train.train.domain.SttTrainVO;
import com.kt.smp.stt.train.train.mapper.SttTrainMapper;

@Repository
public class SttTrainRepository {

    private final SttTrainMapper mapper;

    public SttTrainRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(SttTrainMapper.class);
    }

    public SttTrainVO detail(Long trainId) {
        return mapper.detail(trainId);
    }

    public SttTrainVO detailLastOne(String serviceModelId) {
        return mapper.detailLastOne(serviceModelId);
    }

    public SttTrainVO detailByResultModelId(String resultModelId) {
        return mapper.detailByResultModelId(resultModelId);
    }

    public Page<SttTrainVO> list(SttTrainSearchCondition searchCondition) {
        return mapper.list(searchCondition);
    }

    public int count(SttTrainSearchCondition searchCondition) {
        return mapper.count(searchCondition);
    }

    public boolean exists(Long trainId) {
        return mapper.exists(trainId) > 0;
    }

    public int insert(SttTrainVO sttTrainVO) {
        return mapper.insert(sttTrainVO);
    }

    public int update(SttTrainVO sttTrainVO) {
        return mapper.update(sttTrainVO);
    }

    public int detailUpdate(SttTrainVO sttTrainVO) {
        return mapper.detailUpdate(sttTrainVO);
    }

    public int updateCallbackFields(SttTrainVO sttTrainVO) {
        return mapper.updateCallbackFields(sttTrainVO);
    }
}
