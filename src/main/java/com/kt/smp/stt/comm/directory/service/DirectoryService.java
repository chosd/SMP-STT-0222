package com.kt.smp.stt.comm.directory.service;

import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.stt.comm.directory.dto.*;
import com.kt.smp.stt.comm.directory.repository.DirectoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final HomePathResolver homePathResolver;

    public List<DirectoryListDto> getAll() {
        return directoryRepository.findAll();
    }
    public List<DirectoryListDto> getTrainList() {
        return directoryRepository.findTrainList();
    }
    public List<DirectoryListDto> getList(int groupId) {
        return directoryRepository.findByGroupId(groupId);
    }

    public DirectoryDto get(int id) {
        return directoryRepository.findById(id);
    }

    public String getAbsolutePath(Integer id) {

        DirectoryDto directory = directoryRepository.findById(id);
        return directory.getAbsolutePath().toString();
    }

    public boolean isExistName(DirectoryGroupDto group, String name) {
        return 0 < directoryRepository.countByGroupIdAndName(group.getId(), name);
    }

    public boolean isExistName(DirectoryGroupDto group, DirectoryDto exclude, String name) {

        return 0 < directoryRepository.countByGroupIdAndExcludedDirIdAndName(group.getId(), exclude.getId(), name);
    }

    public boolean isExistPath(String path) {
        return 0 < directoryRepository.countByPath(path);
    }

    public boolean isExistPath(DirectoryDto exclude, String path) {
        return 0 < directoryRepository.countByExcludedDirIdAndPath(exclude.getId(), path);
    }

    @Transactional
    public void save(ConfigDto config, DirectoryGroupDto group, DirectorySaveDto newDirectory) {

        int priority = getNewPriority(group);
        //String homePath = homePathResolver.resolve(config);
        String homePath = homePathResolver.resolveSuhyup(); // 수협에서는 뒤에 프로젝트코드값이 안붙음
        newDirectory.init(homePath, group, priority);
        createDirectory(newDirectory.getAbsolutePath());
        directoryRepository.save(newDirectory);
    }

    private int getNewPriority(DirectoryGroupDto group) {
        List<DirectoryListDto> directoryList = directoryRepository.findByGroupId(group.getId());
        return directoryList.size() + 1;
    }

    @Transactional
    public void changeInUse(DirectoryDto directory, String inUse) {
        directoryRepository.updateInUse(directory.getId(), inUse);
    }

    @Transactional
    public void delete(DirectoryDeleteDto target) {
        List<Integer> targetIdList = target.getTargetIdList();
        for (Integer id : targetIdList) {
            directoryRepository.delete(id);
        }
    }

    @Transactional
    public void update(DirectoryDto savedDirectory, DirectoryUpdateDto modifiedDirectory) {

        Path directoryPath = Paths.get(savedDirectory.getHomePath(), modifiedDirectory.getPath());
        createDirectory(directoryPath);
        directoryRepository.updateNameAndPath(
                savedDirectory.getId(),
                modifiedDirectory.getName(),
                modifiedDirectory.getPath());
    }

    @Transactional
    public void upPriority(DirectoryGroupDto group, DirectoryDto directory) {
        if (directory.getPriority() == 1) {
            return;
        }

        int newPriority = directory.getPriority() - 1;
        DirectoryDto savedDirectory = directoryRepository.findByGroupIdAndPriority(group.getId(), newPriority);

        directoryRepository.updatePriority(directory.getId(), newPriority);
        directoryRepository.updatePriority(savedDirectory.getId(), directory.getPriority());

    }

    @Transactional
    public void downPriority(DirectoryGroupDto group, DirectoryDto directory) {

        int newPriority = directory.getPriority() + 1;
        DirectoryDto savedDirectory = directoryRepository.findByGroupIdAndPriority(group.getId(), newPriority);

        if (savedDirectory == null) {
            directoryRepository.updatePriority(directory.getId(), directory.getPriority());
            return;
        }

        directoryRepository.updatePriority(directory.getId(), newPriority);
        directoryRepository.updatePriority(savedDirectory.getId(), directory.getPriority());
    }

    private void createDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

}
