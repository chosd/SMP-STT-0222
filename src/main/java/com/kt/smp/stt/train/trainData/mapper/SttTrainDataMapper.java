package com.kt.smp.stt.train.trainData.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

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

/**
 * The interface SMP_STT API_연동규격서 결과코드 3.3 정의에 따름 train data mapper.
 */
/**
 *@FileName : SttTrainDataMapper.java
 *@Project : STT_SMP_Service
 *@Date : 2023. 10. 13.
 *@작성자 : munho.jang
 *@변경이력 : 
 *@프로그램설명 : 
 */
@Mapper
public interface SttTrainDataMapper {
    // 조회

    /**
     * Detail stt train data vo.
     *
     * @param trainDataId the train data id
     * @return the stt train data vo
     */
    SttTrainDataVO detail(Long trainDataId);
    
    SttTrainAmDataVO amDetail(Long amDataId);

    /**
     * List page.
     *
     * @param searchCondition the search condition
     * @return the page
     */
    Page<SttTrainDataVO> listPage(SttTrainDataSearchCondition searchCondition);
    
    Page<SttTrainAmDataVO> amDataSearch(SttTrainDataAmSearchCondition searchCondition);
    
    List<SttTrainDatasetListDto> amDatasetAll();
    
    DirectoryListDto directorySearch();

    /**
     * Count int.
     *
     * @param searchCondition the search condition
     * @return the int
     */
    int count(SttTrainDataSearchCondition searchCondition);
    
    int amCount(SttTrainDataAmSearchCondition searchCondition);

    /**
     * Exists boolean.
     *
     * @param trainDataId the train data id
     * @return the int (0 or 1)
     */
    int exists(Long trainDataId);
    
    int existsAmData(Long amDataId);

    /**
     * Exists boolean.
     *
     * @param contents the train data contents
     * @return the int (0 or 1)
     */
    int existContents(String contents);

    // 등록/수정/삭제

    /**
     * Insert int.
     *
     * @param sttTrainDataVO the stt train data vo
     * @return the int
     */
    int insert(SttTrainDataVO sttTrainDataVO);

    /**
     * Insert int.
     *
     * @param sttTrainDataSaveReqDto the stt train data vo
     * @return the int
     */
    int insertBulk(SttTrainDataSaveReqDto sttTrainDataSaveReqDto);

    /**
     * Update int.
     *
     * @param sttTrainDataVO the stt train data vo
     * @return the int
     */
    int update(SttTrainDataVO sttTrainDataVO);
    
    int amUpdate(SttTrainAmDataVO sttTrainAmDataVO);

    /**
     * Delete int.
     *
     * @param trainDataIdList the train data id list
     * @return the int
     */
    int delete(SttTrainDataListVO trainDataIdList);
    
    int amDelete(SttTrainDataListVO trainAmDataIdList);

    /**
     * countDuplicateContents int.
     *
     * @param contents the train data content
     * @return the int
     */
    int countDuplicateContents(String contents, String encryptedContents, String serviceModelId);
    
    int countDuplicateDatasetName(String dataset, String serviceModelId);
    
    int countDuplicateDirectoryPath(String path,String serviceModelId);
    
    /**
     *@MethodName : amInsert
     *@작성일 : 2023. 10. 13.
     *@작성자 : munho.jang
     *@변경이력 : 
     *@Method설명 : AM학습데이터 신규화면의 등록처리 
     * @param dto
     * @return
     */
    int amInsert(SttTrainDataMultipartSaveDto dto);


    /**
     *@MethodName : amDirectInsert
     *@작성일 : 2024. 01. 17.
     *@작성자 : chanmi.joo
     *@변경이력 :
     *@Method설명 : AM학습데이터 초기 대량 데이터 직접 등록
     * @param dto
     * @return
     */
    int amDirectInsert(SttTrainDataMultipartSaveDto dto);

    /**
     *@MethodName : amDataServiceListResultAll
     *@작성일 : 2024. 01. 18.
     *@작성자 : chanmi.joo
     *@변경이력 :
     *@Method설명 : AM학습데이터 아이디에 해당하는 서비스 모델 데이터 수 리턴
     * @param serviceModelId
     * @return int
     */
    int amDataServiceListResultAll(int serviceModelId);


    /**
     * @MethodName : countServiceModelDirectPath
     * @작성일 : 2024. 1. 19.
     * @작성자 : chanmi.joo
     * @변경이력 :
     * @Method설명 : AM학습데이터 해당 서비스 모델에 이미 등록된 초기 데이터 경로가 있는지 체크
     * @param serviceModelId
     * @return int
     */
    int countServiceModelDirectPath(String serviceModelId);
}
