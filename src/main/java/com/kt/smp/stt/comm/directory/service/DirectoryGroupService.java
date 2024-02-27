package com.kt.smp.stt.comm.directory.service;

import com.kt.smp.stt.comm.directory.dto.*;
import com.kt.smp.stt.comm.directory.repository.DirectoryGroupRepository;
import com.kt.smp.stt.comm.directory.repository.DirectoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DirectoryGroupService {

    private final DirectoryGroupRepository groupRepository;
    private final DirectoryRepository directoryRepository;

    public int count(DirectoryGroupSearchCondition searchCondition) {
        return groupRepository.count(searchCondition);
    }

    public List<DirectoryGroupListDto> search(DirectoryGroupSearchCondition searchCondition) {
        return groupRepository.search(searchCondition);
    }

    public DirectoryGroupDto get(int id) {
        DirectoryGroupDto group = groupRepository.findById(id);
        if (group == null) {
            throw new IllegalArgumentException("등록되지 않은 디렉토리 그룹입니다");
        }

        List<DirectoryListDto> directoryList = directoryRepository.findByGroupId(group.getId());
        group.setDirectoryList(directoryList);
        return group;
    }

    public boolean isExistName(String name) {
        return 0 < groupRepository.countByName(name);
    }
    
    public boolean isExistDefaultPath(String path) {
        return 0 < groupRepository.countByDefaultPath(path);
    }

    @Transactional
    public void save(DirectoryGroupSaveDto newGroup) {

        if (isExistName(newGroup.getName())) {
            throw new IllegalArgumentException("이미 등록되어 있는 그룹 이름 입니다");
        }

        groupRepository.save(newGroup);
    }

    @Transactional
    public void update(int groupId, DirectoryGroupUpdateDto modifiedGroup) {
        modifiedGroup.setId(groupId);
        groupRepository.update(modifiedGroup);
    }

    @Transactional
    public void delete(DirectoryGroupDeleteDto target) {
        List<Integer> targetIdList = target.getTargetIdList();
        for (Integer id : targetIdList) {
//            if (isHavingDirectory(id)) { 그룹상세페이지 제거
//                throw new IllegalArgumentException("그룹에 속한 디렉토리가 있어 삭제할 수 없습니다");
//            }
            groupRepository.delete(id);
        }
    }

    private boolean isHavingDirectory(Integer groupId) {

        return directoryRepository.findByGroupId(groupId).size() > 0;

    }
}
