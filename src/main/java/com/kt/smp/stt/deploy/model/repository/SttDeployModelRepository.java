package com.kt.smp.stt.deploy.model.repository;

import com.github.pagehelper.Page;
import com.kt.smp.stt.deploy.model.domain.SttDeployModelListVO;
import com.kt.smp.stt.deploy.model.domain.SttDeployModelSearchCondition;
import com.kt.smp.stt.deploy.model.domain.SttDeployModelVO;
import com.kt.smp.stt.deploy.model.mapper.SttDeployModelMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SttDeployModelRepository {

    private final SttDeployModelMapper mapper;

    public SttDeployModelRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(SttDeployModelMapper.class);
    }

    public SttDeployModelVO detail(Long modelId) {
        return mapper.detail(modelId);
    }

    public Page<SttDeployModelVO> list(SttDeployModelSearchCondition searchCondition) {
        return mapper.list(searchCondition);
    }

    public int countDuplicateModelId(String resultModelId) {
        
    	return mapper.countDuplicateModelId(resultModelId);
    }
    
    public int count(SttDeployModelSearchCondition searchCondition) {
        return mapper.count(searchCondition);
    }

    public boolean exists(Long modelId) {
        return mapper.exists(modelId) > 0;
    }

    public int insert(SttDeployModelVO sttDeployModelVO) {
        return mapper.insert(sttDeployModelVO);
    }

    public int update(SttDeployModelVO sttDeployModelVO) {
        return mapper.update(sttDeployModelVO);
    }

    public int delete(SttDeployModelListVO modelIdList) {
        return mapper.delete(modelIdList);
    }

    public List<String> getResultModelIds() {
        return mapper.getResultModelIds();
    }

    public String getModelPath(String resultModelId) {
        return mapper.getModelPath(resultModelId);
    }

    public SttDeployModelVO detailByResultModelId(String resultModelId) {
        return mapper.detailByResultModelId(resultModelId);
    }
}
