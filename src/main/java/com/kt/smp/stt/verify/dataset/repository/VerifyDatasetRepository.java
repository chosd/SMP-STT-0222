package com.kt.smp.stt.verify.dataset.repository;

import com.kt.smp.stt.verify.dataset.dto.*;
import com.kt.smp.stt.verify.dataset.mapper.VerifyDatasetMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class VerifyDatasetRepository {

    private final VerifyDatasetMapper mapper;

    public VerifyDatasetRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(VerifyDatasetMapper.class);
    }

    public int count(VerifyDatasetSearchCondition searchCondition) {
        return mapper.count(searchCondition);
    }

    public List<VerifyDatasetListDto> search(VerifyDatasetSearchCondition searchCondition) {
        return mapper.search(searchCondition);
    }

    public List<VerifyDatasetListDto> findAll() {
        return mapper.findAll();
    }

    public VerifyDatasetDto findById(int id) {
        return mapper.findById(id);
    }

    public int countByName(String name) {
        return mapper.countByName(name);
    }

    public int countByDirectory(Integer directory) {
        return mapper.countByDirectory(directory);
    }

    public void save(VerifyDatasetSaveDto newDataset) {
        mapper.save(newDataset);
    }

    public void update(VerifyDatasetUpdateDto modifiedDataset) {
        mapper.update(modifiedDataset);
    }

    public void delete(Integer id) {
        mapper.delete(id);
    }
}
