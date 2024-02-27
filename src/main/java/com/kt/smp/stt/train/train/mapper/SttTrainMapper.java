package com.kt.smp.stt.train.train.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.kt.smp.stt.train.train.domain.SttTrainSearchCondition;
import com.kt.smp.stt.train.train.domain.SttTrainVO;

/**
 * The interface Stt train mapper.
 *
 * @author jaime
 * @title SttTrainMapper
 * @see\n <pre> </pre>
 * @since 2022 -04-05
 */
@Mapper
public interface SttTrainMapper {

    /**
     * Detail stt train vo.
     *
     * @param trainId the train id
     * @return the stt train vo
     */
    SttTrainVO detail(Long trainId);


    /**
     * Detail last one stt train service.
     *
     * @param serviceModel the service model
     * @return the stt train service
     */
    SttTrainVO detailLastOne(String serviceModelId);

    /**
     * Detail by result model id stt train vo.
     *
     * @param resultModelId the result model id
     * @return the stt train vo
     */
    SttTrainVO detailByResultModelId(String resultModelId);

    /**
     * List page.
     *
     * @param searchCondition the search condition
     * @return the page
     */
    Page<SttTrainVO> list(SttTrainSearchCondition searchCondition);

    /**
     * Count int.
     *
     * @param searchCondition the search condition
     * @return the int
     */
    int count(SttTrainSearchCondition searchCondition);

    /**
     * Exists boolean.
     *
     * @param trainId the train id
     * @return the int (0 or 1)
     */
    int exists(Long trainId);

    /**
     * Insert int.
     *
     * @param sttTrainVO the stt train vo
     * @return the int
     */
    int insert(SttTrainVO sttTrainVO);

    /**
     * Update int.
     *
     * @param sttTrainVO the stt train vo
     * @return the int
     */
    int update(SttTrainVO sttTrainVO);

    /**
     * Update int.
     *
     * @param sttTrainVO the stt train vo
     * @return the int
     */
    int detailUpdate(SttTrainVO sttTrainVO);

    /**
     * Update callback fields int.
     *
     * @param sttTrainVO the stt train vo
     * @return the int
     */
    int updateCallbackFields(SttTrainVO sttTrainVO);
}
