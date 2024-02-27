package com.kt.smp.stt.verify.data.mapper;

import com.kt.smp.stt.verify.data.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VerifyDataMapper {
    int count(VerifyDataSearchCondition searchCondition);

    List<VerifyDataListDto> search(VerifyDataSearchCondition searchCondition);

    VerifyDataDto findById(int id);

    List<VerifyDataDto> findByDatasetId(Integer datasetId);
    
    List<VerifyDataDto> findByDatasetName(String datasetName);
    List<VerifyDataListDto> findDatasetGroup();
    
    List<VerifyDataDto> findByDatasetNameId(String name, Integer id);

    VerifyDataDto findByDatasetIdAndWavFileName(Integer serviceModelId, String wavFileName);

    int countByDatasetIdAndWavFileName(String datasetName, String wavFileName);
    
    int countByName(String name);
    
    int countByPath(String path,String code);

    void save(VerifyDataSaveDto newData);

    void update(VerifyDataUpdateDto modifiedData);

    void delete(Integer id);

    void deleteByDatasetId(Integer datasetId);
}
