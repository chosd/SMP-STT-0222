package com.kt.smp.stt.deploy.deploy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployMngSearchCondition;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployMngVO;
import com.kt.smp.stt.deploy.deploy.repository.SttDeployMngRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Stt deploy mng service.
 *
 * @author kyungtae
 * @title STT 배포 이력 관리 Service
 * @see <pre></pre>
 * @since 2022.02.18
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SttDeployMngService {

    private final SttDeployMngRepository sttDeployMngRepository;

    /**
     * List list.
     *
     * @param searchCondition the search condition
     * @return the list
     */
    public Page<SttDeployMngVO> list(SttDeployMngSearchCondition searchCondition) {
        return sttDeployMngRepository.list(searchCondition);
    }

    /**
     * Detail stt deploy mng vo.
     *
     * @param deployId the deploy id
     * @return the stt deploy mng vo
     */
    public SttDeployMngVO detail(long deployId) {
        if (!sttDeployMngRepository.exists(deployId)) {
            throw new IllegalStateException("ERROR: deployId does not exists");
        }

        return sttDeployMngRepository.detail(deployId);
    }

    /**
     * Detail last one stt deploy mng vo.
     *
     * @param serviceModel the service model
     * @return the stt deploy mng vo
     */
    public SttDeployMngVO detailLastOne(String serviceModelId) {
        return sttDeployMngRepository.detailLastOne(serviceModelId);
    }

    /**
     * Detail by result model id stt deploy mng vo.
     *
     * @param resultModelId the result model id
     * @return the stt deploy mng vo
     */
    public SttDeployMngVO detailByResultModelId(String resultModelId) {
        return sttDeployMngRepository.detailByResultModelId(resultModelId);
    }

    /**
     * Count int.
     *
     * @param searchCondition the search condition
     * @return the int
     */
    public int count(SttDeployMngSearchCondition searchCondition) {
        return sttDeployMngRepository.count(searchCondition);
    }

    /**
     * Insert int.
     *
     * @param sttDeployMngVO the stt deploy mng vo
     * @return the int
     */
    @Transactional
    public int insert(SttDeployMngVO sttDeployMngVO) {
        return sttDeployMngRepository.insert(sttDeployMngVO);
    }

    /**
     * Update int.
     *
     * @param sttDeployMngVO the stt deploy mng vo
     * @return the int
     */
    @Transactional
    public int update(SttDeployMngVO sttDeployMngVO) {
//        log.info(">>>> receive sttDeployMngVO {}", sttDeployMngVO);
        log.info(">>>> receive sttDeployMngVO status : " + sttDeployMngVO.getStatus());

        return sttDeployMngRepository.update(sttDeployMngVO);
    }

    /**
     * Update callback fields int.
     *
     * @param sttDeployMngVO the stt deploy mng vo
     * @return the int
     */
    @Transactional
    public int updateCallbackFields(SttDeployMngVO sttDeployMngVO) {
        return sttDeployMngRepository.updateCallbackFields(sttDeployMngVO);
    }
}
