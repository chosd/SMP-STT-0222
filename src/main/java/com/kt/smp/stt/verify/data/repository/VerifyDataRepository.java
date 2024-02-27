package com.kt.smp.stt.verify.data.repository;

import com.kt.smp.stt.verify.data.dto.*;
import com.kt.smp.stt.verify.data.mapper.VerifyDataMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class VerifyDataRepository {

    private final VerifyDataMapper mapper;

    public VerifyDataRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(VerifyDataMapper.class);
    }

    public int count(VerifyDataSearchCondition searchCondition) {
        return mapper.count(searchCondition);
    }

    public List<VerifyDataListDto> search(VerifyDataSearchCondition searchCondition) {
        return mapper.search(searchCondition);
    }

    public VerifyDataDto findById(int id) {
        return mapper.findById(id);
    }

    public List<VerifyDataDto> findByDatasetId(Integer datasetId) {
        return mapper.findByDatasetId(datasetId);
    }
    
    public List<VerifyDataDto> findByDatasetName(String datasetName) {
        
    	return mapper.findByDatasetName(datasetName);
    }
    
    public List<VerifyDataListDto> findDatasetGroup() {
        
    	return mapper.findDatasetGroup();
    }
    
    public List<VerifyDataDto> findByDatasetNameId(String name, Integer id) {
        return mapper.findByDatasetNameId(name,id);
    }

    public VerifyDataDto findByDatasetIdAndWavFileName(Integer serviceModelId, String wavFileName) {
        return mapper.findByDatasetIdAndWavFileName(serviceModelId, wavFileName);
    }

    public int countByDatasetIdAndWavFileName(String datasetName, String wavFileName) {
        return mapper.countByDatasetIdAndWavFileName(datasetName, wavFileName);
    }

    public int countByName(String name) {
        return mapper.countByName(name);
    }
    
    public int countByPath(String path,String code) {
        return mapper.countByPath(path,code);
    }
    
    public void save(VerifyDataSaveDto newData) {
        mapper.save(newData);
    }

    public void update(VerifyDataUpdateDto modifiedData) {
        mapper.update(modifiedData);
    }

    public void delete(Integer id) {
        mapper.delete(id);
    }

    public void deleteByDatasetId(Integer datasetId) {
        mapper.deleteByDatasetId(datasetId);
    }
}
