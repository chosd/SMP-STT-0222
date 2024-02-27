package com.kt.smp.stt.verify.dataset.mapper;

import com.kt.smp.stt.verify.dataset.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VerifyDatasetMapper {
    int count(VerifyDatasetSearchCondition searchCondition);

    List<VerifyDatasetListDto> search(VerifyDatasetSearchCondition searchCondition);

    List<VerifyDatasetListDto> findAll();

    VerifyDatasetDto findById(int id);

    int countByName(String name);

    int countByDirectory(Integer directory);

    void save(VerifyDatasetSaveDto newDataset);

    void update(VerifyDatasetUpdateDto modifiedDataset);

    void delete(Integer id);
}
