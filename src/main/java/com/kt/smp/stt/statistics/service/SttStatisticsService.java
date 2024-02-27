package com.kt.smp.stt.statistics.service;

import com.github.pagehelper.Page;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.statistics.component.SttStatisticsChartDataBuilder;
import com.kt.smp.stt.statistics.domain.SttStatisticsDetailSearchConditionDto;
import com.kt.smp.stt.statistics.domain.SttStatisticsSearchCondition;
import com.kt.smp.stt.statistics.domain.SttStatisticsSearchUnit;
import com.kt.smp.stt.statistics.domain.SttStatisticsVO;
import com.kt.smp.stt.statistics.dto.SttStatisticsSearchResponseDto;
import com.kt.smp.stt.statistics.repository.SttStatisticsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Stt statistics service.
 *
 * @author jaime
 * @title SttStatisticsService
 * @see\n <pre> </pre>
 * @since 2022 -07-18
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SttStatisticsService {

    private final SttStatisticsRepository sttStatisticsRepository;
    
    private final SttStatisticsChartDataBuilder sttStatisticsChartDataBuilder;
    
    private final ServiceModelService serviceModelService;

    /**
     * Detail stt statistics vo.
     *
     * @param searchCondition the search condition
     * @return the stt statistics vo
     */
    public SttStatisticsVO detail(SttStatisticsDetailSearchConditionDto searchCondition) {
        return sttStatisticsRepository.detail(searchCondition);
    }

    /**
     * Exists boolean.
     *
     * @param searchCondition the search condition
     * @return the boolean
     */
    public int exists(SttStatisticsDetailSearchConditionDto searchCondition) {
        return sttStatisticsRepository.exists(searchCondition);
    }

    /**
     * Insert int.
     *
     * @param sttStatisticsVO the stt statistics vo
     * @return the int
     */
    @Transactional
    public int insert(SttStatisticsVO sttStatisticsVO) {
        return sttStatisticsRepository.insert(sttStatisticsVO);
    }

    /**
     * 
    * @MethodName : getStatisticsList
    * @작성일 : 2024. 2. 16.
    * @작성자 : homin.lee
    * @변경이력 :
    * @Method설명 : 통계 리스트 조회
    * @param searchCondition
    * @return
     */
	public SttStatisticsSearchResponseDto getStatisticsList(SttStatisticsSearchCondition searchCondition) {
    	SttStatisticsSearchUnit searchUnit = searchCondition.getSearchUnit();

        Page<SttStatisticsVO> list = sttStatisticsRepository.list(searchCondition);

        Map<String, Page<SttStatisticsVO>> pageHashmap = null;
        Page<SttStatisticsVO> page;
		
        if (ObjectUtils.isEmpty(searchCondition.getServiceCode())) {
        	searchCondition.setServiceCode(sttStatisticsRepository.getMostNumerousServiceCode(searchCondition));
        	pageHashmap = getPageHashmapAll(searchCondition, list);
        	page = pageHashmap.get(searchCondition.getServiceCode());
        } else {
        	page = injectServiceCodeToAllItems(searchCondition, list);
        }
        
		return new SttStatisticsSearchResponseDto(page, pageHashmap, searchUnit);
	}
	
	
	/**
	 * 
	* @MethodName : getStatisticsChartData
	* @작성일 : 2024. 2. 16.
	* @작성자 e: homin.le
	* @변경이력 :
	* @Method설명 : 통계 차트를 조회하여 통계차트 데이터 생성 후 반환 
	* @param searchCondition
	* @return 통계 차트 데이터
	 */
	public JSONObject getStatisticsChartData(SttStatisticsSearchCondition searchCondition) {
		List<SttStatisticsVO> findList = new ArrayList<>();
		if (searchCondition.getSearchUnit() == SttStatisticsSearchUnit.MINUTE) {
			findList = sttStatisticsRepository.getListAllByMinute(searchCondition);
		} else {
			findList = sttStatisticsRepository.getListAll(searchCondition);
		}
		
		Map<String, List<SttStatisticsVO>> serviceCodeChartMap = sttStatisticsChartDataBuilder.getChartHashmap(findList);
        
		JSONObject result = sttStatisticsChartDataBuilder.buildByHashmap(serviceCodeChartMap);
		result.put("searchUnit", searchCondition.getSearchUnit());
		return result;
	}
	
	/**
	 * 
	* @MethodName : getPageHashmapAll
	* @작성일 : 2024. 2. 16.
	* @작성자 : homin.lee
	* @변경이력 :
	* @Method설명 : 모든 통계 데이터를 서비스코드을 Key로 구분하여 Map 반환
	* @param searchCondition
	* @param list
	* @return HashMap<K, V>   K: 서비스코드  V: 서비스코드별 통계 목록 
	 */
    private Map<String, Page<SttStatisticsVO>> getPageHashmapAll(SttStatisticsSearchCondition searchCondition, Page<SttStatisticsVO> list) {
    	
        Map<String, Page<SttStatisticsVO>> hashmap = new HashMap<>();
        
        for (ServiceModelVO serviceModel : serviceModelService.listAll()) {
        	
        	String serviceCode = serviceModel.getServiceCode();
        	
        	Page<SttStatisticsVO> pageList = new Page<SttStatisticsVO>(searchCondition.getPage(), searchCondition.getPageSize());
            pageList.setTotal(list.getTotal());
            pageList.setPages(list.getPages());
            
        	for (SttStatisticsVO vo : list.getResult()) {
                String statisticsServiceCode = vo.getServiceCode();

        		if (statisticsServiceCode == null || statisticsServiceCode.equals(serviceCode)) {
        			pageList.add(vo);
        		}
        	}
        	hashmap.put(serviceCode, pageList);
        }

        return hashmap;
    }
    
    /**
     * 
    * @MethodName : injectServiceCodeToAllItems
    * @작성일 : 2024. 2. 16.
    * @작성자 : homin.lee
    * @변경이력 :
    * @Method설명 : 서비스 코드가 없는 날짜 더미 데이터에도 서비스 코드를 주입한 후 반환
    * @param searchCondition
    * @param list
    * @return list
     */
    private Page<SttStatisticsVO> injectServiceCodeToAllItems(SttStatisticsSearchCondition searchCondition, Page<SttStatisticsVO> list) {
        
        for (SttStatisticsVO vo : list.getResult()) {
        	vo.setServiceCode(searchCondition.getServiceCode());
        }
        
        return list;
    }
}
