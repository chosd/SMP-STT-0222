package com.kt.smp.stt.comm.serviceModel.service;

import com.github.pagehelper.Page;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelListVO;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelSearchCondition;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.repository.ServiceModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type SMP_STT API_연동규격서 결과코드 3.3 정의에 따름 service model service.
 *
 * @author jieun.chang
 * @title ServiceModelService
 * @see\n <pre> </pre>
 * @since 2022-12-15
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ServiceModelService {

    private final ServiceModelRepository serviceModelRepository;

    /**
     * listPage.
     *
     * @param searchCondition the search condition
     * @return the listPage
     */
    public Page<ServiceModelVO> listPage(ServiceModelSearchCondition searchCondition) {
        Page<ServiceModelVO> page = serviceModelRepository.listPage(searchCondition);
        return page;
    }

    /**
     * list.
     * @return the list
     */
    public List<ServiceModelVO> listAll() {
        return serviceModelRepository.listAll();
    }

    /**
     * Detail stt service model vo.
     *
     * @param serviceModelId the service model id
     * @return the stt service model vo
     */
    public ServiceModelVO detail(long serviceModelId) {
    	log.info(">>> detail serviceModelId : " + serviceModelId);
    	boolean result = serviceModelRepository.exists(serviceModelId);
        if (!result) {
            throw new IllegalStateException("ERROR: serviceModelId does not exists");
        }

        ServiceModelVO detail = serviceModelRepository.detail(serviceModelId);
        return detail;
    }

    public ServiceModelVO detailS(String serviceModelId) {
    	log.info(">>> detailS serviceModelId : " + serviceModelId);
    	boolean result = serviceModelRepository.existServiceCode(serviceModelId);
    	if (!result) {
    		throw new IllegalStateException("ERROR: serviceModelId does not exists");
    	}
    	
    	ServiceModelVO detail = serviceModelRepository.detailByServiceCode(serviceModelId);
    	return detail;
    }
    
    public ServiceModelVO detailByServiceCode(String serviceCode) {
    	log.info(">>> detailByServiceCode serviceCode : " + serviceCode);
        if (!serviceModelRepository.existServiceCode(serviceCode)) {
            throw new IllegalStateException("ERROR: serviceCode does not exists");
        }

        ServiceModelVO detail = serviceModelRepository.detailByServiceCode(serviceCode);
        return detail;
    }

    /**
     * Count int.
     *
     * @param searchCondition the search condition
     * @return the int
     */
    public int count(ServiceModelSearchCondition searchCondition) {
        return serviceModelRepository.count(searchCondition);
    }

    /**
     * Insert int.
     *
     * @param serviceModelVO the stt service model vo
     * @return the int
     */
    @Transactional
    public int insert(ServiceModelVO serviceModelVO) {

        if (hasInvalidServiceModelName(serviceModelVO.getServiceModelName())) {
            throw new IllegalArgumentException("Error: Invalid insert service model name request");
        }

        if (hasInvalidServiceCode(serviceModelVO.getServiceCode())) {
            throw new IllegalArgumentException("Error: Invalid insert service code request");
        }

        if (hasInvalidDescription(serviceModelVO.getDescription())) {
            throw new IllegalArgumentException("Error: Invalid insert description request");
        }

        return serviceModelRepository.insert(serviceModelVO);
    }

    /**
     * Update int.
     *
     * @param serviceModelVO the stt service model vo
     * @return the int
     */
    @Transactional
    public int update(ServiceModelVO serviceModelVO) {

        if (hasInvalidServiceModelName(serviceModelVO.getServiceModelName())) {
            throw new IllegalArgumentException("Error: Invalid update service model name request");
        }

        if (hasInvalidServiceCode(serviceModelVO.getServiceCode())) {
            throw new IllegalArgumentException("Error: Invalid update service code request");
        }

        if (hasInvalidDescription(serviceModelVO.getDescription())) {
            throw new IllegalArgumentException("Error: Invalid insert description request");
        }

        return serviceModelRepository.update(serviceModelVO);
    }

    /**
     * Delete int.
     *
     * @param serviceModelIdList the service model id list
     * @return the int
     */
    @Transactional
    public int delete(ServiceModelListVO serviceModelIdList) {
        return serviceModelRepository.delete(serviceModelIdList);
    }

    /**
     * hasDuplicateServiceModelName int.
     *
     * @param serviceModelName
     * @return the int
     */
    public boolean hasDuplicateServiceModelName(String serviceModelName) {
        return serviceModelRepository.countDuplicateServiceModelName(serviceModelName) > 0;
    }

    public boolean hasDuplicateServiceCode(String serviceCode) {
        return serviceModelRepository.countDuplicateServiceCode(serviceCode) > 0;
    }

    public boolean hasInvalidServiceModelName(String serviceModelName) {

        // 255자 이내
        if (serviceModelName.length() > 255 || serviceModelName.length() < 0) {
            return true;
        }
        return false;
    }

    public boolean hasInvalidServiceCode(String serviceCode) {

        // 255자 이내
        if (serviceCode.length() > 255 || serviceCode.length() < 0) {
            return true;
        }
        return false;
    }

    public boolean hasInvalidDescription(String description) {
        // 255자 이내
        if (description.length() > 255 || description.length() < 0) {
            return true;
        }
        return false;
    }

    public ServiceModelVO getByName(String name) {
        return serviceModelRepository.findByName(name);
    }
}
