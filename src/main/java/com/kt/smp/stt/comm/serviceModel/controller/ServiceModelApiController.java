package com.kt.smp.stt.comm.serviceModel.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.common.exception.SttException;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelListVO;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelSearchCondition;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.dto.ServiceModelSearchResponseDto;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.common.component.SttCmsResultStatus;
import com.kt.smp.stt.train.trainData.dto.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.kt.smp.stt.common.component.SttCmsResultStatus.*;

@Slf4j
@RestController
@RequestMapping("${smp.service.uri.prefix}/serviceModel/api")
@RequiredArgsConstructor
public class ServiceModelApiController {

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
    @SmpServiceApi(name = "서비스모델 검색", method = RequestMethod.GET, path = "/listPage", type = "검색", description = "서비스모델 검색")
    @ResponseBody
    public ResponseEntity<ServiceModelSearchResponseDto> listPage(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "orderBy", defaultValue = "REG_DT DESC") String orderBy,
            @ModelAttribute("searchCondition") ServiceModelSearchCondition searchCondition) {
    	serviceModelService.listAll();
        PageHelper.startPage(pageNum, pageSize, orderBy);
        Page<ServiceModelVO> page = serviceModelService.listPage(searchCondition);

        ServiceModelSearchResponseDto searchResponseDto = new ServiceModelSearchResponseDto(page);

        return ResponseEntity.ok(searchResponseDto);
    }

    @SmpServiceApi(name = "서비스모델 상세", method = RequestMethod.GET, path = "/detail/{serviceModelId}", type = "상세", description = "서비스모델 상세")
    public ResponseEntity<BaseResponseDto<Object>> detail(@PathVariable(name = "serviceModelId") Long serviceModelId) {

        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);

        try {
            ServiceModelVO detail = serviceModelService.detail(serviceModelId);
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

    /**
     * Insert ajax model and view.
     *
     * @param request        the request
     * @param serviceModelVO the stt train data vo
     * @return the model and view
     */
    @SmpServiceApi(name = "서비스모델 등록", method = RequestMethod.POST, path = "/insert", type = "목록", description = "서비스모델 등록")
    @ResponseBody
    public ResponseEntity<BaseResponseDto<Object>> insert(
            HttpServletRequest request,
            @RequestBody ServiceModelVO serviceModelVO) {

        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        serviceModelVO.setRegId(header.getUserId());
        serviceModelVO.setRegIp(request.getRemoteAddr());

        try {
            serviceModelService.insert(serviceModelVO);
        } catch (SttException e) {
            log.error("[ERROR] insert : {}", e.getMessage());
            responseDto.setResultCode(e.getStatus().getResultCode());
        } catch (Exception e) {
            log.error("[ERROR] insert : {}", e.getMessage());
            responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
        }

        return makeResponse(responseDto, "insert");
    }

    /**
     * Update ajax model and view.
     *
     * @param request        the request
     * @param serviceModelVO the stt train data vo
     * @return the model and view
     */
    @SmpServiceApi(name = "서비스모델 수정", method = RequestMethod.POST, path = "/update", type = "목록", description = "서비스모델 수정")
    @ResponseBody
    public ResponseEntity<BaseResponseDto<Object>> update(
            HttpServletRequest request,
            @RequestBody ServiceModelVO serviceModelVO) {

        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        serviceModelVO.setUpdId(header.getUserId());
        serviceModelVO.setUpdIp(request.getRemoteAddr());

        try {
            serviceModelService.update(serviceModelVO);
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
     * @param serviceModelIdList the train data id list
     * @return the model and view
     */
    @SmpServiceApi(name = "서비스모델 삭제", method = RequestMethod.POST, path = "/delete", type = "목록", description = "서비스모델 삭제")
    @ResponseBody
    public ResponseEntity<BaseResponseDto<Object>> delete(
            HttpServletRequest request,
            @RequestBody ServiceModelListVO serviceModelIdList) {

        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);

        try {
            serviceModelService.delete(serviceModelIdList);
        } catch (SttException e) {
            log.error("[ERROR] delete : {}", e.getMessage());
            responseDto.setResultCode(e.getStatus().getResultCode());
        } catch (Exception e) {
            log.error("[ERROR] delete : {}", e.getMessage());
            responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
        }

        return makeResponse(responseDto, "delete");
    }

    @SmpServiceApi(name = "서비스모델명 중복체크", method = RequestMethod.POST, path = "/existServiceModelName", type = "중복체크", description = "서비스모델명 중복 체크")
    public ResponseEntity<BaseResponseDto<Object>> existServiceModelName(
            @RequestParam("serviceModelName") String serviceModelName) {

        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        boolean hasDuplicatedContents = serviceModelService.hasDuplicateServiceModelName(serviceModelName);

        if (hasDuplicatedContents) {
            responseDto.setResultCode(DATA_DUPLICATED.getResultCode());
        }

        return makeResponse(responseDto, "service model name duplicate check");
    }

    @SmpServiceApi(name = "서비스코드 중복체크", method = RequestMethod.POST, path = "/existServiceCode", type = "중복체크", description = "서비스코드 중복 체크")
    public ResponseEntity<BaseResponseDto<Object>> existServiceCode(
            @RequestParam("serviceCode") String serviceCode) {

        BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        boolean hasDuplicatedCode = serviceModelService.hasDuplicateServiceCode(serviceCode);

        if (hasDuplicatedCode) {
            responseDto.setResultCode(DATA_DUPLICATED.getResultCode());
        }

        return makeResponse(responseDto, "service model code duplicate check");
    }

    private ResponseEntity<BaseResponseDto<Object>> makeResponse(BaseResponseDto<Object> responseDto, String function) {

        responseDto.setResultMsg(getMessage(function, responseDto.getResultCode()));

        return ResponseEntity.ok(responseDto);
    }

    private String getMessage(String function, String resultCode) {
        String msg = "";
        StringBuffer sb = new StringBuffer(msg);

        if (!resultCode.startsWith("0")) {
//            msg = "실패";
            sb.append("실패");
        }

//        msg += "성공";
        sb.append("성공");

        return msg;
    }
}
