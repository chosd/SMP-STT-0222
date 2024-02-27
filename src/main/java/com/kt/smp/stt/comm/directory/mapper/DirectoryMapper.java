package com.kt.smp.stt.comm.directory.mapper;

import com.kt.smp.stt.comm.directory.dto.DirectoryDto;
import com.kt.smp.stt.comm.directory.dto.DirectoryListDto;
import com.kt.smp.stt.comm.directory.dto.DirectorySaveDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DirectoryMapper {

    List<DirectoryListDto> findAll();
    List<DirectoryListDto> findTrainList();
    DirectoryDto findById(int id);
    List<DirectoryListDto> findByGroupId(int groupId);

    DirectoryDto findByGroupIdAndPriority(int groupId, int priority);

    DirectoryDto findByName(String name);

    int countByGroupIdAndName(int groupId, String name);

    int countByPath(String path);

    int countByGroupIdAndExcludedDirIdAndName(int groupId, int excludedDirId, String name);

    int countByExcludedDirIdAndPath(int excludedDirId, String path);

    void save(DirectorySaveDto newDirectory);

    void updateInUse(int id, String inUse);

    void updateNameAndPath(int id, String name, String path);

    void updatePriority(int id, int priority);
    void delete(Integer id);
}
