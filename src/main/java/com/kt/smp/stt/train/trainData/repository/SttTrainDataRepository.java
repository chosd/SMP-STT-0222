package com.kt.smp.stt.train.trainData.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.kt.smp.stt.comm.directory.dto.DirectoryListDto;
import com.kt.smp.stt.train.trainData.domain.SttTrainAmDataVO;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataAmSearchCondition;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataListVO;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataSearchCondition;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataVO;
import com.kt.smp.stt.train.trainData.dto.SttTrainDataMultipartSaveDto;
import com.kt.smp.stt.train.trainData.dto.SttTrainDataSaveReqDto;
import com.kt.smp.stt.train.trainData.dto.SttTrainDatasetListDto;
import com.kt.smp.stt.train.trainData.mapper.SttTrainDataMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SttTrainDataRepository {

    private final SttTrainDataMapper mapper;

    public SttTrainDataRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(SttTrainDataMapper.class);
    }

    public SttTrainDataVO detail(Long trainDataId) {
        return mapper.detail(trainDataId);
    }
    
    public SttTrainAmDataVO amDetail(Long amDataId) {
        return mapper.amDetail(amDataId);
    }

    public Page<SttTrainDataVO> listPage(SttTrainDataSearchCondition searchCondition) {
        return mapper.listPage(searchCondition);
    }
    
    public Page<SttTrainAmDataVO> amDataSearch(SttTrainDataAmSearchCondition searchCondition) {
    	
    	return mapper.amDataSearch(searchCondition);
    }
    
    public List<SttTrainDatasetListDto> amDatasetAll() {
    	
    	return mapper.amDatasetAll();
    }
    
    public DirectoryListDto directorySearch() {
        
        return mapper.directorySearch();
    }

    public int count(SttTrainDataSearchCondition searchCondition) {
        return mapper.count(searchCondition);
    }
    
    public int amCount(SttTrainDataAmSearchCondition searchCondition) {
        return mapper.amCount(searchCondition);
    }

    public boolean exists(Long trainDataId) {
        return mapper.exists(trainDataId) > 0;
    }
    
    public boolean existsAmData(Long amDataId) {
        return mapper.existsAmData(amDataId) > 0;
    }

    public boolean existContents(String contents) {
        return mapper.existContents(contents) > 0;
    }

    public int insert(SttTrainDataVO sttTrainDataVO) {
        return mapper.insert(sttTrainDataVO);
    }

    public int insertBulk(SttTrainDataSaveReqDto sttTrainDataSaveReqDto) {
        return mapper.insertBulk(sttTrainDataSaveReqDto);
    }

    public int update(SttTrainDataVO sttTrainDataVO) {
        return mapper.update(sttTrainDataVO);
    }
    
    public int amUpdate(SttTrainAmDataVO sttTrainAmDataVO) {
    	
        return mapper.amUpdate(sttTrainAmDataVO);
    }

    public int delete(SttTrainDataListVO trainDataIdList) {
        return mapper.delete(trainDataIdList);
    }

    public int amDelete(SttTrainDataListVO trainAmDataIdList) {
        return mapper.amDelete(trainAmDataIdList);
    }

    public int countDuplicateContents(String contents, String encryptedContents, String serviceModelId) {
        return mapper.countDuplicateContents(contents, encryptedContents, serviceModelId);
    }
    public int countDuplicateDatasetName(String dataset, String serviceModelId) {
        
    	return mapper.countDuplicateDatasetName(dataset, serviceModelId);
    }
    
    public int countDuplicateDirectoryPath(String path,String serviceModelId) {
        
    	return mapper.countDuplicateDirectoryPath(path,serviceModelId);
    }

    public int countServiceModelDirectPath(String serviceModelId){
        return mapper.countServiceModelDirectPath(serviceModelId);
    }
    
    public int amInsert(SttTrainDataMultipartSaveDto dto) {
        return mapper.amInsert(dto);
    }

    public int amDirectInsert(SttTrainDataMultipartSaveDto dto) {
        return mapper.amDirectInsert(dto);
    }

    public int amDataServiceListResultAll(int serviceModelId) {
        return mapper.amDataServiceListResultAll(serviceModelId);
    }
    
}
