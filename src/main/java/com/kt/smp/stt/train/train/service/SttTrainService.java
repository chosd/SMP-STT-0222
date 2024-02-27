package com.kt.smp.stt.train.train.service;

import com.kt.smp.stt.train.trainData.domain.SttTrainAmDataVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.kt.smp.stt.train.train.domain.SttTrainSearchCondition;
import com.kt.smp.stt.train.train.domain.SttTrainVO;
import com.kt.smp.stt.train.train.repository.SttTrainRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Stt train service.
 *
 * @author jaime
 * @title SttTrainService
 * @see\n <pre> </pre>
 * @since 2022 -04-05
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SttTrainService {
    
    private final SttTrainRepository sttTrainRepository;

    /**
     * List list.
     *
     * @param searchCondition the search condition
     * @return the list
     */
    public Page<SttTrainVO> list(SttTrainSearchCondition searchCondition) {
        return sttTrainRepository.list(searchCondition);
    }

    /**
     * Detail stt train vo.
     *
     * @param trainId the train id
     * @return the stt train vo
     */
    public SttTrainVO detail(long trainId) {
        if (!sttTrainRepository.exists(trainId)) {
            throw new IllegalStateException("ERROR: trainId does not exists");
        }

        return sttTrainRepository.detail(trainId);
    }

    /**
     * Detail last one stt train vo.
     *
     * @param serviceModel the service model
     * @return the stt train vo
     */
    public SttTrainVO detailLastOne(String serviceModelId) {
        return sttTrainRepository.detailLastOne(serviceModelId);
    }

    /**
     * Detail by result model id stt train vo.
     *
     * @param resultModelId the result model id
     * @return the stt train vo
     */
    public SttTrainVO detailByResultModelId(String resultModelId) {
        return sttTrainRepository.detailByResultModelId(resultModelId);
    }

    /**
     * Count int.
     *
     * @param searchCondition the search condition
     * @return the int
     */
    public int count(SttTrainSearchCondition searchCondition) {
        return sttTrainRepository.count(searchCondition);
    }

    /**
     * Insert int.
     *
     * @param sttTrainVO the stt train vo
     * @return the int
     */
    @Transactional
    public int insert(SttTrainVO sttTrainVO) {
        return sttTrainRepository.insert(sttTrainVO);
    }

    /**
     * Update int.
     *
     * @param sttTrainVO the stt train vo
     * @return the int
     */
    @Transactional
    public int update(SttTrainVO sttTrainVO) {
        log.info("[sttTrainVO] {}", sttTrainVO);

        return sttTrainRepository.update(sttTrainVO);
    }

    @Transactional
    public int detailUpdate(SttTrainVO SttTrainVO) {
        log.info("[sttTrainVO] {}", SttTrainVO);

        return sttTrainRepository.detailUpdate(SttTrainVO);
    }

    /**
     * Update callback fields int.
     *
     * @param sttTrainVO the stt train vo
     * @return the int
     */
    @Transactional
    public int updateCallbackFields(SttTrainVO sttTrainVO) {
        return sttTrainRepository.updateCallbackFields(sttTrainVO);
    }
}
