package com.kt.smp.stt.comm.directory.controller;

import com.dev.dto.HttpResponse;
import com.dev.func.dataconverter.JsonUtil;
import com.dev.type.ResponseStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.comm.directory.dto.*;
import com.kt.smp.stt.comm.directory.service.DirectoryGroupService;
import com.kt.smp.stt.comm.directory.service.DirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/api/directory")
public class DirectoryApiController {

    private final DirectoryGroupService groupService;
    private final DirectoryService directoryService;
    private final ConfigService configService;

    @SmpServiceApi(
            name = "디렉토리 목록 조회",
            method = RequestMethod.GET,
            path = "/group/{groupId}/dir",
            type = "조회",
            description = "디렉토리 목록 조회")
    public String getList(
            @PathVariable("groupId") int groupId) throws JsonProcessingException {

        try {

            DirectoryGroupDto group = groupService.get(groupId);
            if (group == null) {
                throw new IllegalArgumentException("등록되지 않은 그룹입니다");
            }

            List<DirectoryListDto> directoryList = directoryService.getList(groupId);
            return JsonUtil.toJson(HttpResponse.onSuccess(directoryList));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리 조회",
            method = RequestMethod.GET,
            path = "/group/dir/{id}",
            type = "조회",
            description = "디렉토리 조회")
    public String get(
            @PathVariable("id") int id) throws JsonProcessingException {

        try {

            DirectoryDto directory = directoryService.get(id);
            if (directory == null) {
                throw new IllegalArgumentException("등록되지 않은 그룹입니다");
            }

            return JsonUtil.toJson(HttpResponse.onSuccess(directory));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리명 중복체크",
            method = RequestMethod.GET,
            path = "/group/{groupId}/dir/name/duplicate",
            type = "조회",
            description = "디렉토리명 중복체크")
    public String duplicateNameCheck(
            @PathVariable("groupId") int groupId,
            @RequestParam("encodedName") String encodedName) throws JsonProcessingException {

        try {

            DirectoryGroupDto group = groupService.get(groupId);
            if (group == null) {
                throw new IllegalArgumentException("등록되지 않은 그룹입니다");
            }

            String name = URLDecoder.decode(encodedName, StandardCharsets.UTF_8);
            if (directoryService.isExistName(group, name)) {
                return JsonUtil.toJson(HttpResponse.onSuccess(com.dev.type.ResponseStatus.CONFLICT));
            }

            return JsonUtil.toJson(HttpResponse.onSuccess(ResponseStatus.ACCEPTED));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리명 중복체크",
            method = RequestMethod.GET,
            path = "/group/{groupId}/dir/{directoryId}/name/duplicate",
            type = "조회",
            description = "디렉토리명 중복체크")
    public String duplicateNameCheck(
            @PathVariable("groupId") int groupId,
            @PathVariable("directoryId") int directoryId,
            @RequestParam("encodedName") String encodedName) throws JsonProcessingException {

        try {

            DirectoryGroupDto group = groupService.get(groupId);
            if (group == null) {
                throw new IllegalArgumentException("등록되지 않은 그룹입니다");
            }

            DirectoryDto directory = directoryService.get(directoryId);
            if (directory == null) {
                throw new IllegalArgumentException("등록되지 않은 경로입니다");
            }

            String name = URLDecoder.decode(encodedName, StandardCharsets.UTF_8);
            if (directoryService.isExistName(group, directory, name)) {
                return JsonUtil.toJson(HttpResponse.onSuccess(com.dev.type.ResponseStatus.CONFLICT));
            }

            return JsonUtil.toJson(HttpResponse.onSuccess(ResponseStatus.ACCEPTED));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리 경로 중복체크",
            method = RequestMethod.GET,
            path = "/group/{groupId}/dir/path/duplicate",
            type = "조회",
            description = "디렉토리 경로 중복체크")
    public String duplicatePathCheck(
            @PathVariable("groupId") int groupId,
            @RequestParam("path") String path) throws JsonProcessingException {

        try {

            DirectoryGroupDto group = groupService.get(groupId);
            if (group == null) {
                throw new IllegalArgumentException("등록되지 않은 그룹입니다");
            }

            if (directoryService.isExistPath(path)) {
                return JsonUtil.toJson(HttpResponse.onSuccess(com.dev.type.ResponseStatus.CONFLICT));
            }

            return JsonUtil.toJson(HttpResponse.onSuccess(ResponseStatus.ACCEPTED));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리 경로 중복체크",
            method = RequestMethod.GET,
            path = "/group/{groupId}/dir/{directoryId}/path/duplicate",
            type = "조회",
            description = "디렉토리 경로 중복체크")
    public String duplicatePathCheck(
            @PathVariable("groupId") int groupId,
            @PathVariable("directoryId") int directoryId,
            @RequestParam("path") String path) throws JsonProcessingException {

        try {

            DirectoryGroupDto group = groupService.get(groupId);
            if (group == null) {
                throw new IllegalArgumentException("등록되지 않은 그룹입니다");
            }

            DirectoryDto directory = directoryService.get(directoryId);
            if (directory == null) {
                throw new IllegalArgumentException("등록되지 않은 디렉토리입니다");
            }

            if (directoryService.isExistPath(directory, path)) {
                return JsonUtil.toJson(HttpResponse.onSuccess(com.dev.type.ResponseStatus.CONFLICT));
            }

            return JsonUtil.toJson(HttpResponse.onSuccess(ResponseStatus.ACCEPTED));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리 등록",
            method = RequestMethod.POST,
            path = "/group/{groupId}/dir",
            type = "등록",
            description = "디렉토리 등록")
    public String save(
            HttpServletRequest request,
            @PathVariable("groupId") int groupId,
            @RequestBody DirectorySaveDto newDirectory) throws JsonProcessingException {

        try {

            DirectoryGroupDto group = groupService.get(groupId);
            if (group == null) {
                throw new IllegalArgumentException("등록되지 않은 그룹입니다");
            }

            newDirectory.audit(request);
            MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);

            ConfigDto config = configService.getByProjectCode(header.getProjectCode());
            directoryService.save(config, group, newDirectory);

            return JsonUtil.toJson(HttpResponse.onSuccess(true));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리 사용여부 변경",
            method = RequestMethod.POST,
            path = "/group/{groupId}/dir/{directoryId}/inUse",
            type = "등록",
            description = "디렉토리 등록")
    public String updateInUse(
            HttpServletRequest request,
            @PathVariable("groupId") int groupId,
            @PathVariable("directoryId") int directoryId,
            @RequestParam("inUse") String inUse) throws JsonProcessingException {

        try {

            DirectoryGroupDto group = groupService.get(groupId);
            if (group == null) {
                throw new IllegalArgumentException("등록되지 않은 그룹입니다");
            }

            DirectoryDto directory = directoryService.get(directoryId);
            if (directory == null) {
                throw new IllegalArgumentException("등록되지 않은 디렉토리입니다");
            }

            directoryService.changeInUse(directory, inUse);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리 수정",
            method = RequestMethod.POST,
            path = "/group/{groupId}/dir/{directoryId}/update",
            type = "삭제",
            description = "디렉토리 삭제")
    public String update(
            @PathVariable("groupId") int groupId,
            @PathVariable("directoryId") int directoryId,
            @RequestBody DirectoryUpdateDto modifiedDirectory) throws JsonProcessingException {

        try {

            DirectoryGroupDto group = groupService.get(groupId);
            if (group == null) {
                throw new IllegalArgumentException("등록되지 않은 그룹입니다");
            }

            DirectoryDto savedDirectory = directoryService.get(directoryId);
            if (savedDirectory == null) {
                throw new IllegalArgumentException("등록되지 않은 디렉토리입니다");
            }

            directoryService.update(savedDirectory, modifiedDirectory);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리 삭제",
            method = RequestMethod.POST,
            path = "/group/dir/delete",
            type = "삭제",
            description = "디렉토리 삭제")
    public String delete(@RequestBody DirectoryDeleteDto target) throws JsonProcessingException {

        try {

            directoryService.delete(target);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리 노출순서 올림",
            method = RequestMethod.POST,
            path = "/group/{groupId}/dir/{directoryId}/priority/up",
            type = "삭제",
            description = "디렉토리 삭제")
    public String upPriority(
            @PathVariable("groupId") int groupId,
            @PathVariable("directoryId") int directoryId) throws JsonProcessingException {

        try {

            DirectoryGroupDto group = groupService.get(groupId);
            if (group == null) {
                throw new IllegalArgumentException("등록되지 않은 그룹입니다");
            }

            DirectoryDto directory = directoryService.get(directoryId);
            if (directory == null) {
                throw new IllegalArgumentException("등록되지 않은 디렉토리입니다");
            }

            directoryService.upPriority(group, directory);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "디렉토리 노출순서 올림",
            method = RequestMethod.POST,
            path = "/group/{groupId}/dir/{directoryId}/priority/down",
            type = "삭제",
            description = "디렉토리 삭제")
    public String downPriority(
            @PathVariable("groupId") int groupId,
            @PathVariable("directoryId") int directoryId) throws JsonProcessingException {

        try {

            DirectoryGroupDto group = groupService.get(groupId);
            if (group == null) {
                throw new IllegalArgumentException("등록되지 않은 그룹입니다");
            }

            DirectoryDto directory = directoryService.get(directoryId);
            if (directory == null) {
                throw new IllegalArgumentException("등록되지 않은 디렉토리입니다");
            }

            directoryService.downPriority(group, directory);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }
}
