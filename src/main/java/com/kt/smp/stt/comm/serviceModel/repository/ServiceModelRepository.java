package com.kt.smp.stt.comm.serviceModel.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelListVO;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelSearchCondition;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.mapper.ServiceModelMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ServiceModelRepository {

    private final ServiceModelMapper mapper;
    private static List<ServiceModelVO> SERVICE_MODEL_LIST = new ArrayList<ServiceModelVO>();
    private static int LIST_COUNTER = 0;

    public ServiceModelRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        mapper = sqlSession.getMapper(ServiceModelMapper.class);
    }

    
    public ServiceModelVO detail(Long trainDataId) {
    	ServiceModelVO result = null;
    	for(ServiceModelVO sModel : SERVICE_MODEL_LIST) {
    		if(sModel.getId().equals(trainDataId)) {
    			result = sModel;
    			break;
    		}
    	}
    	log.info("> ServiceModelRepository. detail : " + result);
    	return result;
//        return mapper.detail(trainDataId);
    }

    public ServiceModelVO detailByServiceCode(String serviceCode) {
    	ServiceModelVO result = null;
    	for(ServiceModelVO sModel : SERVICE_MODEL_LIST) {
    		if(sModel.getServiceCode().equals(serviceCode)) {
    			result = sModel;
    			break;
    		}
    	}
    	log.info("> ServiceModelRepository. detailByServiceCode : " + result);
        return result;
//        return mapper.detailByServiceCode(serviceCode);
    }

    public Page<ServiceModelVO> listPage(ServiceModelSearchCondition searchCondition) {
        return mapper.listPage(searchCondition);
    }

    public List<ServiceModelVO> listAll() {
    	log.info("> ServiceModelRepository. listAll : " + LIST_COUNTER);
    	if(SERVICE_MODEL_LIST.isEmpty() || LIST_COUNTER > 20) {
    		restList();
    		LIST_COUNTER = 0;
    	}
    	LIST_COUNTER++;
        return SERVICE_MODEL_LIST;
    }

    public int count(ServiceModelSearchCondition searchCondition) {
        return mapper.count(searchCondition);
    }

    public boolean exists(Long serviceModelId) {
    	boolean result = false;
    	for(ServiceModelVO sModel : SERVICE_MODEL_LIST) {
    		if(sModel.getId().equals(serviceModelId)) {
    			result = true;
    			break;
    		}
    	}
    	log.info("> ServiceModelRepository. exist : " + result);
        return result;
    }

    public boolean existServiceModelName(String serviceModelName) {
        boolean result = false;
    	for(ServiceModelVO sModel : SERVICE_MODEL_LIST) {
    		if(sModel.getServiceModelName().equals(serviceModelName)) {
    			result = true;
    			break;
    		}
    	}
    	log.info("> ServiceModelRepository. existServiceModelName : " + result);
        return result;
//        return mapper.existServiceModelName(serviceModelName) > 0;
    }

    public boolean existServiceCode(String serviceCode) {
    	boolean result = false;
    	for(ServiceModelVO sModel : SERVICE_MODEL_LIST) {
    		if(sModel.getServiceCode().equals(serviceCode)) {
    			result = true;
    			break;
    		}
    	}
    	log.info("> ServiceModelRepository. existServiceCode : " + result);
        return result;
//        return mapper.existServiceCode(serviceCode) > 0;
    }

    public int insert(ServiceModelVO serviceModelVO) {
    	int result = mapper.insert(serviceModelVO);
    	if(result > 0) restList();
    	return result;
    }

    public int update(ServiceModelVO serviceModelVO) {
        int result = mapper.update(serviceModelVO);
        if(result > 0) restList();
    	return result;
    }

    public int delete(ServiceModelListVO serviceModelIdList) {
        int result = mapper.delete(serviceModelIdList);
        if(result > 0) restList();
    	return result;
    }

    public int countDuplicateServiceModelName(String serviceModelName) {
    	int result = 0;
    	for(ServiceModelVO sModel : SERVICE_MODEL_LIST) {
    		if(sModel.getServiceModelName().equals(serviceModelName)) {
    			result ++;
    		}
    	}
    	log.info("> ServiceModelRepository. countDuplicateServiceModelName : " + result);
    	return result;
//        return mapper.countDuplicateServiceModelName(serviceModelName);
    }

    public int countDuplicateServiceCode(String serviceCode) {
    	int result = 0;
    	for(ServiceModelVO sModel : SERVICE_MODEL_LIST) {
    		if(sModel.getServiceCode().equals(serviceCode)) {
    			result ++;
    		}
    	}
    	log.info("> ServiceModelRepository. countDuplicateServiceCode : " + result);
    	return result;
//        return mapper.countDuplicateServiceCode(serviceCode);
    }

    public ServiceModelVO findByName(String name) {
    	ServiceModelVO result = null;
    	for(ServiceModelVO sModel : SERVICE_MODEL_LIST) {
    		if(sModel.getServiceModelName().equals(name)) {
    			result = sModel;
    			break;
    		}
    	}
    	log.info("> ServiceModelRepository. findByName : " + result);
        return result;
//        return mapper.findByName(name);
    }
    
    private void restList() {
    	SERVICE_MODEL_LIST = mapper.listAll();
    }
    
}
