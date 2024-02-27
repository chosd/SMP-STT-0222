package com.kt.smp.stt.comm.serviceModel.mapper;

import com.github.pagehelper.Page;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelListVO;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelSearchCondition;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * The interface SMP_STT API_연동규격서 결과코드 3.3 정의에 따름 service model mapper.
 */
@Mapper
public interface ServiceModelMapper {
    // 조회

    /**
     * Detail stt service model vo.
     *
     * @param trainDataId the service model id
     * @return the stt service model vo
     */
    ServiceModelVO detail(Long trainDataId);

    ServiceModelVO detailByServiceCode(String serviceCode);

    /**
     * List page.
     *
     * @param searchCondition the search condition
     * @return the page
     */
    Page<ServiceModelVO> listPage(ServiceModelSearchCondition searchCondition);

    /**
     * List page.
     *
     * @return the page
     */
    List<ServiceModelVO> listAll();

    /**
     * Count int.
     *
     * @param searchCondition the search condition
     * @return the int
     */
    int count(ServiceModelSearchCondition searchCondition);

    /**
     * Exists boolean.
     *
     * @param serviceModelId the service model id
     * @return the boolean
     */
    int exists(Long serviceModelId);

    /**
     * Exists boolean.
     *
     * @param serviceModelName the service model contents
     * @return the boolean
     */
    int existServiceModelName(String serviceModelName);
    
    /**
     * Exists boolean.
     *
     * @param serviceCode the service model contents
     * @return the boolean
     */
    int existServiceCode(String serviceCode);
    

    // 등록/수정/삭제

    /**
     * Insert int.
     *
     * @param serviceModelVO the stt service model vo
     * @return the int
     */
    int insert(ServiceModelVO serviceModelVO);

    /**
     * Update int.
     *
     * @param serviceModelVO the stt service model vo
     * @return the int
     */
    int update(ServiceModelVO serviceModelVO);

    /**
     * Delete int.
     *
     * @param serviceModelIdList the service model id list
     * @return the int
     */
    int delete(ServiceModelListVO serviceModelIdList);

    /**
     * countDuplicateServiceModelName int.
     *
     * @param serviceModelName the service model name
     * @return the int
     */
    int countDuplicateServiceModelName(String serviceModelName);
    
    /**
     * countDuplicateServiceCode int.
     *
     * @param serviceCode the service code
     * @return the int
     */
    int countDuplicateServiceCode(String serviceCode);

    ServiceModelVO findByName(String name);
}
