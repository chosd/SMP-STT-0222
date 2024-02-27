package com.kt.smp.stt.comm.directory.controller;

import com.dev.dto.HttpResponse;
import com.dev.func.dataconverter.JsonUtil;
import com.dev.type.ResponseStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.stt.comm.directory.dto.*;
import com.kt.smp.stt.comm.directory.service.DirectoryGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/api/directory")
public class DirectoryGroupApiController {

    private final DirectoryGroupService groupService;

    @SmpServiceApi(
            name = "디렉토리 그룹 목록 조회",
            method = RequestMethod.GET,
            path = "/group",
            type = "조회",
            description = "디렉토리 그룹 목록 조회")
    public String search(@ModelAttribute DirectoryGroupSearchCondition searchCondition) throws JsonProcessingException {
        try {
        	log.info(">>>>> searchCondition.getGubunType() : "+searchCondition.getGubunType());
            searchCondition.determineConditionByType();
            int count = groupService.count(searchCondition);
            return JsonUtil.toJson(HttpResponse.onSuccess(count, 0 < count ? groupService.search(searchCondition) : null));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리 그룹 조회",
            method = RequestMethod.GET,
            path = "/group/{id}",
            type = "조회",
            description = "디렉티로 그룹 조회")
    public String get(@PathVariable("id") int id) throws JsonProcessingException {

        try {

            DirectoryGroupDto group = groupService.get(id);
            return JsonUtil.toJson(HttpResponse.onSuccess(group));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리 그룹명 중복체크",
            method = RequestMethod.GET,
            path = "/group/name/duplicate",
            type = "조회",
            description = "디렉토리 그룹명 중복체크")
    public String duplicateNameCheck(@RequestParam("encodedName") String encodedName) throws JsonProcessingException {

        try {

            String name = URLDecoder.decode(encodedName, StandardCharsets.UTF_8);
            if (groupService.isExistName(name)) {
                return JsonUtil.toJson(HttpResponse.onSuccess(ResponseStatus.CONFLICT));
            }

            return JsonUtil.toJson(HttpResponse.onSuccess(ResponseStatus.ACCEPTED));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }
    
    @SmpServiceApi(name = "디렉토리 경로 중복체크",method = RequestMethod.GET,path = "/group/path/duplicate",type = "조회",description = "디렉토리 경로 중복체크")
    public String duplicateDefaultPathCheck(@RequestParam("path") String path) throws JsonProcessingException {

        try {

            if (groupService.isExistDefaultPath(path)) {
            	
                return JsonUtil.toJson(HttpResponse.onSuccess(ResponseStatus.CONFLICT));
            }

            return JsonUtil.toJson(HttpResponse.onSuccess(ResponseStatus.ACCEPTED));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리 그룹 등록",
            method = RequestMethod.POST,
            path = "/group",
            type = "등록",
            description = "디렉토리 그룹 등록")
    public String save(
            HttpServletRequest request,
            @RequestBody DirectoryGroupSaveDto newGroup) throws JsonProcessingException {

        try {
            newGroup.audit(request);
            groupService.save(newGroup);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리 그룹 수정",
            method = RequestMethod.POST,
            path = "/group/{groupId}/update",
            type = "등록",
            description = "디렉토리 그룹 수정")
    public String update(
            HttpServletRequest request,
            @PathVariable("groupId") int groupId,
            @RequestBody DirectoryGroupUpdateDto modifiedGroup) throws JsonProcessingException {

        try {
            modifiedGroup.audit(request);
            groupService.update(groupId, modifiedGroup);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리 그룹 삭제",
            method = RequestMethod.POST,
            path = "/group/delete",
            type = "삭제",
            description = "디렉토리 그룹 삭제")
    public String deleteList(@RequestBody DirectoryGroupDeleteDto target) throws JsonProcessingException {

        try {
            groupService.delete(target);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }
}
