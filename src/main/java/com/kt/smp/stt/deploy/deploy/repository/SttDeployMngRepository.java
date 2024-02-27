package com.kt.smp.stt.deploy.deploy.repository;

import com.github.pagehelper.Page;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployMngSearchCondition;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployMngVO;
import com.kt.smp.stt.deploy.deploy.mapper.SttDeployMngMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class SttDeployMngRepository {

    private final SttDeployMngMapper mapper;

    public SttDeployMngRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(SttDeployMngMapper.class);
    }

    public SttDeployMngVO detail(Long deployId) {
        return mapper.detail(deployId);
    }

    public SttDeployMngVO detailLastOne(String serviceModelId) {
        return mapper.detailLastOne(serviceModelId);
    }

    public SttDeployMngVO detailByResultModelId(String resultModelId) {
        return mapper.detailByResultModelId(resultModelId);
    }

    public Page<SttDeployMngVO> list(SttDeployMngSearchCondition searchCondition) {
        return mapper.list(searchCondition);
    }

    public int count(SttDeployMngSearchCondition searchCondition) {
        return mapper.count(searchCondition);
    }

    public boolean exists(Long deployId) {
        return mapper.exists(deployId) > 0;
    }

    public int insert(SttDeployMngVO sttDeployMngVO) {
        return mapper.insert(sttDeployMngVO);
    }

    public int update(SttDeployMngVO sttDeployMngVO) {
        return mapper.update(sttDeployMngVO);
    }

    public int updateCallbackFields(SttDeployMngVO sttDeployMngVO) {
        return mapper.updateCallbackFields(sttDeployMngVO);
    }
}
