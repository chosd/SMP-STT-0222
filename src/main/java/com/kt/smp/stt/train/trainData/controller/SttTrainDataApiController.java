package com.kt.smp.stt.train.trainData.controller;

import static com.kt.smp.stt.common.component.SttCmsResultStatus.*;

import com.dev.dto.HttpResponse;
import com.dev.func.dataconverter.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.common.exception.SmpException;
import com.kt.smp.common.exception.SttException;
import com.kt.smp.common.util.crypto.TextCrypto;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.common.component.SttCmsResultStatus;
import com.kt.smp.stt.train.trainData.cache.SttCacheManager;
import com.kt.smp.stt.train.trainData.cache.SttTrainDataBulkCache;
import com.kt.smp.stt.train.trainData.domain.SttTrainAmDataVO;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataAmSearchCondition;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataListVO;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataSearchCondition;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataVO;
import com.kt.smp.stt.train.trainData.dto.*;
import com.kt.smp.stt.train.trainData.service.SttTrainDataService;
import com.kt.smp.stt.verify.data.dto.VerifyDataMultipartSaveDto;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("${smp.service.uri.prefix}/trainData/api")
@RequiredArgsConstructor
public class SttTrainDataApiController {

  private final SttTrainDataService sttTrainDataService;

  private final SttTrainDataService trainDataService;
  private final ServiceModelService serviceModelService;
  
  /**
   * List ajax model and view.
   *
   * @param pageNum         the page num
   * @param pageSize        the page size
   * @param orderBy         the order by
   * @param searchCondition the search condition
   * @return the model and view
   */
  @SmpServiceApi(
    name = "LM 학습데이터 검색",
    method = RequestMethod.GET,
    path = "/listPage",
    type = "검색",
    description = "LM 학습데이터 검색"
  )
  @ResponseBody
  public ResponseEntity<SttTrainDataSearchResponseDto> listPage(
    @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
    @RequestParam(
      value = "orderBy",
      defaultValue = "REG_DT DESC"
    ) String orderBy,
    @ModelAttribute(
      "searchCondition"
    ) SttTrainDataSearchCondition searchCondition
  ) {
    PageHelper.startPage(pageNum, pageSize, orderBy);
    Page<SttTrainDataVO> page = trainDataService.listPage(searchCondition);

    SttTrainDataSearchResponseDto searchResponseDto = new SttTrainDataSearchResponseDto(
      page
    );

    return ResponseEntity.ok(searchResponseDto);
  }

  @SmpServiceApi(
    name = "AM 학습데이터 검색",
    method = RequestMethod.GET,
    path = "/amListPage",
    type = "검색",
    description = "AM 학습데이터 검색"
  )
  @ResponseBody
  public ResponseEntity<SttTrainDataAmSearchResponseDto> amDataSearch(
    @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
    @RequestParam(
      value = "orderBy",
      defaultValue = "REG_DT DESC"
    ) String orderBy,
    @ModelAttribute(
      "searchCondition"
    ) SttTrainDataAmSearchCondition searchCondition
  ) {
    PageHelper.startPage(pageNum, pageSize, orderBy);
    Page<SttTrainAmDataVO> page = trainDataService.amDataSearch(
      searchCondition
    );

    SttTrainDataAmSearchResponseDto searchResponseDto = new SttTrainDataAmSearchResponseDto(
      page
    );

    return ResponseEntity.ok(searchResponseDto);
  }

  @SmpServiceApi(
    name = "AM 학습데이터 상세",
    method = RequestMethod.GET,
    path = "/amDetail/{amDataId}",
    type = "상세",
    description = "AM 학습데이터 상세"
  )
  public ResponseEntity<BaseResponseDto<Object>> amDetail(
    @PathVariable(name = "amDataId") Long amDataId
  ) {
    BaseResponseDto<Object> responseDto = new BaseResponseDto<>(
      SttCmsResultStatus.SUCCESS
    );

    try {
      SttTrainAmDataVO detail = trainDataService.amDetail(amDataId);
      responseDto.setResult(detail);
    } catch (SttException e) {
      log.error("[ERROR] detail : {}", e.getMessage());
      responseDto.setResultCode(e.getStatus().getResultCode());
    } catch (Exception e) {
      log.error("[ERROR] detail : {}", e.getMessage());
      responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
    }

    return makeResponse(responseDto, "amDetail");
  }

  @SmpServiceApi(
    name = "LM 학습데이터 상세",
    method = RequestMethod.GET,
    path = "/detail/{trainDataId}",
    type = "상세",
    description = "LM 학습데이터 상세"
  )
  public ResponseEntity<BaseResponseDto<Object>> detail(
    @PathVariable(name = "trainDataId") Long trainDataId
  ) {
    BaseResponseDto<Object> responseDto = new BaseResponseDto<>(
      SttCmsResultStatus.SUCCESS
    );

    try {
      SttTrainDataVO detail = trainDataService.detail(trainDataId);
      responseDto.setResult(detail);
    } catch (SttException e) {
      log.error("[ERROR] detail : {}", e.getMessage());
      responseDto.setResultCode(e.getStatus().getResultCode());
    } catch (Exception e) {
      log.error("[ERROR] detail : {}", e.getMessage());
      responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
    }

    return makeResponse(responseDto, "detail");
  }

  @SmpServiceApi(name = "AM 학습데이터 등록",method = RequestMethod.POST,path = "/amDataInsert",type = "등록",description = "AM 학습데이터 등록")
  public String amDataSave(HttpServletRequest request,@ModelAttribute SttTrainDataMultipartSaveDto newData) throws JsonProcessingException {
	  try {
	      newData.audit(request);
        trainDataService.amDataInsert(newData, request);
        return JsonUtil.toJson(HttpResponse.onSuccess(true));
	  
	  } catch (Throwable e) {
	      e.printStackTrace();
        return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
	  }
  }


  /**
   *@MethodName : amDataDirectSave
   *@작성일 : 2023. 01. 17.
   *@작성자 : chanmi.joo
   *@변경이력 :
   *@Method설명 : AM 학습데이터 초기 데이터 직접 등록 (각 모델별 등록된 학습 데이터가 없을 경우 활성화)
   * @param request
   * @param newData
   * @return
   */
  @SmpServiceApi(name = "AM 학습데이터 초기 데이터 직접 등록",method = RequestMethod.POST,path = "/amDataDirectInsert",type = "등록",description = "AM 학습데이터 초기 데이터 직접 등록")
  public ResponseEntity<BaseResponseDto<Object>> amDataDirectSave(HttpServletRequest request,@RequestBody SttTrainDataMultipartSaveDto newData) {
    BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
    try {
      newData.audit(request);
      if (trainDataService.hasDirectAnswerFile(newData) && trainDataService.hasDirectVoiceFile(newData)){
        if(trainDataService.hasServiceModelDirectPath(newData.getServiceModelId().toString())){
          // 데이터 중복 코드 반환, 이미 해당 서비스 모델에 초기 데이터가 업로드 됨
          responseDto.setResultCode(DATA_DUPLICATED.getResultCode());
        } else {
          // 초기 대량 데이터 직접 업로드
          trainDataService.amDataDirectInsert(newData, request);
        }
      }
    } catch (Exception e) {
      log.error("[ERROR] amDataDirectInsert : {}", e.getMessage());
      responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
      responseDto.setResultMsg(INTERNAL_SERVER_ERROR.getResultMessage());
    }

    return makeResponse(responseDto, "amDataDirectInsert");
  }

  /**
   * Insert ajax model and view.
   *
   * @param request        the request
   * @param sttTrainDataVO the stt train data vo
   * @return the model and view
   */
  @SmpServiceApi(name = "LM 학습데이터 등록",method = RequestMethod.POST,path = "/insert",type = "목록",description = "LM 학습데이터 등록")
  @ResponseBody
  public ResponseEntity<BaseResponseDto<Object>> insert(HttpServletRequest request,@RequestBody SttTrainDataVO sttTrainDataVO) {
	  MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
	  BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
	  
	 
	  String contents = sttTrainDataVO.getContents().trim().replaceAll("\\s+", " ");;
	  
	  sttTrainDataVO.setContents(contents);
	  sttTrainDataVO.setRegId(header.getUserId());
	  sttTrainDataVO.setRegIp(request.getRemoteAddr());

    try {
    	
      trainDataService.insert(sttTrainDataVO);
      
    } catch (SttException e) {
      log.error("[ERROR] insert : ", e);
      responseDto.setResultCode(e.getStatus().getResultCode());
    } catch (Exception e) {
      log.error("[ERROR] insert : ", e);
      responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
    }

    return makeResponse(responseDto, "insert");
  }

  @SmpServiceApi(
    name = "AM 학습데이터 수정",
    method = RequestMethod.POST,
    path = "/amUpdate",
    type = "목록",
    description = "AM 학습데이터 수정"
  )
  @ResponseBody
  public ResponseEntity<BaseResponseDto<Object>> amUpdate(
    HttpServletRequest request,
    @RequestBody SttTrainAmDataVO sttTrainAmDataVO
  ) {
    BaseResponseDto<Object> responseDto = new BaseResponseDto<>(
      SttCmsResultStatus.SUCCESS
    );
    //sttTrainAmDataVO.setDatasetName(sttTrainAmDataVO.getDatasetName().trim().replaceAll("\\s+", " "));
    sttTrainAmDataVO.setRegId("admin");
    sttTrainAmDataVO.setRegIp(request.getRemoteAddr());
    sttTrainAmDataVO.setUpdId(request.getRemoteUser());

    try {
      trainDataService.amUpdate(sttTrainAmDataVO);
    } catch (SttException e) {
      log.error("[ERROR] update : {}", e.getMessage());
      responseDto.setResultCode(e.getStatus().getResultCode());
    } catch (Exception e) {
      log.error("[ERROR] update : {}", e.getMessage());
      responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
    }
    return makeResponse(responseDto, "update");
  }

  /**
   * Update ajax model and view.
   *
   * @param request        the request
   * @param sttTrainDataVO the stt train data vo
   * @return the model and view
   */
  @SmpServiceApi(
    name = "LM 학습데이터 수정",
    method = RequestMethod.POST,
    path = "/update",
    type = "목록",
    description = "LM 학습데이터 수정"
  )
  @ResponseBody
  public ResponseEntity<BaseResponseDto<Object>> update(
    HttpServletRequest request,
    @RequestBody SttTrainDataVO sttTrainDataVO
  ) {
    BaseResponseDto<Object> responseDto = new BaseResponseDto<>(
      SttCmsResultStatus.SUCCESS
    );
    sttTrainDataVO.setContents(
      sttTrainDataVO.getContents().trim().replaceAll("\\s+", " ")
    );
    sttTrainDataVO.setRegId("admin");
    sttTrainDataVO.setRegIp(request.getRemoteAddr());
    sttTrainDataVO.setUpdId(request.getRemoteUser());

    try {
      trainDataService.update(sttTrainDataVO);
    } catch (SttException e) {
      log.error("[ERROR] update : {}", e.getMessage());
      responseDto.setResultCode(e.getStatus().getResultCode());
    } catch (Exception e) {
      log.error("[ERROR] update : {}", e.getMessage());
      responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
    }

    return makeResponse(responseDto, "update");
  }

  /**
   * Delete ajax model and view.
   *
   * @param request         the request
   * @param trainDataIdList the train data id list
   * @return the model and view
   */
  @SmpServiceApi(
    name = "LM 학습데이터 삭제",
    method = RequestMethod.POST,
    path = "/delete",
    type = "목록",
    description = "LM 학습데이터 삭제"
  )
  @ResponseBody
  public ResponseEntity<BaseResponseDto<Object>> delete(
    HttpServletRequest request,
    @RequestBody SttTrainDataListVO trainDataIdList
  ) {
    BaseResponseDto<Object> responseDto = new BaseResponseDto<>(
      SttCmsResultStatus.SUCCESS
    );

    try {
      trainDataService.delete(trainDataIdList);
    } catch (SttException e) {
      log.error("[ERROR] delete : {}", e.getMessage());
      responseDto.setResultCode(e.getStatus().getResultCode());
    } catch (Exception e) {
      log.error("[ERROR] delete : {}", e.getMessage());
      responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
    }

    return makeResponse(responseDto, "delete");
  }

  @SmpServiceApi(
    name = "AM 학습데이터 삭제",
    method = RequestMethod.POST,
    path = "/amDelete",
    type = "목록",
    description = "AM 학습데이터 삭제"
  )
  @ResponseBody
  public ResponseEntity<BaseResponseDto<Object>> amDelete(
    HttpServletRequest request,
    @RequestBody SttTrainDataListVO trainDataIdList
  ) {
    BaseResponseDto<Object> responseDto = new BaseResponseDto<>(
      SttCmsResultStatus.SUCCESS
    );

    try {
      trainDataService.amDelete(trainDataIdList);
    } catch (SttException e) {
      log.error("[ERROR] delete : {}", e.getMessage());
      responseDto.setResultCode(e.getStatus().getResultCode());
    } catch (Exception e) {
      log.error("[ERROR] delete : {}", e.getMessage());
      responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
    }

    return makeResponse(responseDto, "delete");
  }

  /**
   * Delete ajax model and view.
   *
   * @param request        the request
   * @param sttTrainDataVO the stt train data vo
   * @return the model and view
   */
  @SmpServiceApi(
    name = "LM 학습데이터 일괄 삭제",
    method = RequestMethod.POST,
    path = "/deleteAll",
    type = "목록",
    description = "LM 학습데이터 일괄 삭제"
  )
  @ResponseBody
  public ResponseEntity<BaseResponseDto<Object>> deleteAll(
    HttpServletRequest request,
    @RequestBody SttTrainDataVO sttTrainDataVO
  ) {
    BaseResponseDto<Object> responseDto = new BaseResponseDto<>(
      SttCmsResultStatus.SUCCESS
    );
    SttTrainDataListVO trainDataIdList = trainDataService.getListByServiceModel(
      SttTrainDataSearchCondition
        .builder()
        .serviceModelId(sttTrainDataVO.getServiceModelId() + "")
        .build()
    );

    if (trainDataIdList.getTrainDataIdList().size() == 0) {
      return makeResponse(responseDto, "deleteAll");
    }

    try {
      trainDataService.delete(trainDataIdList);
    } catch (SttException e) {
      log.error("[ERROR] delete : {}", e.getMessage());
      responseDto.setResultCode(e.getStatus().getResultCode());
    } catch (Exception e) {
      log.error("[ERROR] delete : {}", e.getMessage());
      responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
    }

    return makeResponse(responseDto, "deleteAll");
  }

  /**
   *@MethodName : amDeleteAll
   *@작성일 : 2023. 10. 18.
   *@작성자 : munho.jang
   *@변경이력 :
   *@Method설명 : AM학습데이터 신규화면 기능추가, SttTrainDataListVO 클래스의 신규생성은 고민을 해봐야 할듯
   * @param request
   * @param sttTrainAmDataVO
   * @return
   */
  @SmpServiceApi(
    name = "AM 학습데이터 일괄 삭제",
    method = RequestMethod.POST,
    path = "/amDeleteAll",
    type = "목록",
    description = "AM 학습데이터 일괄 삭제"
  )
  @ResponseBody
  public ResponseEntity<BaseResponseDto<Object>> amDeleteAll(
    HttpServletRequest request,
    @RequestBody SttTrainAmDataVO sttTrainAmDataVO
  ) {
    BaseResponseDto<Object> responseDto = new BaseResponseDto<>(
      SttCmsResultStatus.SUCCESS
    );
    SttTrainDataListVO trainDataIdList = trainDataService.getAmListByServiceModel(
      SttTrainDataAmSearchCondition
        .builder()
        .serviceModelId(sttTrainAmDataVO.getServiceModelId())
        .build()
    );

    if (trainDataIdList.getTrainDataIdList().size() == 0) {
      return makeResponse(responseDto, "deleteAll");
    }

    try {
      trainDataService.amDelete(trainDataIdList);
    } catch (SttException e) {
      log.error("[ERROR] delete : {}", e.getMessage());
      responseDto.setResultCode(e.getStatus().getResultCode());
    } catch (Exception e) {
      log.error("[ERROR] delete : {}", e.getMessage());
      responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
    }

    return makeResponse(responseDto, "deleteAll");
  }

  @SmpServiceApi(
    name = "학습데이터 중복체크",
    method = RequestMethod.POST,
    path = "/existContents",
    type = "중복체크",
    description = "학습데이터 중복 체크"
  )
  public ResponseEntity<BaseResponseDto<Object>> existContents(
    @RequestParam("contents") String contents,
    @RequestParam("serviceModelId") String serviceModelId
  ) {
    BaseResponseDto<Object> responseDto = new BaseResponseDto<>(
      SttCmsResultStatus.SUCCESS
    );
    boolean hasDuplicatedContents = sttTrainDataService.hasDuplicateContents(
      contents,
      serviceModelId
    );

    if (hasDuplicatedContents) {
      responseDto.setResultCode(DATA_DUPLICATED.getResultCode());
    }

    return makeResponse(responseDto, "speakerCodeCheck");
  }

  @SmpServiceApi(
    name = "정답지 데이터셋명 중복체크",
    method = RequestMethod.POST,
    path = "/existAmData",
    type = "중복체크",
    description = "정답지 데이터셋명 중복 체크"
  )
  public ResponseEntity<BaseResponseDto<Object>> existAmDataCheck(
    @RequestParam("datasetName") String datasetName,
    @RequestParam("serviceModelId") String serviceModelId
  ) {
    BaseResponseDto<Object> responseDto = new BaseResponseDto<>(
      SttCmsResultStatus.SUCCESS
    );
    boolean hasDuplicatedContents = sttTrainDataService.hasDuplicateDatasetName(
      datasetName,
      serviceModelId
    );

    if (hasDuplicatedContents) {
      responseDto.setResultCode(DATA_DUPLICATED.getResultCode());
    }

    return makeResponse(responseDto, "speakerCodeCheck");
  }
  
  @SmpServiceApi(name = "하위 디렉토리명 중복체크",method = RequestMethod.POST,path = "/existDirPath",type = "중복체크",description = "하위 디렉토리명 중복 체크")
  public ResponseEntity<BaseResponseDto<Object>> existDirectoryPath(@RequestParam("detailPath") String detailPath, @RequestParam("serviceModelId") String serviceModelId) {
	  
	  BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
	  boolean hasDuplicatedPath = sttTrainDataService.hasDuplicateDirectoryPath(detailPath,serviceModelId);

	  if (hasDuplicatedPath) {
		  responseDto.setResultCode(DATA_DUPLICATED.getResultCode());
	  }
	
	  return makeResponse(responseDto, "speakerCodeCheck");
  }

  @SmpServiceApi(
    name = "LM 학습데이터 대량등록",
    method = RequestMethod.POST,
    path = "/insertBulk",
    type = "대량등록",
    description = "LM 학습데이터 대량등록",
    consumes = {
      MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE,
    }
  )
  public ResponseEntity<BaseResponseDto<Object>> insertBulk(
    HttpServletRequest request,
    HttpServletResponse response,
    @RequestPart("excelFile") MultipartFile excelFile
  ) throws SmpException {
    BaseResponseDto<Object> responseDto = new BaseResponseDto<>(
      SttCmsResultStatus.SUCCESS
    );
    Map<String, String> resultObj = new HashMap<>();
    MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(
      request
    );

    // 새로운 요청 키 생성
    String uploadKey = UUID.randomUUID().toString();

    // 업로드한 파일 캐시에 저장
    SttTrainDataBulkCache cache = trainDataService.saveBulkInsertFilesToCache(
      uploadKey,
      excelFile
    );

    if (
      cache.isFinished() &&
      cache.getResultCode() != SttTrainDataBulkCache.ResultCode.SUCCESS
    ) {
      responseDto.setResultCode(BAD_REQUEST.getResultCode());
      responseDto.setResultMsg(cache.getResultCode().getMessage()); // 대량등록은 별도 메시지로 응답
    } else {
      // Async
      trainDataService.insertBulk(
        uploadKey,
        header.getUserId(),
        request.getRemoteAddr()
      ); // 두번째 인자값 admin > header.getUserId() 변경

      responseDto.setResultCode(SUCCESS.getResultCode());
      responseDto.setResultMsg(
        getMessage("insertBulk", responseDto.getResultCode())
      );

      resultObj.put("key", uploadKey);
      responseDto.setResult(resultObj);
    }
    return ResponseEntity.ok(responseDto);
  }

  /**
   * @title 대량 등록
   * @author jieun.chang
   * @see <pre></pre>
   * @since 2022.05.02
   */
  @SmpServiceApi(
    name = "LM 학습데이터 대량등록 Progress 조회",
    method = RequestMethod.GET,
    path = "/insertBulk/{key}/progress",
    type = "대량등록",
    description = "LM 학습데이터 대량등록 Progress 조회"
  )
  public ResponseEntity<BaseResponseDto<SttTrainDataBulkProgressResDto>> getInsertBulkProgress(
    @PathVariable("key") String key
  ) {
    BaseResponseDto<SttTrainDataBulkProgressResDto> responseDto = new BaseResponseDto<>(
      SttCmsResultStatus.SUCCESS
    );

    // 세션에서 uploadKey 값 찾기
    SttTrainDataBulkCache cache = SttCacheManager
      .getInstance()
      .getTrainDataCache(key);

    if (ObjectUtils.isEmpty(cache)) {
      responseDto.setResultCode(NO_PROCESS_IN_PROGRESS.getResultCode());
    } else if (cache.isFinished()) {
      // 완료된 걸 조회할 때는 캐시 날리기
      SttTrainDataBulkCache.ResultCode resultCode = cache.getResultCode();
      List<SttTrainDataBulkSaveReqDto> failList = cache.getFailList();
      int totalCount = cache.getTotalCount();
      int successCount = cache.getSuccessList().size();

      SttCacheManager.getInstance().removeTrainDataCache(key);

      SttTrainDataBulkProgressResDto progressResDto = SttTrainDataBulkProgressResDto
        .builder()
        .resultCode(resultCode)
        .failList(failList)
        .totalCount(totalCount)
        .successCount(successCount)
        .build();

      responseDto.setResult(progressResDto);
    } else if (!cache.isFinished()) {
      SttTrainDataBulkProgressResDto progressResDto = SttTrainDataBulkProgressResDto
        .builder()
        .currCount(cache.getCurrCount())
        .totalCount(cache.getTotalCount())
        .successCount(cache.getSuccessList().size())
        .build();

      responseDto.setResultCode(IN_PROGRESS.getResultCode());
      responseDto.setResult(progressResDto);
    }

    responseDto.setResultMsg(
      getMessage("bulkProgress", responseDto.getResultCode())
    );

    return ResponseEntity.ok(responseDto);
  }

  @SmpServiceApi(
    name = "서비스모델 목록 조회",
    method = RequestMethod.GET,
    path = "/serviceModelList",
    type = "조회",
    description = "서비스모델 목록 조회"
  )
  public ResponseEntity<BaseResponseDto<Object>> getServiceModelList() {
    BaseResponseDto<Object> responseDto = new BaseResponseDto<>(
      SttCmsResultStatus.SUCCESS
    );

    try {
      List<ServiceModelVO> list = serviceModelService.listAll();
      responseDto.setResult(list);
    } catch (SttException e) {
      log.error("[ERROR] service model list : {}", e.getMessage());
      responseDto.setResultCode(e.getStatus().getResultCode());
    } catch (Exception e) {
      log.error("[ERROR] service model list : {}", e.getMessage());
      responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
    }

    return makeResponse(responseDto, "getServiceModelList");
  }

  /**
   *@MethodName : getServiceModelResultList
   *@작성일 : 2023. 01. 19.
   *@작성자 : chanmi.joo
   *@변경이력 :
   *@Method설명 : 서비스모델별 AM학습 데이터 갯수를 반환
   * @param
   * @return responseDto
   */
  @SmpServiceApi(
    name = "서비스모델별 결과수 조회",
    method = RequestMethod.GET,
    path = "/serviceModelResultList",
    type = "조회",
    description = "서비스모델별 결과수 조회"
  )
  public ResponseEntity<BaseResponseDto<Object>> getServiceModelResultList() {
    BaseResponseDto<Object> responseDto = new BaseResponseDto<>(
      SttCmsResultStatus.SUCCESS
    );

    try {
      int serviceListNum = serviceModelService.listAll().size(); // 서비스 모델 수
      ArrayList<Integer> list = trainDataService.serviceModelResultAll(serviceListNum);
      responseDto.setResult(list.toArray(new Integer[list.size()]));
    } catch (SttException e) {
      log.error("[ERROR] service model Result list : {}", e.getMessage());
      responseDto.setResultCode(e.getStatus().getResultCode());
    } catch (Exception e) {
      log.error("[ERROR] service model Result list : {}", e.getMessage());
      responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
    }

    return makeResponse(responseDto, "getServiceModelResultList");
  }
  
  @SmpServiceApi(name = "데이터셋 다운로드",method = RequestMethod.GET,path = "/amDataset/{id}/download",type = "다운로드",description = "데이터셋 다운로드")
  public void datasetDownload(@PathVariable("id") Integer id, HttpServletResponse response) {

      Path zipFilePath = null;
      try {

    	  SttTrainAmDataVO detail = trainDataService.amDetail(Long.valueOf(id));
          zipFilePath = trainDataService.getAnswerSheetAndWavFile(detail);
          log.info(">>> zipFilePath : "+zipFilePath);
          try(FileInputStream inputStream = new FileInputStream(zipFilePath.toFile())) {
              setResponseHeader(response, detail.getDatasetName());
              ServletOutputStream outputStream = response.getOutputStream();
              StreamUtils.copy(inputStream, outputStream);
          }

      } catch (IOException ex) {
          ex.printStackTrace();
          log.error("[ERROR] download am dataset fail : {}", ex.getMessage());
      } catch (InvalidPathException e) {
      	//windows 환경에서 실행할 경우 파일명 에러
      	log.error(e.getMessage() + ">>>>>> [windows] 파일명에 ':'를 포함할 수 없습니다.");
      } finally {
          deleteZipFile(zipFilePath);
      }
  }
  
  private void deleteZipFile(Path zipFilePath) {

      try {
          if (zipFilePath != null && zipFilePath.toFile().exists()) {
              Files.delete(zipFilePath);
          }
      } catch (IOException e) {
      	log.error(e.getMessage());
      }

  }
  
  private void setResponseHeader(HttpServletResponse response, String datasetName) throws UnsupportedEncodingException {
      String filename = URLEncoder.encode(datasetName+".zip", StandardCharsets.UTF_8);
      response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\";");
      response.setContentType("application/zip");
  }

  private ResponseEntity<BaseResponseDto<Object>> makeResponse(BaseResponseDto<Object> responseDto,String function) {
    responseDto.setResultMsg(getMessage(function, responseDto.getResultCode()));

    return ResponseEntity.ok(responseDto);
  }

  private String getMessage(String function, String resultCode) {
    String msg;
    String key = "";
    StringBuffer sb = new StringBuffer();
    // 성공 응답이 아닌 경우, ${function}.fail (있으면) 메시지 먼저 출력
    if (!resultCode.startsWith("0")) {
      key = "stt traindata" + "." + function + ".fail";
      //msg = messageProperties.getMessage(key);
      msg = "실패";
    } else {
    	//key = MESSAGE_KEY + "." + function + "." + resultCode;
        key = "stt traindata" + "." + function + "." + resultCode;
        msg = "성공";
        //msg += "성공";
    }
    
    return msg;
  }
}
