package com.kt.smp.stt.deploy.model.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kt.smp.fileutil.builder.SxssfExcelBuilder;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.deploy.model.domain.DeployMngModel;
import com.kt.smp.stt.deploy.model.domain.SttDeployModelListVO;
import com.kt.smp.stt.deploy.model.domain.SttDeployModelSearchCondition;
import com.kt.smp.stt.deploy.model.domain.SttDeployModelVO;
import com.kt.smp.stt.deploy.model.repository.SttDeployModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * The type Stt upload service.
 *
 * @author jaime
 * @title SttDeployService
 * @see\n <pre> </pre>
 * @since 2022 -02-21
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SttDeployModelService {

    private static final int MAX_PAGE_SIZE = 10000;

    private final ServiceModelService serviceModelService;

    private final SttDeployModelRepository sttDeployModelRepository;

    /**
     * List list.
     *
     * @param searchCondition the search condition
     * @return the list
     */
    public Page<SttDeployModelVO> list(SttDeployModelSearchCondition searchCondition) {
        return sttDeployModelRepository.list(searchCondition);
    }

    /**
     * Detail stt upload vo.
     *
     * @param modelId the upload id
     * @return the stt upload vo
     */
    public SttDeployModelVO detail(long modelId) {
        if (!sttDeployModelRepository.exists(modelId)) {
            throw new IllegalStateException("ERROR: uploadId does not exists");
        }

        return sttDeployModelRepository.detail(modelId);
    }
    
    public boolean hasDuplicateResultModelId(String resultModelId) {
        
  	  return (sttDeployModelRepository.countDuplicateModelId(resultModelId) > 0);
    }

    /**
     * Count int.
     *
     * @param searchCondition the search condition
     * @return the int
     */
    public int count(SttDeployModelSearchCondition searchCondition) {
        return sttDeployModelRepository.count(searchCondition);
    }

    /**
     * Gets deploy mng models.
     *
     * @return the deploy mng models
     */
    public DeployMngModel getDeployMngModels() {
        List<SttDeployModelVO> deployUploadVOS = getAllDeployModelVOs();
        List<Map<String, String>> resultModelId2Descriptions = new ArrayList<>();
        List<Map<String, String>> resultModelId2ModelTypes = new ArrayList<>(); // 기존 화면에 모델타입 추가하기위함
        Map<String, List<String>> serviceCode2ResultModelIds = new HashMap<>();

        for (ServiceModelVO serviceModelVO : serviceModelService.listAll()) {
            serviceCode2ResultModelIds.put(serviceModelVO.getServiceCode(), new ArrayList<>());
        }

        for (SttDeployModelVO deployUploadVO : deployUploadVOS) {
        	log.info(">>>> deployUploadVO.getServiceModelId : "+deployUploadVO.getServiceModelId());
        	List<String> resultModelIdMap = serviceCode2ResultModelIds.get(Long.toString(deployUploadVO.getServiceModelId()));
        	log.info(">>>>> before add [deployUploadVO.getResultModelId value] : "+deployUploadVO.getResultModelId());
        	resultModelIdMap.add(deployUploadVO.getResultModelId());

            Map<String, String> tempMap = new HashMap<>();
            tempMap.put("key", deployUploadVO.getResultModelId());
            tempMap.put("value", deployUploadVO.getDescription());
            resultModelId2Descriptions.add(tempMap);
            
            Map<String, String> modelTypeMap = new HashMap<>();
            modelTypeMap.put("key", deployUploadVO.getResultModelId());
            modelTypeMap.put("value", deployUploadVO.getModelType());
            resultModelId2ModelTypes.add(modelTypeMap);
            
        }

        DeployMngModel deployMngModel = new DeployMngModel();
        deployMngModel.setResultModelId2Descriptions(resultModelId2Descriptions);
        deployMngModel.setServiceCode2ResultModelIds(serviceCode2ResultModelIds);
        deployMngModel.setResultModelId2ModelTypes(resultModelId2ModelTypes);

        return deployMngModel;
    }

    /**
     * Gets model path.
     *
     * @param resultModelId the result model id
     * @return the model path
     */
    public String getModelPath(String resultModelId) {
        return sttDeployModelRepository.getModelPath(resultModelId);
    }

    /**
     * Detail by result model id stt deploy model vo.
     *
     * @param resultModelId the result model id
     * @return the stt deploy model vo
     */
    public SttDeployModelVO detailByResultModelId(String resultModelId) {
        return sttDeployModelRepository.detailByResultModelId(resultModelId);
    }

    /**
     * Insert int.
     *
     * @param sttDeployModelVO the stt upload vo
     * @return the int
     */
    @Transactional
    public int insert(SttDeployModelVO sttDeployModelVO) {
        return sttDeployModelRepository.insert(sttDeployModelVO);
    }

    /**
     * Update int.
     *
     * @param sttDeployModelVO the stt upload vo
     * @return the int
     */
    @Transactional
    public int update(SttDeployModelVO sttDeployModelVO) {
        return sttDeployModelRepository.update(sttDeployModelVO);
    }

    /**
     * Delete int.
     *
     * @param modelIdList the upload id list
     * @return the int
     */
    @Transactional
    public int delete(SttDeployModelListVO modelIdList) {
        return sttDeployModelRepository.delete(modelIdList);
    }

    private List<SttDeployModelVO> getAllDeployModelVOs() {
        SttDeployModelSearchCondition searchCondition = SttDeployModelSearchCondition.builder()
                .build();
        int listSize = count(searchCondition);
        int pageNum = 1;
        List<SttDeployModelVO> resultList = new ArrayList<>();

        while (listSize > 0) {
            PageHelper.startPage(pageNum, MAX_PAGE_SIZE, "REG_DT DESC");
            Page<SttDeployModelVO> sttDeployModelVOs = list(searchCondition);

            for (SttDeployModelVO sttDeployModelVO : sttDeployModelVOs) {
                resultList.add(sttDeployModelVO);
            }

            pageNum += 1;
            listSize -= MAX_PAGE_SIZE;
        }

        return resultList;
    }

    /**
     * Gets excel map.
     *
     * @param searchCondition the search condition
     * @return the excel map
     */
    public Map<String, Object> getExcelMap(SttDeployModelSearchCondition searchCondition) {
        long listSize = sttDeployModelRepository.count(searchCondition);
        List<String> keys = Arrays.asList("NO"
                , "SERVICE_MODEL"
                , "DESCRIPTION"
                , "UPLOADED_BY"
                , "UPLOADED_AT"
                , "DATA_NUM"
                , "RESULT_MODEL_ID");
        List<String> headers = Arrays.asList("No."
                , "서비스 모델"
                , "설명"
                , "작성자"
                , "등록일시"
                , "학습데이터 수"
                , "결과모델 ID");

        return SxssfExcelBuilder.makeExcelDataMap(listSize
                , searchCondition
                , keys
                , headers
                , null
                , null);
    }
}
