package com.kt.smp.stt.deploy.model.mapper;

import com.github.pagehelper.Page;
import com.kt.smp.stt.deploy.model.domain.SttDeployModelListVO;
import com.kt.smp.stt.deploy.model.domain.SttDeployModelSearchCondition;
import com.kt.smp.stt.deploy.model.domain.SttDeployModelVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * The interface Stt upload mapper.
 */
@Mapper
public interface SttDeployModelMapper {
    // 조회

    /**
     * Detail stt deploy model vo.
     *
     * @param modelId the model id
     * @return the stt deploy model vo
     */
    SttDeployModelVO detail(Long modelId);

    /**
     * List page.
     *
     * @param searchCondition the search condition
     * @return the page
     */
    Page<SttDeployModelVO> list(SttDeployModelSearchCondition searchCondition);
    
    int countDuplicateModelId(String resultModelId);

    /**
     * Count int.
     *
     * @param searchCondition the search condition
     * @return the int
     */
    int count(SttDeployModelSearchCondition searchCondition);

    /**
     * Exists boolean.
     *
     * @param modelId the upload id
     * @return the boolean
     */
    int exists(Long modelId);

    // 등록/수정/삭제

    /**
     * Insert int.
     *
     * @param sttDeployModelVO the stt deploy model vo
     * @return the int
     */
    int insert(SttDeployModelVO sttDeployModelVO);

    /**
     * Update int.
     *
     * @param sttDeployModelVO the stt deploy model vo
     * @return the int
     */
    int update(SttDeployModelVO sttDeployModelVO);

    /**
     * Delete int.
     *
     * @param modelIdList the model id list
     * @return the int
     */
    int delete(SttDeployModelListVO modelIdList);

    /**
     * Gets result model ids.
     *
     * @return the result model ids
     */
    List<String> getResultModelIds();

    /**
     * Gets model path.
     *
     * @param resultModelId the result model id
     * @return the model path
     */
    String getModelPath(String resultModelId);

    /**
     * Detail by result model id stt deploy model vo.
     *
     * @param resultModelId the result model id
     * @return the stt deploy model vo
     */
    SttDeployModelVO detailByResultModelId(String resultModelId);
}
