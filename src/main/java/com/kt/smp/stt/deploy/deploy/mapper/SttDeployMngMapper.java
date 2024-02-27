package com.kt.smp.stt.deploy.deploy.mapper;

import com.github.pagehelper.Page;
import com.kt.smp.stt.common.ServiceModel;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployMngSearchCondition;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployMngVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface Stt deploy mng mapper.
 *
 * @author kyungtae
 * @title STT 배포 이력 관리 Mapper
 * @see <pre></pre>
 * @since 2022.02.18
 */
@Mapper
public interface SttDeployMngMapper {
    // 조회

    /**
     * Detail stt deploy mng vo.
     *
     * @param deployId the deploy id
     * @return the stt deploy mng vo
     */
    SttDeployMngVO detail(Long deployId);

    /**
     * Detail last one stt deploy mng vo.
     *
     * @param serviceModel the service model
     * @return the stt deploy mng vo
     */
    SttDeployMngVO detailLastOne(String serviceModelId);

    /**
     * Detail by result model id stt deploy mng vo.
     *
     * @param resultModelId the result model id
     * @return the stt deploy mng vo
     */
    SttDeployMngVO detailByResultModelId(String resultModelId);

    /**
     * List list.
     *
     * @param searchCondition the search condition
     * @return the list
     */
    Page<SttDeployMngVO> list(SttDeployMngSearchCondition searchCondition);

    /**
     * Count int.
     *
     * @param searchCondition the request dto
     * @return the int
     */
    int count(SttDeployMngSearchCondition searchCondition);

    /**
     * Exists boolean.
     *
     * @param deployId the deploy id
     * @return the boolean
     */
    int exists(Long deployId);

    // 등록/수정/삭제

    /**
     * Insert int.
     *
     * @param sttDeployMngVO the deploy mng vo
     * @return the int
     */
    int insert(SttDeployMngVO sttDeployMngVO);

    /**
     * Update int.
     *
     * @param sttDeployMngVO the deploy mng vo
     * @return the int
     */
    int update(SttDeployMngVO sttDeployMngVO);

    /**
     * Update callback fields int.
     *
     * @param sttDeployMngVO the stt deploy mng vo
     * @return the int
     */
    int updateCallbackFields(SttDeployMngVO sttDeployMngVO);
}
